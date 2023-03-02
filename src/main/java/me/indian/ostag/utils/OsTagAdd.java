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
    private static String subTag = ColorUtil.replaceColorCode(plugin.getConfig().getString("subtag"));
    private static String nick = ColorUtil.replaceColorCode(plugin.getConfig().getString("nick"));
    private static String aSubTag = ColorUtil.replaceColorCode(plugin.getConfig().getString("a-subtag"));
    private static String aNick = ColorUtil.replaceColorCode(plugin.getConfig().getString("a-nick"));

    public static void addDevNormal(Player player) {
        if (OsTag.papKot) {
            PlaceholderAPI api = PlaceholderAPI.getInstance();
            subTag = api.translateString(ColorUtil.replaceColorCode(conf.getString("subtag")), player);
            nick = api.translateString(ColorUtil.replaceColorCode(conf.getString("nick")), player);
        }
        if (conf.getBoolean("NameTag")) {
            player.setNameTag(nick
                    .replace(PrefixesUtil.NAME, player.getDisplayName())
                    .replace(PrefixesUtil.SUFFIX, getLuckPermSuffix(player))
                    .replace(PrefixesUtil.PREFFIX, getLuckPermPreffix(player))
                    .replace(PrefixesUtil.GROUPDISPLAYNAME, getLuckPermGroupDisName(player))
                    .replace(PrefixesUtil.XP, getXp(player))
                    .replace(PrefixesUtil.UNIQUE_DESCRIPTION, getPlayerUnique(player))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            player.setScoreTag(subTag
                    .replace(PrefixesUtil.DEVICE, getDevice(player))
                    .replace(PrefixesUtil.CONTROLLER, getController(player))
                    .replace(PrefixesUtil.HEALTH, player.getHealth() + "")
                    .replace(PrefixesUtil.MAX_HEALTH, player.getMaxHealth() + "")
                    .replace(PrefixesUtil.MODEL, player.getLoginChainData().getDeviceModel() + "")
                    .replace(PrefixesUtil.FOOD, player.getFoodData().getLevel() / 2 + "")
                    .replace(PrefixesUtil.MAX_FOOD, player.getFoodData().getMaxLevel() / 2 + "")
                    .replace(PrefixesUtil.VERSION, player.getLoginChainData().getGameVersion())
                    .replace(PrefixesUtil.XUID, getXuid(player))
                    .replace(PrefixesUtil.LANGUAGE, player.getLoginChainData().getLanguageCode())
                    .replace(PrefixesUtil.PING, getPing(player))
                    .replace(PrefixesUtil.SUFFIX, getLuckPermSuffix(player))
                    .replace(PrefixesUtil.PREFFIX, getLuckPermPreffix(player))
                    .replace(PrefixesUtil.XP, getXp(player))
                    .replace(PrefixesUtil.CPS, String.valueOf(CpsListener.getCPS(player)))
                    .replace(PrefixesUtil.GAMEMODE, getGameMode(player))
                    .replace(PrefixesUtil.UNIQUE_DESCRIPTION, getPlayerUnique(player))
            );
        }
    }

    public static void addDevAdvanced(Player player) {
        if (OsTag.papKot) {
            final PlaceholderAPI api = PlaceholderAPI.getInstance();
            aSubTag = api.translateString(ColorUtil.replaceColorCode(conf.getString("a-subtag")), player);
            aNick = api.translateString(ColorUtil.replaceColorCode(conf.getString("a-nick")), player);
        }

        if (conf.getBoolean("NameTag")) {
            player.setNameTag(aNick
                    .replace(PrefixesUtil.SUFFIX, getLuckPermSuffix(player))
                    .replace(PrefixesUtil.PREFFIX, getLuckPermPreffix(player))
                    .replace(PrefixesUtil.NAME, player.getDisplayName())
                    .replace(PrefixesUtil.GROUPDISPLAYNAME, getLuckPermGroupDisName(player))
                    .replace(PrefixesUtil.XP, getXp(player))
                    .replace(PrefixesUtil.UNIQUE_DESCRIPTION, getPlayerUnique(player))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            player.setScoreTag(aSubTag
                    .replace(PrefixesUtil.DEVICE, getDevice(player))
                    .replace(PrefixesUtil.CONTROLLER, getController(player))
                    .replace(PrefixesUtil.HEALTH, player.getHealth() + "")
                    .replace(PrefixesUtil.MAX_HEALTH, player.getMaxHealth() + "")
                    .replace(PrefixesUtil.FOOD, player.getFoodData().getLevel() / 2 + "")
                    .replace(PrefixesUtil.MAX_FOOD, player.getFoodData().getMaxLevel() / 2 + "")
                    .replace(PrefixesUtil.MODEL, player.getLoginChainData().getDeviceModel() + "")
                    .replace(PrefixesUtil.VERSION, player.getLoginChainData().getGameVersion())
                    .replace(PrefixesUtil.XUID, getXuid(player))
                    .replace(PrefixesUtil.LANGUAGE, player.getLoginChainData().getLanguageCode())
                    .replace(PrefixesUtil.PING, getPing(player))
                    .replace(PrefixesUtil.SUFFIX, getLuckPermSuffix(player))
                    .replace(PrefixesUtil.PREFFIX, getLuckPermPreffix(player))
                    .replace(PrefixesUtil.XP, getXp(player))
                    .replace(PrefixesUtil.CPS, String.valueOf(CpsListener.getCPS(player)))
                    .replace(PrefixesUtil.GAMEMODE, getGameMode(player))
                    .replace(PrefixesUtil.UNIQUE_DESCRIPTION, getPlayerUnique(player))
            );
        }
    }
}