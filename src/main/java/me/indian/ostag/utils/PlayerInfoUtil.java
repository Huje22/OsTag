package me.indian.ostag.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.listeners.InputListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;


public class PlayerInfoUtil {

    private static final OsTag plugin = OsTag.getInstance();

    public static String getDevice(Player p) {
        Config conf = plugin.getConfig();
        String windows = ColorUtil.replaceColorCode(conf.getString("Windows"));
        String android = ColorUtil.replaceColorCode(conf.getString("Android"));
        String ios = ColorUtil.replaceColorCode(conf.getString("Ios"));
        String mac = ColorUtil.replaceColorCode(conf.getString("Mac"));
        String fire = ColorUtil.replaceColorCode(conf.getString("Fire"));
        String hololens = ColorUtil.replaceColorCode(conf.getString("Hololens"));
        String dedicated = ColorUtil.replaceColorCode(conf.getString("Hedicated"));
        String tvos = ColorUtil.replaceColorCode(conf.getString("TvOs"));
        String playstation = ColorUtil.replaceColorCode(conf.getString("PlayStation"));
        String nintendo = ColorUtil.replaceColorCode(conf.getString("Nintendo"));
        String xbox = ColorUtil.replaceColorCode(conf.getString("Xbox"));
        String linux = ColorUtil.replaceColorCode(conf.getString("Linux"));
        String unknow = ColorUtil.replaceColorCode(conf.getString("Unknow"));

        switch (p.getLoginChainData().getDeviceOS()) {
            case 1:
                return android;
            case 2:
                return ios;
            case 3:
                return mac;
            case 4:
                return fire;
            case 5:
                return "gearvr not supported as of 2020";
            case 6:
                return hololens;
            case 7:
            case 8:
                return windows;
            case 9:
                return dedicated;
            case 10:
                return tvos;
            case 11:
                return playstation;
            case 12:
                return nintendo;
            case 13:
                return xbox;
            case 14:
                return "winphone not supported as of 2020";
            case 15:
                return linux;
            default:
                return unknow;
        }
    }

    public static String getControler(Player p) {
        Config conf = plugin.getConfig();

        String motion_controller = ColorUtil.replaceColorCode(conf.getString("motion_controller"));
        String dotyk = ColorUtil.replaceColorCode(conf.getString("touch"));
        String klawa = ColorUtil.replaceColorCode(conf.getString("keyboard"));
        String pad = ColorUtil.replaceColorCode(conf.getString("gamepad"));
        String unknowcon = ColorUtil.replaceColorCode(conf.getString("UnknowControler"));

        if (OsTag.serverMovement) {
            switch (InputListener.getControler(p)) {
                case "MOUSE":
                    return klawa;
                case "TOUCH":
                    return dotyk;
                case "GAME_PAD":
                    return pad;
                case "MOTION_CONTROLLER":
                    return motion_controller;
                default:
                    return unknowcon;
            }
        } else {
            switch (p.getLoginChainData().getCurrentInputMode()) {
                case 1:
                    return klawa;
                case 2:
                    return dotyk;
                case 3:
                    return pad;
                case 4:
                    return motion_controller;
                default:
                    return unknowcon;
            }
        }
    }

    public static String getXp(Player p) {
        Config conf = plugin.getConfig();
        String xp = "0";
        if (p.getExperienceLevel() == 0) {
            xp = ColorUtil.replaceColorCode(conf.getString("1lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 1) {
            xp = ColorUtil.replaceColorCode(conf.getString("1lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 10) {
            xp = ColorUtil.replaceColorCode(conf.getString("10lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 15) {
            xp = ColorUtil.replaceColorCode(conf.getString("15lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 20) {
            xp = ColorUtil.replaceColorCode(conf.getString("20lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 25) {
            xp = ColorUtil.replaceColorCode(conf.getString("25lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 30) {
            xp = ColorUtil.replaceColorCode(conf.getString("30lvl") + p.getExperienceLevel());
        }

        return xp;
    }

    public static String getGameMode(Player p) {
        Config conf = plugin.getConfig();
        String survival = ColorUtil.replaceColorCode(conf.getString("survival"));
        String creative = ColorUtil.replaceColorCode(conf.getString("creative"));
        String adventure = ColorUtil.replaceColorCode(conf.getString("adventure"));
        String spectator = ColorUtil.replaceColorCode(conf.getString("spectator"));

        switch (p.getGamemode()) {
            case 1:
                return creative;
            case 2:
                return adventure;
            case 3:
                return spectator;
            case 0:
                return survival;
            default:
                return "Unknow";
        }
    }

    public static String getPing(Player p) {

        String ping = ColorUtil.replaceColorCode("&r0");
        Config conf = plugin.getConfig();
        if (p.getPing() >= 1) {
            ping = ColorUtil.replaceColorCode(conf.getString("low-ping")) + p.getPing();
        }
        if (p.getPing() >= 75) {
            ping = ColorUtil.replaceColorCode(conf.getString("medium-ping")) + p.getPing();
        }
        if (p.getPing() >= 100) {
            ping = ColorUtil.replaceColorCode(conf.getString("high-ping")) + p.getPing();
        }
        return ping;
    }

    public static String getLuckPermPrefix(Player p) {

        String pref = "";

        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {

            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);

            if (user.getCachedData().getMetaData().getPrefix() != null) {
                pref = user.getCachedData().getMetaData().getPrefix();
            }
        }
        return ColorUtil.replaceColorCode(pref);
    }

    public static String getLuckPermSufix(Player p) {
        String suf = "";
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            if (user.getCachedData().getMetaData().getSuffix() != null) {
                suf = user.getCachedData().getMetaData().getSuffix();
            }

        }
        return ColorUtil.replaceColorCode(suf);
    }

    public static String getLuckPermGroupDisName(Player p) {
        String group = "";
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            if (luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName() != null) {
                group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName();
            }
        }
        return ColorUtil.replaceColorCode(group);
    }

    public static String getXuid(Player p) {
        Config conf = plugin.getConfig();
        String xuid = ColorUtil.replaceColorCode(conf.getString("guest"));
        if (p.getLoginChainData().getXUID() != null) {
            xuid = p.getLoginChainData().getXUID();
        }
        return xuid;
    }

    public static String getDimension(Player p) {
        Config conf = plugin.getConfig();
        String overworld = ColorUtil.replaceColorCode(conf.getString("overworld"));
        String nether = ColorUtil.replaceColorCode(conf.getString("nether"));
        String end = ColorUtil.replaceColorCode(conf.getString("end"));

        switch (p.getLevel().getDimension()) {
            case 0:
                return overworld;
            case 1:
                return nether;
            case 2:
                return end;
            default:
                return "Unknow";
        }
    }

    public static String getPlayerUnique(Player p) {
        Config conf = plugin.getConfig();
        String unique;
        String name = p.getDisplayName();

        if (!(conf.getString("Players." + name + ".unique-description").isEmpty())) {
            unique = conf.getString("Players." + name + ".unique-description");
        } else {
            unique = conf.getString("Players.default.description");
        }
        return ColorUtil.replaceColorCode(unique);
    }

}