package me.indian.ostag.others;

import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import java.util.HashMap;
import java.util.Map;
import me.indian.ostag.OsTag;
import me.indian.ostag.utils.ColorUtil;

public class OsTagMetrics extends Task implements Runnable {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static final Metrics metrics = new Metrics(plugin, 16838);
    private boolean notRunned = true;

    @Override
    public void onRun(int i) {
        new Thread(() -> {
            try {
                if (!metrics.isEnabled()) {
                    plugin.getLogger().info(ColorUtil.replaceColorCode("&aMetrics is disabled"));
                    this.cancel();
                    return;
                }
                metrics();
                if (notRunned) {
                    notRunned = false;
                    plugin.getLogger().info(ColorUtil.replaceColorCode("&aLoaded Metrics"));
                }
            } catch (Exception e) {
                plugin.getLogger().info(ColorUtil.replaceColorCode("&cCan't load metrics"));
                System.out.println(e.getMessage());
                this.cancel();
            }
        }).start();
    }

    private static void metrics() throws Exception {
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

    private static String mapDeviceOSToString(int os) {
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
