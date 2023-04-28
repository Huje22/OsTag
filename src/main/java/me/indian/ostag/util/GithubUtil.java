package me.indian.ostag.util;

import cn.nukkit.plugin.PluginLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import me.indian.ostag.OsTag;

public class GithubUtil {

    private static final OsTag plugin = OsTag.getInstance();
    private static final PluginLogger logger = plugin.getLogger();
    private static final Map<Integer, String> versions = new TreeMap<>();
    private static final StringBuilder response = new StringBuilder();
    private static final String debugPrefix = ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&8[&dLatest tag&8] ");
    private static final String current = OsTag.getInstance().getDescription().getVersion();
    private static final String latest = getLatestTag();
    private static final String errorMessage = "&cCan't get latest tag";

    public static String getFastTagInfo() {
        if (latest.equals(errorMessage)) {
            return ColorUtil.replaceColorCode(errorMessage);
        }
        if (current.equals(latest)) {
            return ColorUtil.replaceColorCode("&etrue");
        } else {
            return ColorUtil.replaceColorCode("&4false");
        }
    }

    public static String checkTagCompatibility() {
        String tag = "&aYou are running latest version";
        if (latest.equals(errorMessage)) {
            return ColorUtil.replaceColorCode(errorMessage);
        }
        if (!current.equals(latest)) {
            tag = "&aNew update available, your version &b" + current + "&a latest version &b" + latest;
        }
        return ColorUtil.replaceColorCode(tag);
    }

    public static String getLatestTag() {
        try {
            URL url = new URL("https://api.github.com/repos/OpenPlugins-Minecraft/OsTag/tags");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            int responseCode = connection.getResponseCode();
            if (!(responseCode == HttpURLConnection.HTTP_OK)) {
                if (plugin.debug) {
                    logger.info(ColorUtil.replaceColorCode(debugPrefix + "&cCan't get latest tag, HTTP response code: " + responseCode));
                }
                return errorMessage;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            return parseLatestTagFromJson(response.toString());
        } catch (IOException e) {
            return errorMessage;
        }
    }

    public static String getBehindCount() {
        if (response.toString().isEmpty()) return " ";
        String json = response.toString();
        String[] tags = json.replaceAll("[\\[\\]{}\"]", "").split(",");

        int counter = 0;
        int index = -1;

        for (int i = 0; i < 66; i += 6) {
            versions.put(counter, tags[i].split(":")[1]);
            counter++;
        }

        if (plugin.debug) {
            for (Map.Entry<Integer, String> entry : versions.entrySet()) {
                logger.info(debugPrefix + entry.getKey() + ": " + entry.getValue());
            }
        }

        for (Map.Entry<Integer, String> entry : versions.entrySet()) {
            if (entry.getValue().equals(current)) {
                index = entry.getKey();
                break;
            }
        }

        if (index == -1) {
            return ColorUtil.replaceColorCode(" &5(&cBehind more than 10 version&5)");
        }
        if (index == 0) {
            return "";
        }

        return ColorUtil.replaceColorCode(" &5(&c" + index + " &dVersions behind&5)");
    }


    public static int getBehindIntCount() {
        if (response.toString().isEmpty()) return 0;
        String json = response.toString();
        String[] tags = json.replaceAll("[\\[\\]{}\"]", "").split(",");

        int counter = 0;
        int index = -1;

        for (int i = 0; i < 66; i += 6) {
            versions.put(counter, tags[i].split(":")[1]);
            counter++;
        }

        if (plugin.debug) {
            for (Map.Entry<Integer, String> entry : versions.entrySet()) {
                logger.info(debugPrefix + entry.getKey() + ": " + entry.getValue());
            }
        }

        for (Map.Entry<Integer, String> entry : versions.entrySet()) {
            if (entry.getValue().equals(current)) {
                index = entry.getKey();
                break;
            }
        }

        if (index == -1) {
            return index;
        }
        if (index == 0) {
            return 0;
        }

        return index;
    }


    private static String parseLatestTagFromJson(String json) {
        String[] tags = json.replaceAll("[\\[\\]{}\"]", "").split(",");
        return tags[0].split(":")[1];
    }
}