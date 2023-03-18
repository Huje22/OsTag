package me.indian.ostag.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.indian.ostag.OsTag;

public class OtherUtils {

    private static final String fal = "§cfalse";
    private static final String tru = "§etrue";

    public static String getOsTagStatus() {
        String osTag = fal;
        if (OsTag.osTag) {
            osTag = tru;
        }
        return osTag;
    }

    public static String getFormaterStatus() {
        String formatter = fal;
        if (OsTag.chatFormatter) {
            formatter = tru;
        }
        return formatter;
    }

    public static String getLuckPermStatus() {
        String luckPerms = fal;
        if (OsTag.luckPerm) {
            luckPerms = tru;
        }
        return luckPerms;
    }

    public static String getKotOrPapiStatus() {
        String papiAndKotlinLib = fal;
        if (OsTag.papiAndKotlinLib) {
            papiAndKotlinLib = tru;
        }
        return papiAndKotlinLib;
    }

    public static void sendMessageToAll(String msg) {
        for (Player all : Server.getInstance().getOnlinePlayers().values()) {
            all.sendMessage(msg);
        }
    }
}