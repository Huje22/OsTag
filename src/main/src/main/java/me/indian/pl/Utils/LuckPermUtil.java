package me.indian.pl.Utils;

import cn.nukkit.Player;
import me.indian.pl.OsTag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.checkerframework.checker.nullness.qual.Nullable;


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
