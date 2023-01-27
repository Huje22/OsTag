package me.indian.ostag.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;
import me.indian.ostag.listeners.CpsListener;

import static me.indian.ostag.utils.PlayerInfoUtil.*;

public class OsTagAdd {

    private static final OsTag plugin = OsTag.getInstance();
    private static String subTag = ColorUtil.replaceColorCode(plugin.getConfig().getString("subtag"));
    private static String nick = ColorUtil.replaceColorCode(plugin.getConfig().getString("nick"));
    private static String aSubTag = ColorUtil.replaceColorCode(plugin.getConfig().getString("a-subtag"));
    private static String aNick = ColorUtil.replaceColorCode(plugin.getConfig().getString("a-nick"));

    public static void addDevNormal(Player p) {
        Config conf = plugin.getConfig();
        if (OsTag.papKot) {
            PlaceholderAPI api = PlaceholderAPI.getInstance();
            subTag = api.translateString(ColorUtil.replaceColorCode(conf.getString("subtag")), p);
            nick = api.translateString(ColorUtil.replaceColorCode(conf.getString("nick")), p);
        }
        if (conf.getBoolean("NameTag")) {
            p.setNameTag(nick
                    .replace("<name>", p.getDisplayName())
                    .replace("<suffix>", getLuckPermSufix(p))
                    .replace("<prefix>", getLuckPermPrefix(p))
                    .replace("<groupDisName>", getLuckPermGroupDisName(p))
                    .replace("<xp>", getXp(p))
                    .replace("<unique-description>", getPlayerUnique(p))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            p.setScoreTag(subTag
                    .replace("<device>", getDevice(p))
                    .replace("<controler>", getControler(p))
                    .replace("<health>", p.getHealth() + "")
                    .replace("<max_health>", p.getMaxHealth() + "")
                    .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                    .replace("<food>" ,p.getFoodData().getLevel() / 2 + "")
                    .replace("<max_food>" ,p.getFoodData().getMaxLevel() / 2 + "")
                    .replace("<version>", p.getLoginChainData().getGameVersion())
                    .replace("<xuid>", getXuid(p))
                    .replace("<language>", p.getLoginChainData().getLanguageCode())
                    .replace("<ping>", getPing(p))
                    .replace("<suffix>", getLuckPermSufix(p))
                    .replace("<preffix>", getLuckPermPrefix(p))
                    .replace("<xp>", getXp(p))
                    .replace("<cps>", String.valueOf(CpsListener.getCPS(p)))
                    .replace("<gamemode>", getGameMode(p))
                    .replace("<unique-description>", getPlayerUnique(p))
            );
        }
    }

    public static void addDevAdvanced(Player p) {
        Config conf = plugin.getConfig();
        if (OsTag.papKot) {
            PlaceholderAPI api = PlaceholderAPI.getInstance();
            aSubTag = api.translateString(ColorUtil.replaceColorCode(conf.getString("a-subtag")), p);
            aNick = api.translateString(ColorUtil.replaceColorCode(conf.getString("a-nick")), p);
        }

        if (conf.getBoolean("NameTag")) {
            p.setNameTag(aNick
                    .replace("<suffix>", getLuckPermSufix(p))
                    .replace("<prefix>", getLuckPermPrefix(p))
                    .replace("<name>", p.getDisplayName())
                    .replace("<groupDisName>", getLuckPermGroupDisName(p))
                    .replace("<xp>", getXp(p))
                    .replace("<unique-description>", getPlayerUnique(p))
            );
        }
        if (conf.getBoolean("ScoreTag")) {
            p.setScoreTag(aSubTag
                    .replace("<device>", getDevice(p))
                    .replace("<controler>", getControler(p))
                    .replace("<health>", p.getHealth() + "")
                    .replace("<max_health>", p.getMaxHealth() + "")
                    .replace("<food>" ,p.getFoodData().getLevel() / 2 + "")
                    .replace("<max_food>" ,p.getFoodData().getMaxLevel() / 2 + "")
                    .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                    .replace("<version>", p.getLoginChainData().getGameVersion())
                    .replace("<xuid>", getXuid(p))
                    .replace("<language>", p.getLoginChainData().getLanguageCode())
                    .replace("<ping>", getPing(p))
                    .replace("<suffix>", getLuckPermSufix(p))
                    .replace("<preffix>", getLuckPermPrefix(p))
                    .replace("<xp>", getXp(p))
                    .replace("<cps>", String.valueOf(CpsListener.getCPS(p)))
                    .replace("<gamemode>", getGameMode(p))
                    .replace("<unique-description>", getPlayerUnique(p))
            );
        }
    }
}