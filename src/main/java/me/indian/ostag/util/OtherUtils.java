package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import me.indian.ostag.OsTag;


public class OtherUtils {

    private static final OsTag plugin = OsTag.getInstance();
    private static final String fal = "§cfalse";
    private static final String tru = "§etrue";

    public static String getOsTagStatus() {
        String osTag = fal;
        if (plugin.osTag) {
            osTag = tru;
        }
        return osTag;
    }

    public static String getFormaterStatus() {
        String formatter = fal;
        if (plugin.chatFormatter) {
            formatter = tru;
        }
        return formatter;
    }

    public static String getLuckPermStatus() {
        String luckPerms = fal;
        if (plugin.luckPerm) {
            luckPerms = tru;
        }
        return luckPerms;
    }

    public static String getKotOrPapiStatus() {
        String papiAndKotlinLib = fal;
        if (plugin.papiAndKotlinLib) {
            papiAndKotlinLib = tru;
        }
        return papiAndKotlinLib;
    }

    public static String getMetrics() {
        String metrics = fal;
        if (plugin.getOstagMetrics().isRunning) {
            metrics = tru;
        }
        return metrics;
    }

    public static void sendMessageToAll(String msg) {
        for (Player all : Server.getInstance().getOnlinePlayers().values()) {
            all.sendMessage(msg);
        }
    }
}