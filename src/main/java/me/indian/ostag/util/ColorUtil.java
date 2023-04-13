package me.indian.ostag.util;

import cn.nukkit.utils.TextFormat;

public class ColorUtil {
    public static String replaceColorCode(String msg) {
        return TextFormat.colorize('&', msg);
    }
}