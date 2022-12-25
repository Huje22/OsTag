package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.indian.pl.OsTag;

public class OtherUtils {

    private static final OsTag plugin = OsTag.getInstance();

    private static final String fal = "§cfalse";

    private static final String tru = "§etrue";

    public static String getOsTagStatus() {
        boolean ostag = plugin.getConfig().getBoolean("OsTag");
        String os = fal;
            if (ostag) {
                os = tru;
        }
        return os;
    }

    public static String getFormaterStatus() {
        boolean chatFormater = plugin.getConfig().getBoolean("ChatFormater");
        String forma = fal;
            if (chatFormater) {
                forma = tru;
        }
        return forma;
    }

    public static String getLuckPermStatus() {
        boolean lp = OsTag.luckPerm;
        String luck = fal;
        if (lp) {
            luck = tru;
        }
        return luck;
    }

    public static String getKotOrPapiStatus() {
        boolean papko = OsTag.papKot;
        String papkot = fal;
        if (papko) {
            papkot = tru;
        }
        return papkot;
    }

    public static void sendMessageToAll(String msg) {
        for (Player all : Server.getInstance().getOnlinePlayers().values()) {
            all.sendMessage(msg);
        }
    }

}
