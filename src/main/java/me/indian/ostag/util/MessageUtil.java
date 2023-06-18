package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageUtil {

    private static final Server server = Server.getInstance();

    public static String colorize(final String msg) {
        return TextFormat.colorize('&', msg);
    }

    public static String listToSpacedString(final List<String> lista) {
        if (lista == null) {
            return "";
        }
        return String.join("\n", lista);
    }

    public static String listToString(final List<String> lista, String split) {
        if (split == null) {
            split = " ";
        }

        if (lista == null) {
            return "";
        }
        return String.join(split, lista);
    }

    public static List<String> stringToList(final String text, String split) {
        if (split == null) {
            split = " ";
        }

        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(text.split(split));
    }

    public static void playerCommand(final Player player, final String command) {
        server.dispatchCommand(player, command);
    }

    public static void consoleCommand(final String command) {
        server.dispatchCommand(server.getConsoleSender(), command);
    }

    public static void sendMessageToAll(final String msg) {
        for (final Player all : server.getOnlinePlayers().values()) {
            all.sendMessage(colorize(msg));
        }
    }

    public static void sendMessageToAdmins(final String msg) {
        for (final Player player : server.getOnlinePlayers().values()) {
            if (player.isOp() || player.hasPermission(Permissions.ADMIN)) {
                player.sendMessage(colorize(msg));
            }
        }
    }
}