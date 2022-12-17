package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.indian.pl.OsTag;

public class OtherUtils {


    private static String fal = "§cfalse";
    private static String tru = "§etrue";

    public static String getOsTagStatus(OsTag plugin) {
        Boolean ostag = plugin.getConfig().getBoolean("OsTag");
        String os = fal;
        if (ostag != null) {
            if (ostag) {
                os = tru;
            }
        }
        return os;
    }

    public static String getFormaterStatus(OsTag plugin) {
        Boolean chatFormater = plugin.getConfig().getBoolean("ChatFormater");
        String forma = fal;
        if (chatFormater != null) {
            if (chatFormater) {
                forma = tru;
            }
        }
        return forma;
    }

    public static String getLuckPermStatus(OsTag plugin) {
        Boolean lp = plugin.luckPerm;
        String luck = fal;
        if (lp) {
            luck = tru;
        }
        return luck;
    }

    public static String getDeathSkullsStatus(OsTag plugin) {
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
