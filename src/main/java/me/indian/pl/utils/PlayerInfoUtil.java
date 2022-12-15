package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.pl.listeners.InputListener;
import me.indian.pl.OsTag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import static me.indian.deathskulls.listeners.PlayerDeathListener.skullPlayer;


public class PlayerInfoUtil {

    private static OsTag plugin;

    public PlayerInfoUtil(){
        this.plugin = OsTag.getInstance();
    }



    public static String getDevice(Player p) {
        Config conf = plugin.getConfig();
        String windows = ChatColor.replaceColorCode(conf.getString("Windows"));
        String android = ChatColor.replaceColorCode(conf.getString("Android"));
        String ios = ChatColor.replaceColorCode(conf.getString("Ios"));
        String mac = ChatColor.replaceColorCode(conf.getString("Mac"));
        String fire = ChatColor.replaceColorCode(conf.getString("Fire"));
        String gearvr = ChatColor.replaceColorCode(conf.getString("Gearvr"));
        String hololens = ChatColor.replaceColorCode(conf.getString("Hololens"));
        String dedicated = ChatColor.replaceColorCode(conf.getString("Hedicated"));
        String tvos = ChatColor.replaceColorCode(conf.getString("TvOs"));
        String playstation = ChatColor.replaceColorCode(conf.getString("PlayStation"));
        String nx = ChatColor.replaceColorCode(conf.getString("Nintendo"));
        String xbox = ChatColor.replaceColorCode(conf.getString("Xbox"));
        String unknow = ChatColor.replaceColorCode(conf.getString("Unknow"));


        String dev = "" + p.getLoginChainData().getDeviceOS();

        String device = dev
                .replace("1", android)
                .replace("2", ios)
                .replace("3", mac)
                .replace("4", fire)
                .replace("5", gearvr)
                .replace("6", hololens)
                .replace("7", windows)
                .replace("8", windows)
                .replace("9", dedicated)
                .replace("10", tvos)
                .replace("11", playstation)
                .replace("12", nx)
                .replace("13", xbox)
                .replace("Unknow", unknow);

        return device;
    }

    public static String getControler(Player p) {
        Config conf = plugin.getConfig();

        String motion_controller = ChatColor.replaceColorCode(conf.getString("motion_controller"));
        String dotyk = ChatColor.replaceColorCode(conf.getString("touch"));
        String klawa = ChatColor.replaceColorCode(conf.getString("keyboard"));
        String pad = ChatColor.replaceColorCode(conf.getString("gamepad"));
        String unknowcon = ChatColor.replaceColorCode(conf.getString("UnknowControler"));

        String controlerr = "";
        if (plugin.getConfig().getBoolean("PowerNukkiX-movement-server")) {
            controlerr = InputListener.getControler(p) + "";
        } else {
            controlerr = p.getLoginChainData().getCurrentInputMode() + "";
        }
        String crt = controlerr
                .replace("0", unknowcon)
                .replace("1", klawa)
                .replace("2", dotyk)
                .replace("3", pad)
                .replace("4", motion_controller)
                .replace("UNDEFINED", unknowcon)
                .replace("MOUSE", klawa)
                .replace("TOUCH", dotyk)
                .replace("GAME_PAD", pad)
                .replace("MOTION_CONTROLLER", motion_controller);

        return crt;
    }

    public static String getXp(Player p) {
        Config conf = plugin.getConfig();
        String xp = "0";
        if (p.getExperienceLevel() == 0) {
            xp = ChatColor.replaceColorCode(conf.getString("1lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 1) {
            xp = ChatColor.replaceColorCode(conf.getString("1lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 10) {
            xp = ChatColor.replaceColorCode(conf.getString("10lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 15) {
            xp = ChatColor.replaceColorCode(conf.getString("15lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 20) {
            xp = ChatColor.replaceColorCode(conf.getString("20lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 25) {
            xp = ChatColor.replaceColorCode(conf.getString("25lvl") + p.getExperienceLevel());
        }
        if (p.getExperienceLevel() >= 30) {
            xp = ChatColor.replaceColorCode(conf.getString("30lvl") + p.getExperienceLevel());
        }

        return xp;
    }

    public static String getGameMode(Player p) {
        Config conf = plugin.getConfig();
        String survival = ChatColor.replaceColorCode(conf.getString("survival"));
        String creative = ChatColor.replaceColorCode(conf.getString("creative"));
        String adventure = ChatColor.replaceColorCode(conf.getString("adventure"));

        String gamemode = "" + p.getGamemode();

        String gmf = gamemode
                .replace("1", creative)
                .replace("2", adventure)
                .replace("0", survival);


        return gmf;
    }

    public static String getPing(Player p) {

        String ping = "";
        Config conf = plugin.getConfig();
        if (p.getPing() >= 1) {
            ping = ChatColor.replaceColorCode(conf.getString("low-ping")) + p.getPing();
        }
        if (p.getPing() >= 75) {
            ping = ChatColor.replaceColorCode(conf.getString("medium-ping")) + p.getPing();
        }
        if (p.getPing() >= 100) {
            ping = ChatColor.replaceColorCode(conf.getString("high-ping")) + p.getPing();
        }
        return ping;
    }

    public static String getLuckPermPrefix(Player p) {

        String pref = "";

        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {

            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            pref = LuckPermUtil.getPrefix(user);
        }
        return ChatColor.replaceColorCode(pref);
    }

    public static String getLuckPermSufix(Player p) {
        String suf = "";
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            suf = LuckPermUtil.getSuffix(user);

        }
        return ChatColor.replaceColorCode(suf);
    }

    public static String getGroupDisName(Player p) {
        String group = "";
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            if (luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName() != null) {
                group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName();
            }
        }
        return ChatColor.replaceColorCode(group);
    }

    public static String getXuid(Player p) {
        Config conf = plugin.getConfig();
        String xuid = ChatColor.replaceColorCode(conf.getString("guest"));
        if (p.getLoginChainData().getXUID() != null) {
            xuid = p.getLoginChainData().getXUID();
        }
        return xuid;
    }

    public static String getSkulll(Player p) {
        String skull = "";
        if (plugin.getServer().getPluginManager().getPlugin("DeathSkulls") != null) {
            if (skullPlayer.contains(p.getUniqueId())) {
                skull = ChatColor.replaceColorCode(plugin.getConfig().getString("Skull-icon"));
            }
        }
        return ChatColor.replaceColorCode(skull);
    }

    public static String getDimension(Player p) {
        Config conf = plugin.getConfig();
        String overworld = conf.getString("overworld");
        String nether = conf.getString("nether");
        String end = conf.getString("end");

        String dimension = "" + p.getLevel().getDimension();
        String dim = dimension
                .replace("0", overworld)
                .replace("1", nether)
                .replace("2", end);


        return ChatColor.replaceColorCode(dim);
    }

    public static String getPlayerUnique(Player p) {
        Config conf = plugin.getConfig();
        String unique = "";
        String name = p.getDisplayName();

        if (!(conf.getString("Players." + name + ".unique-description").isEmpty())) {
            unique = conf.getString("Players." + name + ".unique-description");
        } else {
            unique = conf.getString("Players.default.description");
        }
        return unique;
    }
}
