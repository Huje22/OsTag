package me.indian.ostag.basic;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerMentionConfig;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.ThreadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsTagMetrics {

    private final OsTag plugin;
    private final Server server;
    private final Config config;
    private final PlayerMentionConfig mentionConfig;
    private final PluginManager pluginManager;
    private final PluginLogger logger;
    private final Metrics metrics;
    public boolean enabled;


    public OsTagMetrics() {
        this.plugin = OsTag.getInstance();
        this.server = this.plugin.getServer();
        this.config = this.plugin.getConfig();
        this.mentionConfig = this.plugin.getPlayersMentionConfig();
        this.pluginManager = this.server.getPluginManager();
        this.logger = this.plugin.getLogger();
        this.metrics = new Metrics(this.plugin);
        this.enabled = this.metrics.isEnabled();
    }

    public void run() {
        new ThreadUtil("Ostag Metrics Thread", () -> {
            try {
                if (!this.enabled) {
                    this.logger.info(MessageUtil.colorize("&aMetrics is disabled"));
                    Thread.currentThread().interrupt();
                    return;
                }
                this.customMetrics();
                this.logger.info(MessageUtil.colorize("&aLoaded Metrics"));
            } catch (final Exception e) {
                this.logger.info(MessageUtil.colorize("&cCan't load metrics"));
                if (this.plugin.debug) {
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
            }
        }).newThread().start();
    }

    private void customMetrics() {
        this.metrics.addCustomChart(new Metrics.SimplePie("server_movement", () -> String.valueOf(this.plugin.serverMovement)));
        this.metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> server.getNukkitVersion() + " (MC: " + server.getVersion() + " Nukkit API: " + server.getApiVersion() + " Version " + this.plugin.getDescription().getVersion() + ")"));
        this.metrics.addCustomChart(new Metrics.SimplePie("refresh_time", () -> {
            if (this.plugin.osTag) {
                return this.plugin.getOsTimer().getRefreshTime() + " ticks";
            } else {
                return "";
            }
        }));
        this.metrics.addCustomChart(new Metrics.SimplePie("cps_limit", () -> {
            if (this.plugin.cpsLimit) {
                return this.plugin.getCpsLimiter().getMaxCps() + " cps";
            } else {
                return "Cps Limiter disabled";
            }
        }));
        this.metrics.addCustomChart(new Metrics.AdvancedPie("functions", () -> {
            final Map<String, Integer> functionMap = new HashMap<>();

            final boolean nametag = this.plugin.nametag;
            final boolean scoreTag = this.plugin.scoreTag;
            final boolean update = this.plugin.upDatechecker;
            final boolean auto = this.config.getBoolean("AutoUpdate");
            final boolean debug = this.plugin.debug;
            final boolean censor = this.config.getBoolean("censorship.enable");
            final boolean breaks = this.config.getBoolean("break-between-messages.enable");
            final boolean cooldown = this.config.getBoolean("cooldown.enable");
            final boolean andForAll = this.config.getBoolean("And-for-all");
            final boolean formsDebug = this.config.getBoolean("FormsDebug");
            final boolean mentions = this.config.getBoolean("MentionSound");

            if (scoreTag) {
                functionMap.put("ScoreTag", 1);
            }
            if (nametag) {
                functionMap.put("NameTag", 1);
            }
            if (update) {
                functionMap.put("UpdateChecker", 1);
            }
            if (auto) {
                functionMap.put("AutoUpdate", 1);
            }
            if (debug) {
                functionMap.put("Debug", 1);
            }
            if (censor) {
                functionMap.put("Censorship", 1);
            }
            if (breaks) {
                functionMap.put("Breaks Between Messages", 1);
            }
            if (cooldown) {
                functionMap.put("Cooldown", 1);
            }
            if (andForAll) {
                functionMap.put("And For All", 1);
            }
            if (formsDebug) {
                functionMap.put("Froms Debug", 1);
            }
            if (mentions) {
                functionMap.put("Mention sound", 1);
            }

            return functionMap;
        }));
        this.metrics.addCustomChart(new Metrics.AdvancedPie("main_functions", () -> {
            final Map<String, Integer> functionMap = new HashMap<>();

            final boolean ostag = this.plugin.osTag;
            final boolean formater = this.plugin.chatFormatter;
            final boolean cpsLimiter = this.plugin.cpsLimit;

            if (ostag) {
                functionMap.put("OsTag", 1);
            }
            if (formater) {
                functionMap.put("ChatFormatter", 1);
            }
            if (cpsLimiter) {
                functionMap.put("CpsLimiter", 1);
            }

            return functionMap;
        }));
        this.metrics.addCustomChart(new Metrics.AdvancedPie("mention_sounds", () -> {
            final Map<String, Integer> sounds = new HashMap<>();
            if (!this.mentionConfig.mentionSoundFunctionEnabled()) {
                sounds.put("Disabled", 1);
                return sounds;
            }
            this.server.getOnlinePlayers().forEach(((uuid, player) -> {
                final String sound = this.mentionConfig.getMentionSound(player);
                if (!sounds.containsKey(sound)) {
                    sounds.put(sound, 1);
                } else {
                    sounds.put(sound, sounds.get(sound) + 1);
                }
            }));
            return sounds;
        }));

        /*
        Code from https://github.com/CloudburstMC/Nukkit/blob/master/src/main/java/cn/nukkit/metrics/NukkitMetrics.java#L47
         */
        this.metrics.addCustomChart(new Metrics.SimplePie("xbox_auth", () -> this.server.getPropertyBoolean("xbox-auth") ? "Required" : "Not required"));
        this.metrics.addCustomChart(new Metrics.AdvancedPie("player_platform", () -> {
            final Map<String, Integer> valueMap = new HashMap<>();
            this.server.getOnlinePlayers().forEach((uuid, player) -> {
                final String deviceOS = this.mapDeviceOSToString(player.getLoginChainData().getDeviceOS());
                if (!valueMap.containsKey(deviceOS)) {
                    valueMap.put(deviceOS, 1);
                } else {
                    valueMap.put(deviceOS, valueMap.get(deviceOS) + 1);
                }
            });
            return valueMap;
        }));
        this.metrics.addCustomChart(new Metrics.AdvancedPie("player_game_version", () -> {
            final Map<String, Integer> valueMap = new HashMap<>();
            this.server.getOnlinePlayers().forEach((uuid, player) -> {
                final String gameVersion = player.getLoginChainData().getGameVersion();
                if (!valueMap.containsKey(gameVersion)) {
                    valueMap.put(gameVersion, 1);
                } else {
                    valueMap.put(gameVersion, valueMap.get(gameVersion) + 1);
                }
            });
            return valueMap;
        }));

        // Plugins 

        this.addPluginWithVersionsInfo();
    }
    
    private void addPluginWithVersionsInfo() {
        final List<String> plugins = new ArrayList<>();
        plugins.add("LuckPerms");
        plugins.add("FormConstructor");
        plugins.add("PlaceholderAPI");
        plugins.add("KotlinLib");

        for (final String plg : plugins) {
            this.sendPluginsData(plg);
        }

//      Maybe someday it will be used xD
//        final  Map<String , Plugin> plugins = this.server.getPluginManager().getPlugins();
//        for(Map.Entry<String , Plugin> entry : plugins.entrySet()){
//            this.sendPluginsData(entry.getKey());
//        }
    }

    private void sendPluginsData(final String pluginName) {
        this.metrics.addCustomChart(new Metrics.DrilldownPie("plugins", () -> {
            final Map<String, Map<String, Integer>> valmap = new HashMap<>();
            final Map<String, Integer> assets = new HashMap<>();
            final Plugin plg = this.pluginManager.getPlugin(pluginName);
            if (plg != null) {
                final String ver = plg.getDescription().getVersion();
                assets.put(ver, 1);
                valmap.put(pluginName, assets);
            }
            return valmap;
        }));
    }

    private String mapDeviceOSToString(final int os) {
        switch (os) {
            case 1:
                return "Android";
            case 2:
                return "iOS";
            case 3:
                return "macOS";
            case 4:
                return "FireOS";
            case 5:
                return "Gear VR";
            case 6:
                return "Hololens";
            case 7:
            case 8:
                return "Windows";
            case 9:
                return "Dedicated";
            case 10:
                return "tvos";
            case 11:
                return "PlayStation";
            case 12:
                return "Switch";
            case 13:
                return "Xbox One";
            case 14:
                return "Windows Phone";
            case 15:
                return "Linux";
        }
        return "Unknown (" + os + ")";
    }
}
