package me.indian.ostag.others;

import cn.nukkit.Server;
import me.indian.ostag.OsTag;
import me.indian.ostag.utils.ColorUtil;

import java.util.HashMap;
import java.util.Map;

public class OsTagMetrics {

    private static final OsTag plugin = OsTag.getInstance();

    public static void metricsStart() {
        try {
            int pluginId = 16838;
            Metrics metrics = new Metrics(plugin, pluginId);
            metrics.addCustomChart(new Metrics.SimplePie("server_movement", () -> String.valueOf(plugin.getConfig().getBoolean("PowerNukkiX-movement-server"))));
            metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> Server.getInstance().getNukkitVersion()));
            metrics.addCustomChart(new Metrics.SimplePie("ostag_vs_chatformater", () -> {
                String info = "";
                boolean ostag = plugin.getConfig().getBoolean("OsTag");
                boolean chatFormater = plugin.getConfig().getBoolean("ChatFormater");
                if (ostag && chatFormater) {
                    info = "OsTag and ChatFormater";
                }
                if (ostag && !chatFormater) {
                    info = "OsTag";
                }
                if (!ostag && chatFormater) {
                    info = "ChatFormater";
                }
                return info;
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
            plugin.getLogger().info(ColorUtil.replaceColorCode("&aLoaded Metrics"));
        } catch (Exception e) {
            plugin.getLogger().info(ColorUtil.replaceColorCode("&cCan't load metrics"));
        }
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
        }
        return "Unknown";
    }
}