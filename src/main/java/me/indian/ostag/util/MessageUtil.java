package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

import java.util.List;

public class MessageUtil {

    public static String colorize(final String msg) {
        return TextFormat.colorize('&', msg);
    }

    public static String listToSpacedString(List<String> lista) {
        if (lista == null) {
            return "";
        }
        return String.join("\n", lista);
    }

    public static void sendMessageToAll(final String msg) {
        for (final Player all : Server.getInstance().getOnlinePlayers().values()) {
            all.sendMessage(msg);
        }
    }
}