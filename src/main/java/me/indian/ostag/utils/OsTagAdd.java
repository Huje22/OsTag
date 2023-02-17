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
                    .replace("<name>", player.getDisplayName())
                    .replace("<suffix>", getLuckPermSufix(player))
                    .replace("<prefix>", getLuckPermPrefix(player))
                    .replace("<groupDisName>", getLuckPermGroupDisName(player))
                    .replace("<xp>", getXp(player))
                    .replace("<unique-description>", getPlayerUnique(player))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            player.setScoreTag(subTag
                    .replace("<device>", getDevice(player))
                    .replace("<controler>", getControler(player))
                    .replace("<health>", player.getHealth() + "")
                    .replace("<max_health>", player.getMaxHealth() + "")
                    .replace("<model>", player.getLoginChainData().getDeviceModel() + "")
                    .replace("<food>", player.getFoodData().getLevel() / 2 + "")
                    .replace("<max_food>", player.getFoodData().getMaxLevel() / 2 + "")
                    .replace("<version>", player.getLoginChainData().getGameVersion())
                    .replace("<xuid>", getXuid(player))
                    .replace("<language>", player.getLoginChainData().getLanguageCode())
                    .replace("<ping>", getPing(player))
                    .replace("<suffix>", getLuckPermSufix(player))
                    .replace("<preffix>", getLuckPermPrefix(player))
                    .replace("<xp>", getXp(player))
                    .replace("<cps>", String.valueOf(CpsListener.getCPS(player)))
                    .replace("<gamemode>", getGameMode(player))
                    .replace("<unique-description>", getPlayerUnique(player))
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
                    .replace("<suffix>", getLuckPermSufix(player))
                    .replace("<prefix>", getLuckPermPrefix(player))
                    .replace("<name>", player.getDisplayName())
                    .replace("<groupDisName>", getLuckPermGroupDisName(player))
                    .replace("<xp>", getXp(player))
                    .replace("<unique-description>", getPlayerUnique(player))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            player.setScoreTag(aSubTag
                    .replace("<device>", getDevice(player))
                    .replace("<controler>", getControler(player))
                    .replace("<health>", player.getHealth() + "")
                    .replace("<max_health>", player.getMaxHealth() + "")
                    .replace("<food>", player.getFoodData().getLevel() / 2 + "")
                    .replace("<max_food>", player.getFoodData().getMaxLevel() / 2 + "")
                    .replace("<model>", player.getLoginChainData().getDeviceModel() + "")
                    .replace("<version>", player.getLoginChainData().getGameVersion())
                    .replace("<xuid>", getXuid(player))
                    .replace("<language>", player.getLoginChainData().getLanguageCode())
                    .replace("<ping>", getPing(player))
                    .replace("<suffix>", getLuckPermSufix(player))
                    .replace("<preffix>", getLuckPermPrefix(player))
                    .replace("<xp>", getXp(player))
                    .replace("<cps>", String.valueOf(CpsListener.getCPS(player)))
                    .replace("<gamemode>", getGameMode(player))
                    .replace("<unique-description>", getPlayerUnique(player))
            );
        }
    }
}