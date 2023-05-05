package me.indian.ostag.other;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.ColorUtil;

public class OsTagMetrics {

    private final OsTag plugin = OsTag.getInstance();
    private final PluginLogger logger = plugin.getLogger();
    private final Config config = plugin.getConfig();
    private final String debugPrefix = ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&8[&dMetrics&8] ");
    private final Metrics metrics = new Metrics(plugin, 16838);
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public boolean isRunning;
    public boolean enabled = metrics.isEnabled();

    public void run() {
        executorService.execute(() -> {
            try {
                if (!enabled) {
                    logger.info(ColorUtil.replaceColorCode("&aMetrics is disabled"));
                    isRunning = false;
                    Thread.currentThread().interrupt();
                    return;
                }
                metrics();
                isRunning = true;
                logger.info(ColorUtil.replaceColorCode("&aLoaded Metrics"));
            } catch (Exception e) {
                isRunning = false;
                logger.info(ColorUtil.replaceColorCode("&cCan't load metrics"));
                if(plugin.debug) {
                    logger.error(debugPrefix + e);
                }
                Thread.currentThread().interrupt();
            }
        });
    }

    private void metrics() {
        metrics.addCustomChart(new Metrics.SimplePie("server_movement", () -> String.valueOf(plugin.serverMovement)));
        metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> Server.getInstance().getNukkitVersion()));
        metrics.addCustomChart(new Metrics.SimplePie("ostag_vs_chatformater", () -> {
            String info1 = "All disabled";
            final boolean ostag = plugin.osTag;
            final boolean chatFormater = plugin.chatFormatter;
            if (ostag && chatFormater) {
                info1 = "OsTag and ChatFormater";
            }
            if (ostag && !chatFormater) {
                info1 = "OsTag";
            }
            if (!ostag && chatFormater) {
                info1 = "ChatFormater";
            }
            return info1;
        }));
        metrics.addCustomChart(new Metrics.SimplePie("scoretag_vs_nametag", () -> {
            String info2 = "All disabled";
            if (plugin.osTag) {
                final boolean nametag = config.getBoolean("NameTag");
                final boolean scoreTag = config.getBoolean("ScoreTag");
                if (nametag && scoreTag) {
                    info2 = "NameTag and ScoreTag";
                }
                if (nametag && !scoreTag) {
                    info2 = "NameTag";
                }
                if (!nametag && scoreTag) {
                    info2 = "ScoreTag";
                }
            }
            return info2;
        }));
        /*
        Code from https://github.com/CloudburstMC/Nukkit/blob/master/src/main/java/cn/nukkit/metrics/NukkitMetrics.java#L47
         */
        metrics.addCustomChart(new Metrics.SimplePie("xbox_auth", () -> plugin.getServer().getPropertyBoolean("xbox-auth") ? "Required" : "Not required"));
        metrics.addCustomChart(new Metrics.AdvancedPie("player_platform", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            plugin.getServer().getOnlinePlayers().forEach((uuid, player) -> {
                String deviceOS = mapDeviceOSToString(player.getLoginChainData().getDeviceOS());
                if (!valueMap.containsKey(deviceOS)) {
                    valueMap.put(deviceOS, 1);
                } else {
                    valueMap.put(deviceOS, valueMap.get(deviceOS) + 1);
                }
            });
            return valueMap;
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("player_game_version", () -> {
            Map<String, Integer> valueMap = new HashMap<>();
            plugin.getServer().getOnlinePlayers().forEach((uuid, player) -> {
                String gameVersion = player.getLoginChainData().getGameVersion();
                if (!valueMap.containsKey(gameVersion)) {
                    valueMap.put(gameVersion, 1);
                } else {
                    valueMap.put(gameVersion, valueMap.get(gameVersion) + 1);
                }
            });
            return valueMap;
        }));
    }

    private String mapDeviceOSToString(int os) {
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
