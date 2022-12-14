package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.indian.pl.OsTag;

public class OtherUtils {


    private static String fal = "§cfalse";
    private static String tru = "§etrue";

    public static String getOsTagStatus(OsTag plugin) {
        Boolean ostag = plugin.getConfig().getBoolean("OsTag");
        String os = "";
        if (ostag != null) {
            if (ostag == true) {
                os = tru;
            } else {
                os = fal;
            }
        }
        return os;
    }

    public static String getFormaterStatus(OsTag plugin) {
        Boolean chatFormater = plugin.getConfig().getBoolean("ChatFormater");
        String forma = "";
        if (chatFormater != null) {
            if (chatFormater == true) {
                forma = tru;
            } else {
                forma = fal;
            }
        }
        return forma;
    }

    public static String getLuckPermStatus(OsTag plugin) {
        Boolean lp = plugin.luckPerm;
        String luck = "";
        if (lp == true) {
            luck = tru;
        } else {
            luck = fal;
        }
        return luck;
    }

    public static String getDeathSkullsStatus(OsTag plugin) {
        Boolean ds = plugin.deathSkulls;
        String deaths = "";
        if (ds == true) {
            deaths = tru;
        } else {
            deaths = fal;
        }
        return deaths;
    }

    public static void sendMessageToAll(String msg) {
        for (Player all : Server.getInstance().getOnlinePlayers().values()) {
            all.sendMessage(msg);
        }
    }

}
