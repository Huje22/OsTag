package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.pl.listeners.InputListener;
import me.indian.pl.OsTag;

import static me.indian.pl.utils.PlayerInfoUtil.*;

public class OsTagAdd {
    private final OsTag plugin;

    public OsTagAdd(OsTag plugin) {
        this.plugin = plugin;
    }


    public static void addDevNormal(Player p, OsTag plugin) {

        Config conf = plugin.getConfig();

        p.setNameTag(ChatColor.replaceColorCode(conf.getString("nick"))
                .replace("<name>", p.getDisplayName())
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<prefix>", getLuckPermPrefix(p))
                .replace("<groupDisName>", getGroupDisName(p))
                .replace("<deathskull>", getSkulll(p))
                .replace("<xp>", getXp(p))
                .replace("<unique-description>", getPlayerUnique(p))
                + "\n" + ChatColor.replaceColorCode(conf.getString("subtag"))
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

    public static void addDevAdvanced(Player p, OsTag plugin) {

        Config conf = plugin.getConfig();

        p.setNameTag(ChatColor.replaceColorCode(conf.getString("a-nick"))
                .replace("<suffix>", getLuckPermSufix(p))
                .replace("<prefix>", getLuckPermPrefix(p))
                .replace("<name>", p.getDisplayName())
                .replace("<groupDisName>", getGroupDisName(p))
                .replace("<deathskull>", getSkulll(p))
                .replace("<xp>", getXp(p))
                .replace("<unique-description>", getPlayerUnique(p))
                + "\n" + ChatColor.replaceColorCode(conf.getString("a-subtag"))
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

    public static void scoreTagNormal(Player p, OsTag plugin) {

        Config conf = plugin.getConfig();

        p.setScoreTag(ChatColor.replaceColorCode(conf.getString("subtag"))
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

    public static void scoreTagAdvancd(Player p, OsTag plugin) {

        Config conf = plugin.getConfig();


        p.setScoreTag(ChatColor.replaceColorCode(conf.getString("a-subtag"))
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
