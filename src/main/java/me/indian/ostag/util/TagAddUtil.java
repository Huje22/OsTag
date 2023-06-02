package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;

public class TagAddUtil {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static String subTag = ColorUtil.replaceColorCode(config.getString("subtag"));
    private static String nick = ColorUtil.replaceColorCode(config.getString("nick"));
    private static String aSubTag = ColorUtil.replaceColorCode(config.getString("a-subtag"));
    private static String aNick = ColorUtil.replaceColorCode(config.getString("a-nick"));
    private static final PlaceholderAPI api = plugin.getPlaceholderApi();

    public static void addDevNormal(final Player player) {
        if (plugin.papiAndKotlinLib) {
            subTag = api.translateString(ColorUtil.replaceColorCode(config.getString("subtag")), player);
            nick = api.translateString(ColorUtil.replaceColorCode(config.getString("nick")), player);
        }
        if (plugin.nametag) {
            player.setNameTag(PlayerInfoUtil.replaceAllInfo(player, nick));
        }
        if (plugin.scoreTag) {
            player.setScoreTag(PlayerInfoUtil.replaceAllInfo(player, subTag));
        }
    }

    public static void addDevAdvanced(final Player player) {
        if (plugin.papiAndKotlinLib) {
            aSubTag = api.translateString(ColorUtil.replaceColorCode(config.getString("a-subtag")), player);
            aNick = api.translateString(ColorUtil.replaceColorCode(config.getString("a-nick")), player);
        }
        if (plugin.nametag) {
            player.setNameTag(PlayerInfoUtil.replaceAllInfo(player, aNick));
        }
        if (plugin.scoreTag) {
            player.setScoreTag(PlayerInfoUtil.replaceAllInfo(player, aSubTag));
        }
    }
}