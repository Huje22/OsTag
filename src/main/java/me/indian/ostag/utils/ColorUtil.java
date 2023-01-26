package me.indian.ostag.utils;

import cn.nukkit.utils.TextFormat;

public class ColorUtil {

    public static String replaceColorCode(String msg) {
        return TextFormat.colorize('&', msg);
    }
}