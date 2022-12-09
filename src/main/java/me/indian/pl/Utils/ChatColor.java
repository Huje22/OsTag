package me.indian.pl.Utils;

public class ChatColor {

    public static String replaceColorCode(String msg){
        String repl = msg.replace("&","§");

        return repl;
    }

}
