package me.indian.ostag.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;
import me.indian.ostag.listeners.CpsListener;

import static me.indian.ostag.utils.PlayerInfoUtil.*;

public class OsTagAdd {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config conf = plugin.getConfig();
    private static final PlaceholderAPI api = plugin.getPlaceholderApi();
    private static String subTag = ColorUtil.replaceColorCode(plugin.getConfig().getString("subtag"));
    private static String nick = ColorUtil.replaceColorCode(plugin.getConfig().getString("nick"));
    private static String aSubTag = ColorUtil.replaceColorCode(plugin.getConfig().getString("a-subtag"));
    private static String aNick = ColorUtil.replaceColorCode(plugin.getConfig().getString("a-nick"));

    public static void addDevNormal(Player player) {
        if (OsTag.papKot) {
            subTag = api.translateString(ColorUtil.replaceColorCode(conf.getString("subtag")), player);
            nick = api.translateString(ColorUtil.replaceColorCode(conf.getString("nick")), player);
        }
        if (conf.getBoolean("NameTag")) {
            player.setNameTag(nick
                    .replace(Prefixes.NAME, player.getDisplayName())
                    .replace(Prefixes.SUFFIX, getLuckPermSuffix(player))
                    .replace(Prefixes.PREFFIX, getLuckPermPreffix(player))
                    .replace(Prefixes.GROUPDISPLAYNAME, getLuckPermGroupDisName(player))
                    .replace(Prefixes.XP, getXp(player))
                    .replace(Prefixes.UNIQUE_DESCRIPTION, getUnique(player))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            player.setScoreTag(subTag
                    .replace(Prefixes.DEVICE, getDevice(player))
                    .replace(Prefixes.CONTROLLER, getController(player))
                    .replace(Prefixes.HEALTH, getHealth(player))
                    .replace(Prefixes.MAX_HEALTH, player.getMaxHealth() + "")
                    .replace(Prefixes.MODEL, player.getLoginChainData().getDeviceModel() + "")
                    .replace(Prefixes.FOOD, player.getFoodData().getLevel() / 2 + "")
                    .replace(Prefixes.MAX_FOOD, player.getFoodData().getMaxLevel() / 2 + "")
                    .replace(Prefixes.VERSION, player.getLoginChainData().getGameVersion())
                    .replace(Prefixes.XUID, getXuid(player))
                    .replace(Prefixes.LANGUAGE, player.getLoginChainData().getLanguageCode())
                    .replace(Prefixes.PING, getPing(player))
                    .replace(Prefixes.SUFFIX, getLuckPermSuffix(player))
                    .replace(Prefixes.PREFFIX, getLuckPermPreffix(player))
                    .replace(Prefixes.XP, getXp(player))
                    .replace(Prefixes.CPS, String.valueOf(CpsListener.getCPS(player)))
                    .replace(Prefixes.GAMEMODE, getGameMode(player))
                    .replace(Prefixes.UNIQUE_DESCRIPTION, getUnique(player))
            );
        }
    }

    public static void addDevAdvanced(Player player) {
        if (OsTag.papKot) {
            aSubTag = api.translateString(ColorUtil.replaceColorCode(conf.getString("a-subtag")), player);
            aNick = api.translateString(ColorUtil.replaceColorCode(conf.getString("a-nick")), player);
        }

        if (conf.getBoolean("NameTag")) {
            player.setNameTag(aNick
                    .replace(Prefixes.SUFFIX, getLuckPermSuffix(player))
                    .replace(Prefixes.PREFFIX, getLuckPermPreffix(player))
                    .replace(Prefixes.NAME, player.getDisplayName())
                    .replace(Prefixes.GROUPDISPLAYNAME, getLuckPermGroupDisName(player))
                    .replace(Prefixes.XP, getXp(player))
                    .replace(Prefixes.UNIQUE_DESCRIPTION, getUnique(player))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            player.setScoreTag(aSubTag
                    .replace(Prefixes.DEVICE, getDevice(player))
                    .replace(Prefixes.CONTROLLER, getController(player))
                    .replace(Prefixes.HEALTH, getHealth(player))
                    .replace(Prefixes.MAX_HEALTH, player.getMaxHealth() + "")
                    .replace(Prefixes.FOOD, player.getFoodData().getLevel() / 2 + "")
                    .replace(Prefixes.MAX_FOOD, player.getFoodData().getMaxLevel() / 2 + "")
                    .replace(Prefixes.MODEL, player.getLoginChainData().getDeviceModel() + "")
                    .replace(Prefixes.VERSION, player.getLoginChainData().getGameVersion())
                    .replace(Prefixes.XUID, getXuid(player))
                    .replace(Prefixes.LANGUAGE, player.getLoginChainData().getLanguageCode())
                    .replace(Prefixes.PING, getPing(player))
                    .replace(Prefixes.SUFFIX, getLuckPermSuffix(player))
                    .replace(Prefixes.PREFFIX, getLuckPermPreffix(player))
                    .replace(Prefixes.XP, getXp(player))
                    .replace(Prefixes.CPS, String.valueOf(CpsListener.getCPS(player)))
                    .replace(Prefixes.GAMEMODE, getGameMode(player))
                    .replace(Prefixes.UNIQUE_DESCRIPTION, getUnique(player))
            );
        }
    }
}