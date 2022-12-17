package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.indian.pl.OsTag;

public class OtherUtils {

    private static OsTag plugin = OsTag.getInstance();

    private static String fal = "§cfalse";

    private static String tru = "§etrue";

    public static String getOsTagStatus() {
        Boolean ostag = plugin.getConfig().getBoolean("OsTag");
        String os = fal;
        if (ostag != null) {
            if (ostag) {
                os = tru;
            }
        }
        return os;
    }

    public static String getFormaterStatus() {
        Boolean chatFormater = plugin.getConfig().getBoolean("ChatFormater");
        String forma = fal;
        if (chatFormater != null) {
            if (chatFormater) {
                forma = tru;
            }
        }
        return forma;
    }

    public static String getLuckPermStatus() {
        Boolean lp = plugin.luckPerm;
        String luck = fal;
        if (lp) {
            luck = tru;
        }
        return luck;
    }

    public static String getDeathSkullsStatus() {
        Boolean ds = plugin.deathSkulls;
        String deaths = fal;
        if (ds) {
            deaths = tru;
        }
        return deaths;
    }

    public static void sendMessageToAll(String msg) {
        for (Player all : Server.getInstance().getOnlinePlayers().values()) {
            all.sendMessage(msg);
        }
    }

}
