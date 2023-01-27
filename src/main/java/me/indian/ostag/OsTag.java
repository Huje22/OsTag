package me.indian.ostag;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.commands.OsTagCommand;
import me.indian.ostag.commands.TestttCommand;
import me.indian.ostag.listeners.CpsListener;
import me.indian.ostag.listeners.Formater;
import me.indian.ostag.listeners.InputListener;
import me.indian.ostag.others.OsTagMetrics;
import me.indian.ostag.utils.*;

public class OsTag extends PluginBase {

    public static boolean luckPerm = false;
    public static boolean papKot = false;
    public static boolean serverMovement;
    private static OsTag instance;

    public static OsTag getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        long millisActualTime = System.currentTimeMillis();
        instance = this;
        PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("LuckPerms") == null) {
            getLogger().warning(ColorUtil.replaceColorCode("&cYou don't have lucky perms , ChatFormating don't corectly work"));
        } else {
            luckPerm = true;
        }
        if (pm.getPlugin("PlaceholderAPI") == null && pm.getPlugin("KotlinLib") == null) {
            getLogger().info(ColorUtil.replaceColorCode("&cYou don't have PlaceholderAPI or kotlin lib,placeholders from &b\"PlaceholderAPI\"&c will not work"));
        } else {
            papKot = true;
            registerPlaceholders();
        }
        saveDefaultConfig();
        serverMovement = getConfig().getBoolean("movement-server");
        getConfig().set("PowerNukkiX-movement-server", "change this to movement-server and set to true or false, see wiki for more instructions https://github.com/IndianBartonka/OsTag/wiki/For-nukkit-forks");
        getLogger().info(ColorUtil.replaceColorCode("&aCheck &econfig.yml &anow &bPowerNukkiX-movement-server&a has been named &bmovement-server"));
        pm.registerEvents(new CpsListener(), this);
        if(serverMovement) {
            pm.registerEvents(new InputListener(), this);
        }
        ((PluginCommand<?>) getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) getCommand("tto")).setExecutor(new TestttCommand(this));
        if (getConfig().getBoolean("OsTag")) {
            pm.registerEvents(new OsTimer(), this);
            int refreshTime = getConfig().getInt("refresh-time");
            if (refreshTime <= 0) {
                refreshTime = 1;
                getConfig().set("refresh-time", 1);
                getConfig().save();
                getLogger().warning(ColorUtil.replaceColorCode("&cRefresh time must be higer than &b0 &c,we will set it up for you!"));
            }

            getServer().getScheduler().scheduleRepeatingTask(new OsTimer(), 20 * refreshTime);
        }
        if (this.getConfig().getBoolean("ChatFormater")) {
            pm.registerEvents(new Formater(this), this);
        }
        OsTagMetrics.metricsStart();
        sendOnEnableInfo("admin", getServer().getConsoleSender());
        long executionTime = System.currentTimeMillis() - millisActualTime;
        getLogger().info(ColorUtil.replaceColorCode("&aStarted in &b" + executionTime + " &ams"));
    }


    public void sendOnEnableInfo(String type, CommandSender sender) {
        String ver = this.getDescription().getVersion();
        String aut = String.valueOf(this.getDescription().getAuthors());
        String verNuk = this.getServer().getNukkitVersion();
        String servVer = this.getServer().getVersion();
        String apiVer = this.getServer().getApiVersion();
        switch (type) {
            case "admin":
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + ver));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Version:&3 " + verNuk));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Api Version:&3 " + apiVer));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + servVer));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Plugins"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aLuckPerms&3: " + OtherUtils.getLuckPermStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aKotlinLib & PlaceholderAPI&3: " + OtherUtils.getKotOrPapiStatus()));

                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                break;
            case "normal":
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + ver));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + servVer));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                break;
            default:
                sender.sendMessage("Unknow OnEnableInfo type");
                break;
        }
    }
    public void registerPlaceholders() {
        PlaceholderAPI api = PlaceholderAPI.getInstance();
        String prefix = "ostag_";
        api.builder(prefix + "cps", Integer.class)
                .visitorLoader(entry -> CpsListener.getCPS(entry.getPlayer()))
                .build();
        api.builder(prefix + "test", String.class)
                .visitorLoader(entry -> "test placeholder")
                .build();
        api.builder(prefix + "device", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getDevice(entry.getPlayer()))
                .build();
        api.builder(prefix + "controler", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getControler(entry.getPlayer()))
                .build();
        api.builder(prefix + "prefix", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getLuckPermPrefix(entry.getPlayer()))
                .build();
        api.builder(prefix + "suffix", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getLuckPermSufix(entry.getPlayer()))
                .build();
        api.builder(prefix + "group", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getLuckPermGroupDisName(entry.getPlayer()))
                .build();
        api.builder(prefix + "version", String.class)
                .visitorLoader(entry -> entry.getPlayer().getLoginChainData().getGameVersion())
                .build();
    }
}
