package me.indian.ostag.basic;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.TextUtil;
import me.indian.ostag.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OsTagMetrics {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadUtil("Ostag Metrics Thread"));
    private final OsTag plugin = OsTag.getInstance();
    private final Config config = this.plugin.getConfig();
    private final PluginLogger logger = this.plugin.getLogger();
    private final String debugPrefix = TextUtil.replaceColorCode(this.plugin.publicDebugPrefix + "&8[&dMetrics&8] ");
    private final Metrics metrics = new Metrics(this.plugin, 16838);
    public boolean enabled = this.metrics.isEnabled();
    public boolean isRunning;

    public void run() {
        executorService.execute(() -> {
            try {
                if (!this.enabled) {
                    this.logger.info(TextUtil.replaceColorCode("&aMetrics is disabled"));
                    this.isRunning = false;
                    Thread.currentThread().interrupt();
                    return;
                }
                this.customMetrics();
                this.isRunning = true;
                this.logger.info(TextUtil.replaceColorCode("&aLoaded Metrics"));
            } catch (final Exception e) {
                this.isRunning = false;
                this.logger.info(TextUtil.replaceColorCode("&cCan't load metrics"));
                if (this.plugin.debug) {
                    this.logger.error(this.debugPrefix + e);
                }
                Thread.currentThread().interrupt();
            }
        });
    }

    private void customMetrics() {
        this.metrics.addCustomChart(new Metrics.SimplePie("server_movement", () -> String.valueOf(this.plugin.serverMovement)));
        this.metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> Server.getInstance().getNukkitVersion()));

        this.metrics.addCustomChart(new Metrics.SimplePie("ostag_vs_chatformater", () -> {
            String ostagVsFormater = "All disabled";
            final boolean ostag = this.plugin.osTag;
            final boolean chatFormater = this.plugin.chatFormatter;
            if (ostag && chatFormater) {
                ostagVsFormater = "OsTag and ChatFormater";
            }
            if (ostag && !chatFormater) {
                ostagVsFormater = "OsTag";
            }
            if (!ostag && chatFormater) {
                ostagVsFormater = "ChatFormater";
            }
            return ostagVsFormater;
        }));

        this.metrics.addCustomChart(new Metrics.SimplePie("scoretag_vs_nametag", () -> {
            String scoreVsName = "All disabled";
            if (this.plugin.osTag) {
                final boolean nametag = this.plugin.nametag;
                final boolean scoreTag = this.plugin.scoreTag;
                if (nametag && scoreTag) {
                    scoreVsName = "NameTag and ScoreTag";
                }
                if (nametag && !scoreTag) {
                    scoreVsName = "NameTag";
                }
                if (!nametag && scoreTag) {
                    scoreVsName = "ScoreTag";
                }
            }
            return scoreVsName;
        }));

        this.metrics.addCustomChart(new Metrics.SimplePie("updatecheck_vs_autoupdate", () -> {
            String updateVsAuto = "All disabled";
            final boolean update = this.plugin.upDatechecker;
            final boolean auto = this.config.getBoolean("AutoUpdate");
            if (update && auto) {
                updateVsAuto = "Updatechecker and AutoUpdate";
            }
            //change it to map and rename to `functions` in bstats
            if (update && !auto) {
                updateVsAuto = "UpdateChecker";
            }
            if (!update && auto) {
                updateVsAuto = "AutoUpdate";
            }
            return updateVsAuto;
        }));

        this.metrics.addCustomChart(new Metrics.AdvancedPie("plugins", () -> {
            final Map<String, Integer> valueMap = new HashMap<>();
            final Map<String, Plugin> pluginMap = this.plugin.getServer().getPluginManager().getPlugins();

            for (Map.Entry<String, Plugin> entry : pluginMap.entrySet()) {
                final String key = entry.getKey();
                if (key.equalsIgnoreCase("LuckPerms") || key.equalsIgnoreCase("PlaceholderAPI") || key.equalsIgnoreCase("KotlinLib") ) {
                    if (!valueMap.containsKey(key)) {
                        valueMap.put(key, 1);
                    }
                }
            }
            return valueMap;
        }));

        /*
        Code from https://github.com/CloudburstMC/Nukkit/blob/master/src/main/java/cn/nukkit/metrics/NukkitMetrics.java#L47
         */
        this.metrics.addCustomChart(new Metrics.SimplePie("xbox_auth", () -> this.plugin.getServer().getPropertyBoolean("xbox-auth") ? "Required" : "Not required"));
        this.metrics.addCustomChart(new Metrics.AdvancedPie("player_platform", () -> {
            final Map<String, Integer> valueMap = new HashMap<>();
            this.plugin.getServer().getOnlinePlayers().forEach((uuid, player) -> {
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
            this.plugin.getServer().getOnlinePlayers().forEach((uuid, player) -> {
                final String gameVersion = player.getLoginChainData().getGameVersion();
                if (!valueMap.containsKey(gameVersion)) {
                    valueMap.put(gameVersion, 1);
                } else {
                    valueMap.put(gameVersion, valueMap.get(gameVersion) + 1);
                }
            });
            return valueMap;
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
                return "Windows 10";
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
        return "Unknown";
    }
}
