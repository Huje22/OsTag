package me.indian.ostag.basic;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.service.NKServiceManager;
import cn.nukkit.plugin.service.RegisteredServiceProvider;
import cn.nukkit.plugin.service.ServicePriority;
import cn.nukkit.utils.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.indian.ostag.util.ThreadUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

/**
 * bStats collects some data for plugin authors.
 * <p>
 * Check out https://bStats.org/ to learn more about bStats!
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Metrics {

    // The version of this bStats class
    public static final int B_STATS_VERSION = 1;
    // The url to which the data is sent
    private static final String URL = "https://bStats.org/submitData/bukkit";
    // Should failed requests be logged?
    private static boolean logFailedRequests;
    // Should the sent data be logged?
    private static boolean logSentData;
    // Should the response text be logged?
    private static boolean logResponseStatusText;
    // The uuid of the server
    private static String serverUUID;

    static {
        // You can use the property to disable the check in your test environment
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            // Maven's Relocate is clever and changes strings, too. So we have to use this little "trick" ... :D
            final String defaultPackage = new String(new byte[]{'o', 'r', 'g', '.', 'b', 's', 't', 'a', 't', 's', '.', 'n', 'u', 'k', 'k', 'i', 't'});
            final String examplePackage = new String(new byte[]{'y', 'o', 'u', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e'});
            // We want to make sure nobody just copy & pastes the example and use the wrong package names
            if (Metrics.class.getPackage().getName().equals(defaultPackage) || Metrics.class.getPackage().getName().equals(examplePackage)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }

    // The server
    private static Server server;
    // The plugin
    private final Plugin plugin;
    // Executor service for requests
    // We use an executor service because the Nukkit scheduler is affected by server lags
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, new ThreadUtil("bStats-Metrics"));
    // A list with all custom charts
    private final List<CustomChart> charts = new ArrayList<>();
    // Is bStats enabled on this server?
    private boolean enabled;

    public Metrics(final Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;
        server = this.plugin.getServer();

        try {
            this.loadConfig();
        } catch (final IOException e) {
            // Failed to load configuration
            plugin.getLogger().warning("Failed to load bStats config!", e);
        }

        if (this.enabled) {
            boolean found = false;
            // Search for all other bStats Metrics classes to see if we are the first one
            for (final Class<?> service : server.getServiceManager().getKnownService()) {
                try {
                    service.getField("B_STATS_VERSION"); // Our identifier :)
                    found = true; // We aren't the first
                    break;
                } catch (final NoSuchFieldException ignored) {
                }
            }
            // Register our service
            server.getServiceManager().register(Metrics.class, this, plugin, ServicePriority.NORMAL);
            if (!found) {
                // We are the first!
                this.startSubmitting();
            }
        }
    }

    /**
     * Sends the data to the bStats server.
     *
     * @param plugin Any plugin. It's just used to get a logger instance.
     * @param data   The data to send.
     * @throws Exception If the request failed.
     */
    private static void sendData(final Plugin plugin, final JsonObject data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        if (server.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        }
        if (logSentData) {
            plugin.getLogger().info("Sending data to bStats: " + data);
        }
        final HttpsURLConnection connection = (HttpsURLConnection) new URL(URL).openConnection();

        // Compress the data to save bandwidth
        final byte[] compressedData = compress(data.toString());

        // Add headers
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON format
        connection.setRequestProperty("User-Agent", "MC-Server/" + B_STATS_VERSION);

        // Send data
        connection.setDoOutput(true);
        try (final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(compressedData);
        }

        final StringBuilder builder = new StringBuilder();
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        }

        if (logResponseStatusText) {
            plugin.getLogger().info("Sent data to bStats and received response: " + builder);
        }
    }

    /**
     * Gzips the given String.
     *
     * @param str The string to gzip.
     * @return The gzipped String.
     * @throws IOException If the compression failed.
     */
    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        }
        return outputStream.toByteArray();
    }

    /**
     * Checks if bStats is enabled.
     *
     * @return Whether bStats is enabled or not.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     */
    public void addCustomChart(final CustomChart chart) {
        if (chart == null) {
            throw new IllegalArgumentException("Chart cannot be null!");
        }
        this.charts.add(chart);
    }

    /**
     * Starts the Scheduler which submits our data every 30 minutes.
     */
    private void startSubmitting() {
        final Runnable submitTask = () -> {
            if (!this.plugin.isEnabled()) { // Plugin was disabled
                this.scheduler.shutdown();
                return;
            }
            // Nevertheless we want our code to run in the Nukkit main thread, so we have to use the Nukkit scheduler
            // Don't be afraid! The connection to the bStats server is still async, only the stats collection is sync ;)
            server.getScheduler().scheduleTask(this.plugin, this::submitData);
        };

        // Many servers tend to restart at a fixed time at xx:00 which causes an uneven distribution of requests on the
        // bStats backend. To circumvent this problem, we introduce some randomness into the initial and second delay.
        // WARNING: You must not modify and part of this Metrics class, including the submit delay or frequency!
        // WARNING: Modifying this code will get your plugin banned on bStats. Just don't do it!
        final long initialDelay = (long) (1000 * 60 * (3 + Math.random() * 3));
        final long secondDelay = (long) (1000 * 60 * (Math.random() * 30));
        this.scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
        this.scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1000 * 60 * 30, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the plugin specific data.
     * This method is called using Reflection.
     *
     * @return The plugin specific data.
     */
    public JsonObject getPluginData() {
        final JsonObject data = new JsonObject();

        final String pluginName = this.plugin.getDescription().getName();
        final String pluginVersion = this.plugin.getDescription().getVersion();

        data.addProperty("pluginName", pluginName); // Append the name of the plugin
        data.addProperty("id", 16838); // Append the id of the plugin
        data.addProperty("pluginVersion", pluginVersion); // Append the version of the plugin

        final JsonArray customCharts = new JsonArray();
        for (final CustomChart customChart : this.charts) {
            // Add the data of the custom charts
            final JsonObject chart = customChart.getRequestJsonObject();
            if (chart == null) { // If the chart is null, we skip it
                continue;
            }
            customCharts.add(chart);
        }
        data.add("customCharts", customCharts);

        return data;
    }

    /**
     * Gets the server specific data.
     *
     * @return The server specific data.
     */
    private JsonObject getServerData() {
        // Minecraft specific data
        final int playerAmount = Server.getInstance().getOnlinePlayers().size();
        final int onlineMode = Server.getInstance().getPropertyBoolean("xbox-auth", true) ? 1 : 0;
        final String softwareVersion = Server.getInstance().getApiVersion() + " (MC: " + Server.getInstance().getVersion().substring(1) + ")";
        final String softwareName = Server.getInstance().getName();

        // OS/Java specific data
        final String javaVersion = System.getProperty("java.version");
        final String osName = System.getProperty("os.name");
        final String osArch = System.getProperty("os.arch");
        final String osVersion = System.getProperty("os.version");
        final int coreCount = Runtime.getRuntime().availableProcessors();

        final JsonObject data = new JsonObject();

        data.addProperty("serverUUID", serverUUID);

        data.addProperty("playerAmount", playerAmount);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("bukkitVersion", softwareVersion);
        data.addProperty("bukkitName", softwareName);

        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);

        return data;
    }

    /**
     * Collects the data and sends it afterwards.
     */
    @SuppressWarnings("unchecked")
    private void submitData() {
        final JsonObject data = this.getServerData();

        final JsonArray pluginData = new JsonArray();
        // Search for all other bStats Metrics classes to get their plugin data
        for (final Class<?> service : server.getServiceManager().getKnownService()) {
            try {
                service.getField("B_STATS_VERSION"); // Our identifier :)

                List<? extends RegisteredServiceProvider<?>> providers = null;
                try {
                    providers = server.getServiceManager().getRegistrations(service);
                } catch (final Throwable t) { // no such method: ServiceManager.getRegistrations()
                    try {
                        final Field handle = NKServiceManager.class.getDeclaredField("handle");
                        handle.setAccessible(true);
                        providers = ((Map<Class<?>, List<RegisteredServiceProvider<?>>>) handle.get(server.getServiceManager())).get(service);
                    } catch (final NullPointerException | NoSuchFieldException | IllegalAccessException |
                                   IllegalArgumentException e) {
                        if (logFailedRequests) {
                            this.plugin.getLogger().error("Encountered unexpected exception ", e);
                        }
                    }
                }

                if (providers != null) {
                    for (final RegisteredServiceProvider<?> provider : providers) {
                        try {
                            final Object plugin = provider.getService().getMethod("getPluginData").invoke(provider.getProvider());
                            if (plugin instanceof JsonObject) {
                                pluginData.add((JsonElement) plugin);
                            }
                        } catch (final NullPointerException | NoSuchMethodException | IllegalAccessException |
                                       InvocationTargetException ignored) {
                        }
                    }
                }
            } catch (final NoSuchFieldException ignored) {
            }
        }

        data.add("plugins", pluginData);

        // Create a new thread for the connection to the bStats server
        new Thread(() -> {
            try {
                // Send the data
                sendData(this.plugin, data);
            } catch (final Exception e) {
                // Something went wrong! :(
                if (logFailedRequests) {
                    this.plugin.getLogger().warning("Could not submit plugin stats of " + this.plugin.getName(), e);
                }
            }
        }).start();
    }

    /**
     * Loads the bStats configuration.
     *
     * @throws IOException If something did not work :(
     */
    private void loadConfig() throws IOException {
        final File bStatsFolder = new File(this.plugin.getDataFolder().getParentFile(), "bStats");
        final File configFile = new File(bStatsFolder, "config.yml");
        final Config config = new Config(configFile);

        // Check if the config file exists
        if (!config.exists("serverUuid")) {
            // Add default values
            config.set("enabled", true);
            // Every server gets it's unique random id.
            config.set("serverUuid", UUID.randomUUID().toString());
            // Should failed request be logged?
            config.set("logFailedRequests", false);
            // Should the sent data be logged?
            config.set("logSentData", false);
            // Should the response text be logged?
            config.set("logResponseStatusText", false);

            final DumperOptions dumperOptions = new DumperOptions();
            dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            this.writeFile(configFile,
                    "# bStats collects some data for plugin authors like how many servers are using their plugins.",
                    "# To honor their work, you should not disable it.",
                    "# This has nearly no effect on the server performance!",
                    "# Check out https://bStats.org/ to learn more :)",
                    new Yaml(dumperOptions).dump(config.getRootSection()));
        }

        // Load the data
        this.enabled = config.getBoolean("enabled", true);
        serverUUID = config.getString("serverUuid");
        logFailedRequests = config.getBoolean("logFailedRequests", false);
        logSentData = config.getBoolean("logSentData", false);
        logResponseStatusText = config.getBoolean("logResponseStatusText", false);
    }

    /**
     * Writes a String to a file. It also adds a note for the user.
     *
     * @param file  The file to write to. Cannot be null.
     * @param lines The lines to write.
     * @throws IOException If something did not work :(
     */
    private void writeFile(final File file, final String... lines) throws IOException {
        try (final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (final String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        }
    }

    /**
     * Represents a custom chart.
     */
    public static abstract class CustomChart {

        // The id of the chart
        final String chartId;

        /**
         * Class constructor.
         *
         * @param chartId The id of the chart.
         */
        CustomChart(final String chartId) {
            if (chartId == null || chartId.isEmpty()) {
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            }
            this.chartId = chartId;
        }

        private JsonObject getRequestJsonObject() {
            final JsonObject chart = new JsonObject();
            chart.addProperty("chartId", this.chartId);
            try {
                final JsonObject data = this.getChartData();
                if (data == null) {
                    // If the data is null we don't send the chart.
                    return null;
                }
                chart.add("data", data);
            } catch (final Throwable t) {
                if (logFailedRequests) {
                    server.getLogger().warning("Failed to get data for custom chart with id " + this.chartId, t);
                }
                return null;
            }
            return chart;
        }

        protected abstract JsonObject getChartData() throws Exception;

    }

    /**
     * Represents a custom simple pie.
     */
    public static class SimplePie extends CustomChart {

        private final Callable<String> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public SimplePie(final String chartId, final Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            final JsonObject data = new JsonObject();
            final String value = this.callable.call();
            if (value == null || value.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            data.addProperty("value", value);
            return data;
        }
    }

    /**
     * Represents a custom advanced pie.
     */
    public static class AdvancedPie extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public AdvancedPie(final String chartId, final Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            final JsonObject data = new JsonObject();
            final JsonObject values = new JsonObject();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean allSkipped = true;
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
                    continue; // Skip this invalid
                }
                allSkipped = false;
                values.addProperty(entry.getKey(), entry.getValue());
            }
            if (allSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }
    }

    /**
     * Represents a custom drilldown pie.
     */
    public static class DrilldownPie extends CustomChart {

        private final Callable<Map<String, Map<String, Integer>>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public DrilldownPie(final String chartId, final Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        public JsonObject getChartData() throws Exception {
            final JsonObject data = new JsonObject();
            final JsonObject values = new JsonObject();
            final Map<String, Map<String, Integer>> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean reallyAllSkipped = true;
            for (final Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
                final JsonObject value = new JsonObject();
                boolean allSkipped = true;
                for (final Map.Entry<String, Integer> valueEntry : map.get(entryValues.getKey()).entrySet()) {
                    value.addProperty(valueEntry.getKey(), valueEntry.getValue());
                    allSkipped = false;
                }
                if (!allSkipped) {
                    reallyAllSkipped = false;
                    values.add(entryValues.getKey(), value);
                }
            }
            if (reallyAllSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }
    }

    /**
     * Represents a custom single line chart.
     */
    public static class SingleLineChart extends CustomChart {

        private final Callable<Integer> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public SingleLineChart(final String chartId, final Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            final JsonObject data = new JsonObject();
            final int value = this.callable.call();
            if (value == 0) {
                // Null = skip the chart
                return null;
            }
            data.addProperty("value", value);
            return data;
        }

    }

    /**
     * Represents a custom multi line chart.
     */
    public static class MultiLineChart extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public MultiLineChart(final String chartId, final Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            final JsonObject data = new JsonObject();
            final JsonObject values = new JsonObject();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean allSkipped = true;
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) {
                    continue; // Skip this invalid
                }
                allSkipped = false;
                values.addProperty(entry.getKey(), entry.getValue());
            }
            if (allSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }

    }

    /**
     * Represents a custom simple bar chart.
     */
    public static class SimpleBarChart extends CustomChart {

        private final Callable<Map<String, Integer>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public SimpleBarChart(final String chartId, final Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            final JsonObject data = new JsonObject();
            final JsonObject values = new JsonObject();
            final Map<String, Integer> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            for (final Map.Entry<String, Integer> entry : map.entrySet()) {
                final JsonArray categoryValues = new JsonArray();
                categoryValues.add(new JsonPrimitive(entry.getValue()));
                values.add(entry.getKey(), categoryValues);
            }
            data.add("values", values);
            return data;
        }

    }

    /**
     * Represents a custom advanced bar chart.
     */
    public static class AdvancedBarChart extends CustomChart {

        private final Callable<Map<String, int[]>> callable;

        /**
         * Class constructor.
         *
         * @param chartId  The id of the chart.
         * @param callable The callable which is used to request the chart data.
         */
        public AdvancedBarChart(final String chartId, final Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        @Override
        protected JsonObject getChartData() throws Exception {
            final JsonObject data = new JsonObject();
            final JsonObject values = new JsonObject();
            final Map<String, int[]> map = this.callable.call();
            if (map == null || map.isEmpty()) {
                // Null = skip the chart
                return null;
            }
            boolean allSkipped = true;
            for (final Map.Entry<String, int[]> entry : map.entrySet()) {
                if (entry.getValue().length == 0) {
                    continue; // Skip this invalid
                }
                allSkipped = false;
                final JsonArray categoryValues = new JsonArray();
                for (final int categoryValue : entry.getValue()) {
                    categoryValues.add(new JsonPrimitive(categoryValue));
                }
                values.add(entry.getKey(), categoryValues);
            }
            if (allSkipped) {
                // Null = skip the chart
                return null;
            }
            data.add("values", values);
            return data;
        }
    }

}