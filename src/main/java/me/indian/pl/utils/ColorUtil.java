package me.indian.pl.utils;

import cn.nukkit.utils.TextFormat;

public class ColorUtil {

    public static String replaceColorCode(String msg) {
        String repl = TextFormat.colorize('&' , msg);
        return repl;
    }
    //I don't know nukkit has TextFormat.colorize(); and I made my own method, then I found out and replaced it with this one without removing the class because it would be hard to change ChatColor everywhere

}
