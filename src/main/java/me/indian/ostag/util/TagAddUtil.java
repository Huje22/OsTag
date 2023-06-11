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
        String subTag = TextUtil.colorize(TextUtil.listToString(config.getStringList("subtag")));
        String nick = TextUtil.colorize(config.getString("nick"));

        if (plugin.papiAndKotlinLib) {
            subTag = api.translateString(TextUtil.colorize(subTag), player);
            nick = api.translateString(TextUtil.colorize(nick), player);
        }

        if (plugin.nametag) {
            player.setNameTag(PlayerInfoUtil.replaceAllInfo(player, nick));
        }
        if (plugin.scoreTag) {
            player.setScoreTag(PlayerInfoUtil.replaceAllInfo(player, subTag));
        }
    }

    public static void addDevAdvanced(final Player player) {
        String aSubTag = TextUtil.colorize(TextUtil.listToString(config.getStringList("a-subtag")));
        String aNick = TextUtil.colorize(config.getString("a-nick"));

        if (plugin.papiAndKotlinLib) {
            aSubTag = api.translateString(aSubTag, player);
            aNick = api.translateString(TextUtil.colorize(aNick), player);
        }

        if (plugin.nametag) {
            player.setNameTag(PlayerInfoUtil.replaceAllInfo(player, aNick));
        }
        if (plugin.scoreTag) {
            player.setScoreTag(PlayerInfoUtil.replaceAllInfo(player, aSubTag));
        }
    }
}