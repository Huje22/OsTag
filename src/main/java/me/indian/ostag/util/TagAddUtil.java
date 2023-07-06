package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;

public class TagAddUtil {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static final PlaceholderAPI api = plugin.getPlaceholderApi();

    public static void addDevNormal(final Player player) {
        String subTag = MessageUtil.colorize(MessageUtil.listToSpacedString(config.getStringList("subtag")));
        String nick = MessageUtil.colorize(config.getString("nick"));

        if (plugin.papiAndKotlinLib) {
            subTag = api.translateString(MessageUtil.colorize(subTag), player);
            nick = api.translateString(MessageUtil.colorize(nick), player);
        }

        if (plugin.nametag) {
            player.setNameTag(PlayerInfoUtil.replaceAllInfo(player, nick));
        }
        if (plugin.scoreTag) {
            player.setScoreTag(PlayerInfoUtil.replaceAllInfo(player, subTag));
        }
    }

    public static void addDevAdvanced(final Player player) {
        String aSubTag = MessageUtil.colorize(MessageUtil.listToSpacedString(config.getStringList("a-subtag")));
        String aNick = MessageUtil.colorize(config.getString("a-nick"));

        if (plugin.papiAndKotlinLib) {
            aSubTag = api.translateString(aSubTag, player);
            aNick = api.translateString(MessageUtil.colorize(aNick), player);
        }

        if (plugin.nametag) {
            player.setNameTag(PlayerInfoUtil.replaceAllInfo(player, aNick));
        }
        if (plugin.scoreTag) {
            player.setScoreTag(PlayerInfoUtil.replaceAllInfo(player, aSubTag));
        }
    }

    public static void resetTag(final Player player) {
        plugin.getLogger().info(MessageUtil.colorize("&aResting tag for&6 " + player.getName() + "&a.."));
        if (plugin.nametag) {
            player.setNameTag(PlayerInfoUtil.replaceAllInfo(player, player.getDisplayName()));
            plugin.getLogger().info(MessageUtil.colorize("&aSeted &bNameTag&a to default for&6 " + player.getName() + "&a.."));
        }
        if (plugin.scoreTag) {
            player.setScoreTag(PlayerInfoUtil.replaceAllInfo(player, ""));
            plugin.getLogger().info(MessageUtil.colorize("&aSeted &bScoreTag&a to none for&6 " + player.getName() + "&a.."));
        }
    }
}