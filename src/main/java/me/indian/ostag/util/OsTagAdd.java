package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;

public class OsTagAdd {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static final PlaceholderAPI api = plugin.getPlaceholderApi();
    private static String subTag = ColorUtil.replaceColorCode(config.getString("subtag"));
    private static String nick = ColorUtil.replaceColorCode(config.getString("nick"));
    private static String aSubTag = ColorUtil.replaceColorCode(config.getString("a-subtag"));
    private static String aNick = ColorUtil.replaceColorCode(config.getString("a-nick"));

    public static void addDevNormal(Player player) {
        if (plugin.papiAndKotlinLib) {
            subTag = api.translateString(ColorUtil.replaceColorCode(config.getString("subtag")), player);
            nick = api.translateString(ColorUtil.replaceColorCode(config.getString("nick")), player);
        }
        if (config.getBoolean("NameTag")) {
            player.setNameTag(ReplaceUtil.replace(player, nick));
        }
        if (config.getBoolean("ScoreTag")) {
            player.setScoreTag(ReplaceUtil.replace(player, subTag));
        }
    }

    public static void addDevAdvanced(Player player) {
        if (plugin.papiAndKotlinLib) {
            aSubTag = api.translateString(ColorUtil.replaceColorCode(config.getString("a-subtag")), player);
            aNick = api.translateString(ColorUtil.replaceColorCode(config.getString("a-nick")), player);
        }
        if (config.getBoolean("NameTag")) {
            player.setNameTag(ReplaceUtil.replace(player, aNick));
        }
        if (config.getBoolean("ScoreTag")) {
            player.setScoreTag(ReplaceUtil.replace(player, aSubTag));
        }
    }
}