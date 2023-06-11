package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

public class TextUtil {

    public static String colorize(final String msg) {
        return TextFormat.colorize('&', msg);
    }

    public static void sendMessageToAll(final String msg) {
        for (final Player all : Server.getInstance().getOnlinePlayers().values()) {
            all.sendMessage(msg);
        }
    }
}