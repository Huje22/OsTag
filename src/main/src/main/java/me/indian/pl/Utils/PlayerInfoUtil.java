package me.indian.pl.Utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.pl.Listeners.InputListener;
import me.indian.pl.OsTag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class PlayerInfoUtil {


    public static String getDevice(Player p, OsTag plugin) {
        Config conf = plugin.getConfig();
        String windows = conf.getString("Windows");
        String android = conf.getString("Android");
        String ios = conf.getString("Ios");
        String mac = conf.getString("Mac");
        String fire = conf.getString("Fire");
        String gearvr = conf.getString("Gearvr");
        String hololens = conf.getString("Hololens");
        String dedicated = conf.getString("Hedicated");
        String tvos = conf.getString("TvOs");
        String playstation = conf.getString("PlayStation");
        String nx = conf.getString("Nintendo");
        String xbox = conf.getString("Xbox");
        String unknow = conf.getString("Unknow");


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
    public static String getControler(Player p, OsTag plugin) {
        Config conf = plugin.getConfig();

        String motion_controller = conf.getString("motion_controller");
        String dotyk = conf.getString("touch");
        String klawa = conf.getString("keyboard");
        String pad = conf.getString("gamepad");
        String unknowcon = conf.getString("UnknowControler");

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
    public static String getXp(Player p, OsTag plugin) {
        Config conf = plugin.getConfig();
        String xp = "";
        if (p.getExperienceLevel() >= 1) {
            xp = conf.getString("1lvl") + p.getExperienceLevel();
        }
        if (p.getExperienceLevel() >= 10) {
            xp = conf.getString("10lvl") + p.getExperienceLevel();
        }
        if (p.getExperienceLevel() >= 15) {
            xp = conf.getString("15lvl") + p.getExperienceLevel();
        }
        if (p.getExperienceLevel() >= 20) {
            xp = conf.getString("20lvl") + p.getExperienceLevel();
        }
        if (p.getExperienceLevel() >= 25) {
            xp = conf.getString("25lvl") + p.getExperienceLevel();
        }
        if (p.getExperienceLevel() >= 30) {
            xp = conf.getString("30lvl") + p.getExperienceLevel();
        }
        return xp;
    }
    public static String getGameMode(Player p, OsTag plugin) {
        Config conf = plugin.getConfig();
        String survival = conf.getString("survival");
        String creative = conf.getString("creative");
        String adventure = conf.getString("adventure");

        String gamemode = "" + p.getGamemode();

        String gmf = gamemode
                .replace("1", creative)
                .replace("2", adventure)
                .replace("0", survival);


        return gmf;
    }
    public static String getPing(Player p) {

        String ping = "";
        if (p.getPing() >= 1) {
            ping = "§a" + p.getPing();
        }
        if (p.getPing() >= 75) {
            ping = "§e" + p.getPing();
        }
        if (p.getPing() >= 100) {
            ping = "§c" + p.getPing();
        }
        return ping;
    }
    public static String getLuckPermPrefix(Player p, OsTag plugin) {

        String pref = "";

        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {

            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            pref = LuckPermUtil.getPrefix(user);
        }
        return pref;
    }
    public static String getLuckPermSufix(Player p, OsTag plugin) {
        String suf = "";
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            suf = LuckPermUtil.getSuffix(user);

        }
        return suf;
    }
    public static String getGroupDisName(Player p, OsTag plugin){
        String group = "";
        if (plugin.getServer().getPluginManager().getPlugin("LuckPerms") != null) {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);
            group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup()).getDisplayName();
        }
        return group ;
    }
    public static String getXuid(Player p) {
        String xuid = "Guest";
        if (p.getLoginChainData().getXUID() != null) {
            xuid = p.getLoginChainData().getXUID();
        }
        return xuid;
    }


}
