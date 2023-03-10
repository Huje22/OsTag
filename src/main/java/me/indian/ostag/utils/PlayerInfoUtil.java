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
    private static final Config conf = plugin.getConfig();
    private static final LuckPerms luckPerms = LuckPermsProvider.get();

    public static String getDevice(Player player) {
        String windows = ColorUtil.replaceColorCode(conf.getString("Windows"));
        String android = ColorUtil.replaceColorCode(conf.getString("Android"));
        String ios = ColorUtil.replaceColorCode(conf.getString("Ios"));
        String mac = ColorUtil.replaceColorCode(conf.getString("Mac"));
        String fire = ColorUtil.replaceColorCode(conf.getString("Fire"));
        String hololens = ColorUtil.replaceColorCode(conf.getString("Hololens"));
        String dedicated = ColorUtil.replaceColorCode(conf.getString("Dedicated"));
        String tvos = ColorUtil.replaceColorCode(conf.getString("TvOs"));
        String playstation = ColorUtil.replaceColorCode(conf.getString("PlayStation"));
        String nintendo = ColorUtil.replaceColorCode(conf.getString("Nintendo"));
        String xbox = ColorUtil.replaceColorCode(conf.getString("Xbox"));
        String linux = ColorUtil.replaceColorCode(conf.getString("Linux"));
        String unknow = ColorUtil.replaceColorCode(conf.getString("Unknown"));

        switch (player.getLoginChainData().getDeviceOS()) {
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

    public static String getController(Player player) {
        String motion_controller = ColorUtil.replaceColorCode(conf.getString("Motion_controller"));
        String touch = ColorUtil.replaceColorCode(conf.getString("Touch"));
        String keyboard = ColorUtil.replaceColorCode(conf.getString("Keyboard"));
        String pad = ColorUtil.replaceColorCode(conf.getString("Gamepad"));
        String unknowcon = ColorUtil.replaceColorCode(conf.getString("UnknowControler"));

        if (OsTag.serverMovement) {
            switch (InputListener.getControler(player)) {
                case "MOUSE":
                    return keyboard;
                case "TOUCH":
                    return touch;
                case "GAME_PAD":
                    return pad;
                case "MOTION_CONTROLLER":
                    return motion_controller;
                default:
                    return unknowcon;
            }
        } else {
            switch (player.getLoginChainData().getCurrentInputMode()) {
                case 1:
                    return keyboard;
                case 2:
                    return touch;
                case 3:
                    return pad;
                case 4:
                    return motion_controller;
                default:
                    return unknowcon;
            }
        }
    }

    public static String getXp(Player player) {
        String xp = "0";

        if (player.getExperienceLevel() == 0) {
            xp = ColorUtil.replaceColorCode(conf.getString("1lvl") + player.getExperienceLevel());
        }
        if (player.getExperienceLevel() >= 1) {
            xp = ColorUtil.replaceColorCode(conf.getString("1lvl") + player.getExperienceLevel());
        }
        if (player.getExperienceLevel() >= 10) {
            xp = ColorUtil.replaceColorCode(conf.getString("10lvl") + player.getExperienceLevel());
        }
        if (player.getExperienceLevel() >= 15) {
            xp = ColorUtil.replaceColorCode(conf.getString("15lvl") + player.getExperienceLevel());
        }
        if (player.getExperienceLevel() >= 20) {
            xp = ColorUtil.replaceColorCode(conf.getString("20lvl") + player.getExperienceLevel());
        }
        if (player.getExperienceLevel() >= 25) {
            xp = ColorUtil.replaceColorCode(conf.getString("25lvl") + player.getExperienceLevel());
        }
        if (player.getExperienceLevel() >= 30) {
            xp = ColorUtil.replaceColorCode(conf.getString("30lvl") + player.getExperienceLevel());
        }

        return xp;
    }

    public static String getGameMode(Player player) {
        String survival = ColorUtil.replaceColorCode(conf.getString("survival"));
        String creative = ColorUtil.replaceColorCode(conf.getString("creative"));
        String adventure = ColorUtil.replaceColorCode(conf.getString("adventure"));
        String spectator = ColorUtil.replaceColorCode(conf.getString("spectator"));

        switch (player.getGamemode()) {
            case 1:
                return creative;
            case 2:
                return adventure;
            case 3:
                return spectator;
            case 0:
                return survival;
            default:
                return "Unknown";
        }
    }

    public static String getPing(Player player) {
        String ping = ColorUtil.replaceColorCode("&r0");

        if (player.getPing() >= 1) {
            ping = ColorUtil.replaceColorCode(conf.getString("low-ping")) + player.getPing();
        }
        if (player.getPing() >= 75) {
            ping = ColorUtil.replaceColorCode(conf.getString("medium-ping")) + player.getPing();
        }
        if (player.getPing() >= 100) {
            ping = ColorUtil.replaceColorCode(conf.getString("high-ping")) + player.getPing();
        }
        return ping;
    }

    public static String getLuckPermPreffix(Player player) {
        String pref = "";

        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            if (user.getCachedData().getMetaData().getPrefix() != null) {
                pref = user.getCachedData().getMetaData().getPrefix();
            }
        }
        return ColorUtil.replaceColorCode(pref);
    }

    public static String getLuckPermSuffix(Player player) {
        String suf = "";

        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            if (user.getCachedData().getMetaData().getSuffix() != null) {
                suf = user.getCachedData().getMetaData().getSuffix();
            }
        }
        return ColorUtil.replaceColorCode(suf);
    }

    @SuppressWarnings("ConstantConditions")
    public static String getLuckPermGroupDisName(Player player) {
        String group = "";

        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            if (luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName() != null) {
                group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName();
            }
        }
        return ColorUtil.replaceColorCode(group);
    }

    public static String getXuid(Player player) {
        String xuid = ColorUtil.replaceColorCode(conf.getString("guest"));

        if (player.getLoginChainData().getXUID() != null) {
            xuid = player.getLoginChainData().getXUID();
        }
        return xuid;
    }

    public static String getDimension(Player player) {
        String overworld = ColorUtil.replaceColorCode(conf.getString("overworld"));
        String nether = ColorUtil.replaceColorCode(conf.getString("nether"));
        String end = ColorUtil.replaceColorCode(conf.getString("end"));

        switch (player.getLevel().getDimension()) {
            case 0:
                return overworld;
            case 1:
                return nether;
            case 2:
                return end;
            default:
                return "Unknown";
        }
    }

    public static String getUnique(Player player) {
        String unique;
        String name = player.getDisplayName();

        if (!(conf.getString("Players." + name + ".unique-description").isEmpty())) {
            unique = conf.getString("Players." + name + ".unique-description");
        } else {
            unique = conf.getString("Players.default.description");
        }
        return ColorUtil.replaceColorCode(unique);
    }
}
