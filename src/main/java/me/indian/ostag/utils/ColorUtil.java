package me.indian.ostag.utils;

import cn.nukkit.utils.TextFormat;

public class ColorUtil {

    public static String replaceColorCode(String msg) {
        return TextFormat.colorize('&', msg);
    }
    //I don't know nukkit has TextFormat.colorize(); and I made my own method, then I found out and replaced it with this one without removing the class because it would be hard to change ChatColor everywhere

}
