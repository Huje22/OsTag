package me.indian.pl.utils;

import net.luckperms.api.model.user.User;


public class LuckPermUtil {


    public static String getPrefix(User user) {
        String prefix = "";
        if (user.getCachedData().getMetaData().getPrefix() != null) {
            prefix = user.getCachedData().getMetaData().getPrefix();
        }
        return prefix;
    }

    public static String getSuffix(User user) {
        String suffix = "";
        if (user.getCachedData().getMetaData().getSuffix() != null) {
            suffix = user.getCachedData().getMetaData().getSuffix();
        }
        return suffix;
    }
}
