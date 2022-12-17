package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.pl.OsTag;
import me.indian.pl.listeners.InputListener;

import static me.indian.pl.utils.PlayerInfoUtil.*;

public class OsTagAdd {

    private static OsTag plugin = OsTag.getInstance();

    private static String subTag = ChatColor.replaceColorCode(plugin.getConfig().getString("subtag"));

    private static String nick = ChatColor.replaceColorCode(plugin.getConfig().getString("nick"));

    private static String aSubTag = ChatColor.replaceColorCode(plugin.getConfig().getString("a-subtag"));

    private static String aNick = ChatColor.replaceColorCode(plugin.getConfig().getString("a-nick"));

    public static void addDevNormal(Player p) {

        Config conf = plugin.getConfig();

        if(plugin.papKot){
            PlaceholderAPI api = PlaceholderAPI.getInstance();
            subTag = api.translateString(ChatColor.replaceColorCode(conf.getString("subtag")), p);
            nick = api.translateString(ChatColor.replaceColorCode(conf.getString("nick")),p);
        }

        p.setNameTag(nick
                .replace("<name>", p.getDisplayName())
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<prefix>", getLuckPermPrefix(p))
                .replace("<groupDisName>", getGroupDisName(p))
                .replace("<deathskull>", getSkulll(p))
                .replace("<xp>", getXp(p))
                .replace("<unique-description>", getPlayerUnique(p))
                + "\n" + subTag
                .replace("<device>", getDevice(p))
                .replace("<deathskull>", getSkulll(p))
                .replace("§7", "§7")
                .replace("<controler>", getControler(p))
                .replace("<health>", p.getHealth() + "")
                .replace("<max_health>", p.getMaxHealth() + "")
                .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                .replace("<version>", p.getLoginChainData().getGameVersion())
                .replace("<xuid>", getXuid(p))
                .replace("<language>", p.getLoginChainData().getLanguageCode())
                .replace("<ping>", getPing(p))
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<preffix>", getLuckPermPrefix(p))
                .replace("<xp>", getXp(p))
                .replace("<cps>", String.valueOf(InputListener.getCPS(p)))
                .replace("<gamemode>", getGameMode(p))
                .replace("<unique-description>", getPlayerUnique(p))
        );
    }

    public static void addDevAdvanced(Player p) {

        Config conf = plugin.getConfig();

        if(plugin.papKot){
            PlaceholderAPI api = PlaceholderAPI.getInstance();
            aSubTag = api.translateString(ChatColor.replaceColorCode(conf.getString("a-subtag")), p);
            aNick = api.translateString(ChatColor.replaceColorCode(conf.getString("a-nick")),p);
        }

        p.setNameTag(aNick
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<prefix>", getLuckPermPrefix(p))
                .replace("<name>", p.getDisplayName())
                .replace("<groupDisName>", getGroupDisName(p))
                .replace("<deathskull>", getSkulll(p))
                .replace("<xp>", getXp(p))
                .replace("<unique-description>", getPlayerUnique(p))
                + "\n" + aSubTag
                .replace("<deathskull>", getSkulll(p))
                .replace("<device>", getDevice(p))
                .replace("§7", "§7")
                .replace("<controler>", getControler(p))
                .replace("<health>", p.getHealth() + "")
                .replace("<max_health>", p.getMaxHealth() + "")
                .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                .replace("<version>", p.getLoginChainData().getGameVersion())
                .replace("<xuid>", getXuid(p))
                .replace("<language>", p.getLoginChainData().getLanguageCode())
                .replace("<ping>", getPing(p))
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<preffix>", getLuckPermPrefix(p))
                .replace("<xp>", getXp(p))
                .replace("<cps>", String.valueOf(InputListener.getCPS(p)))
                .replace("<gamemode>", getGameMode(p))
                .replace("<unique-description>", getPlayerUnique(p))
        );
    }

    public static void scoreTagNormal(Player p) {

        Config conf = plugin.getConfig();

        if(plugin.papKot){
            PlaceholderAPI api = PlaceholderAPI.getInstance();
            subTag = api.translateString(ChatColor.replaceColorCode(conf.getString("subtag")), p);
        }

        p.setScoreTag(subTag
                .replace("<device>", getDevice(p))
                .replace("<deathskull>", getSkulll(p))
                .replace("§7", "§7")
                .replace("<controler>", getControler(p))
                .replace("<health>", p.getHealth() + "")
                .replace("<max_health>", p.getMaxHealth() + "")
                .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                .replace("<version>", p.getLoginChainData().getGameVersion())
                .replace("<xuid>", getXuid(p))
                .replace("<language>", p.getLoginChainData().getLanguageCode())
                .replace("<ping>", getPing(p))
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<preffix>", getLuckPermPrefix(p))
                .replace("<xp>", getXp(p))
                .replace("<cps>", String.valueOf(InputListener.getCPS(p)))
                .replace("<gamemode>", getGameMode(p))
                .replace("<unique-description>", getPlayerUnique(p))
        );
    }

    public static void scoreTagAdvancd(Player p) {

        Config conf = plugin.getConfig();
        if(plugin.papKot){
            PlaceholderAPI api = PlaceholderAPI.getInstance();
            aSubTag = api.translateString(ChatColor.replaceColorCode(conf.getString("a-subtag")), p);
        }

        p.setScoreTag(aSubTag
                .replace("<deathskull>", getSkulll(p))
                .replace("<device>", getDevice(p))
                .replace("§7", "§7")
                .replace("<controler>", getControler(p))
                .replace("<health>", p.getHealth() + "")
                .replace("<max_health>", p.getMaxHealth() + "")
                .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                .replace("<version>", p.getLoginChainData().getGameVersion())
                .replace("<xuid>", getXuid(p))
                .replace("<language>", p.getLoginChainData().getLanguageCode())
                .replace("<ping>", getPing(p))
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<preffix>", getLuckPermPrefix(p))
                .replace("<xp>", getXp(p))
                .replace("<cps>", String.valueOf(InputListener.getCPS(p)))
                .replace("<gamemode>", getGameMode(p))
                .replace("<unique-description>", getPlayerUnique(p))
        );
    }
}
