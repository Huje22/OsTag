package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.listener.CpsListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;

import java.text.DecimalFormat;


public class PlayerInfoUtil {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static final LuckPerms luckPerms = plugin.getLuckperms();

    public static String replaceAllInfo(final Player player, final String msg) {
        return msg
                .replace("<cooldown>", plugin.getFormater().getCooldown(player))
                .replace("<name>", player.getName())
                .replace("<dis_name>", player.getDisplayName())
                .replace("<suffix>", getLuckPermSuffix(player))
                .replace("<preffix>", getLuckPermPreffix(player))
                .replace("<groupDisName>", getLuckPermGroupDisName(player))
                .replace("<unique_description>", getUnique(player))
                .replace("<xp>", getXp(player))
                .replace("<device>", getDevice(player))
                .replace("<controller>", getController(player))
                .replace("<health>", getHealth(player))
                .replace("<max_health>", String.valueOf(player.getMaxHealth()))
                .replace("<model>", player.getLoginChainData().getDeviceModel())
                .replace("<food>", String.valueOf(player.getFoodData().getLevel() / 2))
                .replace("<max_food>", String.valueOf(player.getFoodData().getMaxLevel() / 2))
                .replace("<version>", player.getLoginChainData().getGameVersion())
                .replace("<xuid>", getXuid(player))
                .replace("<language>", player.getLoginChainData().getLanguageCode())
                .replace("<ping>", getPing(player))
                .replace("<cps>", String.valueOf(CpsListener.getCPS(player)))
                .replace("<gamemode>", getGameMode(player))
                .replace("<dimension>", getDimension(player)
                );
    }

    public static String getDevice(final Player player) {
        final String windows = MessageUtil.colorize(config.getString("Windows"));
        final String android = MessageUtil.colorize(config.getString("Android"));
        final String ios = MessageUtil.colorize(config.getString("Ios"));
        final String mac = MessageUtil.colorize(config.getString("Mac"));
        final String fire = MessageUtil.colorize(config.getString("Fire"));
        final String hololens = MessageUtil.colorize(config.getString("Hololens"));
        final String dedicated = MessageUtil.colorize(config.getString("Dedicated"));
        final String tvos = MessageUtil.colorize(config.getString("TvOs"));
        final String playstation = MessageUtil.colorize(config.getString("PlayStation"));
        final String nintendo = MessageUtil.colorize(config.getString("Nintendo"));
        final String xbox = MessageUtil.colorize(config.getString("Xbox"));
        final String linux = MessageUtil.colorize(config.getString("Linux"));
        final String unknown = MessageUtil.colorize(config.getString("Unknown"));

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
                return unknown;
        }
    }

    public static String getController(final Player player) {
        final String motion_controller = MessageUtil.colorize(config.getString("Motion_controller"));
        final String touch = MessageUtil.colorize(config.getString("Touch"));
        final String keyboard = MessageUtil.colorize(config.getString("Keyboard"));
        final String pad = MessageUtil.colorize(config.getString("Gamepad"));
        final String unknowcon = MessageUtil.colorize(config.getString("UnknownController"));

        if (plugin.serverMovement) {
            switch (plugin.getInputListener().getController(player)) {
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

    public static String getXp(final Player player) {
        String xp = "0";
        final int playerXp = player.getExperienceLevel();

        if (playerXp == 0) {
            xp = config.getString("1lvl") + playerXp;
        }
        if (playerXp >= 1) {
            xp = config.getString("1lvl") + playerXp;
        }
        if (playerXp >= 5) {
            xp = config.getString("5lvl") + playerXp;
        }
        if (playerXp >= 10) {
            xp = config.getString("10lvl") + playerXp;
        }
        if (playerXp >= 15) {
            xp = config.getString("15lvl") + playerXp;
        }
        if (playerXp >= 20) {
            xp = config.getString("20lvl") + playerXp;
        }
        if (playerXp >= 25) {
            xp = config.getString("25lvl") + playerXp;
        }
        if (playerXp >= 30) {
            xp = config.getString("30lvl") + playerXp;
        }
        if (playerXp >= 35) {
            xp = config.getString("35lvl") + playerXp;
        }

        return MessageUtil.colorize(xp);
    }

    public static String getGameMode(final Player player) {
        final String survival = MessageUtil.colorize(config.getString("survival"));
        final String creative = MessageUtil.colorize(config.getString("creative"));
        final String adventure = MessageUtil.colorize(config.getString("adventure"));
        final String spectator = MessageUtil.colorize(config.getString("spectator"));

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

    public static String getPing(final Player player) {
        String ping = MessageUtil.colorize("&r0");
        final int playerPing = player.getPing();

        if (playerPing >= 1) {
            ping = MessageUtil.colorize(config.getString("low-ping")) + playerPing;
        }
        if (playerPing >= 75) {
            ping = MessageUtil.colorize(config.getString("medium-ping")) + playerPing;
        }
        if (playerPing >= 100) {
            ping = MessageUtil.colorize(config.getString("high-ping")) + playerPing;
        }
        return ping;
    }

    public static String getLuckPermPreffix(final Player player) {
        String pref = "";

        if (plugin.luckPerm) {
            final User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            if (user.getCachedData().getMetaData().getPrefix() != null) {
                pref = user.getCachedData().getMetaData().getPrefix();
            }
        }
        return MessageUtil.colorize(pref);
    }

    public static String getLuckPermSuffix(final Player player) {
        String suf = "";

        if (plugin.luckPerm) {
            final User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            if (user.getCachedData().getMetaData().getSuffix() != null) {
                suf = user.getCachedData().getMetaData().getSuffix();
            }
        }
        return MessageUtil.colorize(suf);
    }

    @SuppressWarnings("ConstantConditions")
    public static String getLuckPermGroupDisName(final Player player) {
        String group = "";

        if (plugin.luckPerm) {
            final User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            if (luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName() != null) {
                group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName();
            }
        }
        return MessageUtil.colorize(group);
    }

    public static String getXuid(final Player player) {
        String xuid = MessageUtil.colorize(config.getString("guest"));

        if (player.getLoginChainData().getXUID() != null) {
            xuid = player.getLoginChainData().getXUID();
        }
        return xuid;
    }

    public static String getDimension(final Player player) {
        final String overworld = MessageUtil.colorize(config.getString("overworld"));
        final String nether = MessageUtil.colorize(config.getString("nether"));
        final String end = MessageUtil.colorize(config.getString("end"));

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

    public static String getUnique(final Player player) {
        String unique = config.getString("Players.default.description");
        final String name = player.getDisplayName();

        if (!(config.getString("Players." + name + ".unique-description").isEmpty())) {
            unique = config.getString("Players." + name + ".unique-description");
        }

        return MessageUtil.colorize(unique);
    }

    public static String getHealth(final Player player) {
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);

        return df.format(player.getHealth());
    }
}
