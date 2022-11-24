package me.indian.pl.Utils;

import me.indian.pl.OsTag;

public class OtherUtils {


    public static String getOsTagStatus(OsTag plugin){
        Boolean ostag = plugin.getConfig().getBoolean("OsTag");
        Boolean chatFormater = plugin.getConfig().getBoolean("ChatFormater");

        String fal = "§cfalse";
        String tru = "§atrue";
        String os = "";
        String forma = "";
        if(ostag == true){os = tru;}else{os = fal;}
        return os;
    }
    public static String getFormaterStatus(OsTag plugin){
        Boolean chatFormater = plugin.getConfig().getBoolean("ChatFormater");
        String fal = "§cfalse";
        String tru = "§atrue";
        String forma = "";
        if(chatFormater == true){forma = tru;}else{forma = fal;}
        return forma;
    }

}
