package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import java.text.DecimalFormat;
import me.indian.ostag.OsTag;
import me.indian.ostag.listener.CpsListener;
import me.indian.ostag.listener.InputListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;


public class PlayerInfoUtil {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static final LuckPerms luckPerms = plugin.getLuckperms();

    public static String replaceAllInfo(final Player player, final String msg) {
        return msg
                .replace("<cooldown>", plugin.getFormater().cooldown(player))
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
        final String windows = TextUtil.replaceColorCode(config.getString("Windows"));
        final String android = TextUtil.replaceColorCode(config.getString("Android"));
        final String ios = TextUtil.replaceColorCode(config.getString("Ios"));
        final String mac = TextUtil.replaceColorCode(config.getString("Mac"));
        final String fire = TextUtil.replaceColorCode(config.getString("Fire"));
        final String hololens = TextUtil.replaceColorCode(config.getString("Hololens"));
        final String dedicated = TextUtil.replaceColorCode(config.getString("Dedicated"));
        final String tvos = TextUtil.replaceColorCode(config.getString("TvOs"));
        final String playstation = TextUtil.replaceColorCode(config.getString("PlayStation"));
        final String nintendo = TextUtil.replaceColorCode(config.getString("Nintendo"));
        final String xbox = TextUtil.replaceColorCode(config.getString("Xbox"));
        final String linux = TextUtil.replaceColorCode(config.getString("Linux"));
        final String unknown = TextUtil.replaceColorCode(config.getString("Unknown"));

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
        final String motion_controller = TextUtil.replaceColorCode(config.getString("Motion_controller"));
        final String touch = TextUtil.replaceColorCode(config.getString("Touch"));
        final String keyboard = TextUtil.replaceColorCode(config.getString("Keyboard"));
        final String pad = TextUtil.replaceColorCode(config.getString("Gamepad"));
        final String unknowcon = TextUtil.replaceColorCode(config.getString("UnknownController"));

        if (plugin.serverMovement) {
            switch (InputListener.getController(player)) {
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

        return TextUtil.replaceColorCode(xp);
    }

    public static String getGameMode(final Player player) {
        final String survival = TextUtil.replaceColorCode(config.getString("survival"));
        final String creative = TextUtil.replaceColorCode(config.getString("creative"));
        final String adventure = TextUtil.replaceColorCode(config.getString("adventure"));
        final String spectator = TextUtil.replaceColorCode(config.getString("spectator"));

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
        String ping = TextUtil.replaceColorCode("&r0");
       final int playerPing = player.getPing();
        
        if (playerPing >= 1) {
            ping = TextUtil.replaceColorCode(config.getString("low-ping")) + playerPing;
        }
        if (playerPing >= 75) {
            ping = TextUtil.replaceColorCode(config.getString("medium-ping")) + playerPing;
        }
        if (playerPing >= 100) {
            ping = TextUtil.replaceColorCode(config.getString("high-ping")) + playerPing;
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
        return TextUtil.replaceColorCode(pref);
    }

    public static String getLuckPermSuffix(final Player player) {
        String suf = "";

        if (plugin.luckPerm) {
            final User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
            if (user.getCachedData().getMetaData().getSuffix() != null) {
                suf = user.getCachedData().getMetaData().getSuffix();
            }
        }
        return TextUtil.replaceColorCode(suf);
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
        return TextUtil.replaceColorCode(group);
    }

    public static String getXuid(final Player player) {
        String xuid = TextUtil.replaceColorCode(config.getString("guest"));

        if (player.getLoginChainData().getXUID() != null) {
            xuid = player.getLoginChainData().getXUID();
        }
        return xuid;
    }

    public static String getDimension(final Player player) {
        final String overworld = TextUtil.replaceColorCode(config.getString("overworld"));
        final String nether = TextUtil.replaceColorCode(config.getString("nether"));
        final String end = TextUtil.replaceColorCode(config.getString("end"));

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

        return TextUtil.replaceColorCode(unique);
    }

    public static String getHealth(final Player player) {
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);

        return df.format(player.getHealth());
    }
}
