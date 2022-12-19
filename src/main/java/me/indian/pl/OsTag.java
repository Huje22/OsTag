package me.indian.pl;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.pl.commands.OsTagCommand;
import me.indian.pl.commands.TestttCommand;
import me.indian.pl.listeners.Formater;
import me.indian.pl.listeners.InputListener;
import me.indian.pl.others.OsTagMetrics;
import me.indian.pl.utils.*;

public class OsTag extends PluginBase implements Listener {

    public static boolean luckPerm;

    public static boolean deathSkulls;

    public static boolean papKot;

    private static OsTag instance;

    public static OsTag getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        long millisActualTime = System.currentTimeMillis();
        instance = this;
        //class instances that are not dependent on the user's choice
        new OtherUtils();
        new PlayerInfoUtil();

        //some informations
        /*
        None
         */
        PluginManager pm = getServer().getPluginManager();
        //plugins info
        if (pm.getPlugin("LuckPerms") == null) {
            getLogger().warning(ChatColor.replaceColorCode("&4You don't have lucky perms , ChatFormating don't corectly work"));
            luckPerm = false;
        } else {
            luckPerm = true;
        }
        if (pm.getPlugin("DeathSkulls") == null) {
            getLogger().info(ChatColor.replaceColorCode("&cYou don't have DeathSkulls plugin, <deathskull> placeholder will not working"));
            deathSkulls = false;
        } else {
            deathSkulls = true;
        }
        if (pm.getPlugin("PlaceholderAPI") == null && pm.getPlugin("KotlinLib") == null) {
            getLogger().info(ChatColor.replaceColorCode("&cYou don't have PlaceholderAPI or kotlin lib,placeholders with \"PlaceholderAPI\" will not work"));
            papKot = false;

        } else {
            papKot = true;

            registerPlaceholders();
        }

        saveDefaultConfig();

        sendOnEnableInfo("admin", getServer().getConsoleSender());

        pm.registerEvents(new InputListener(this), this);

        ((PluginCommand<?>) this.getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) this.getCommand("tto")).setExecutor(new TestttCommand(this));

        if (this.getConfig().getBoolean("OsTag")) {
            new OsTagAdd();
            new OsTimer();
            pm.registerEvents(new OsTimer(), this);
            this.getServer().getScheduler().scheduleRepeatingTask(new OsTimer(), 20 * this.getConfig().getInt("refresh-time"));
        }
        if (this.getConfig().getBoolean("ChatFormater")) {
            pm.registerEvents(new Formater(this), this);
        }
        new OsTagMetrics();
        OsTagMetrics.metricsStart();



        long executionTime = System.currentTimeMillis() - millisActualTime;
        getLogger().info(ChatColor.replaceColorCode("&aStarted in &b" + executionTime + " &ams"));
    }


    public void sendOnEnableInfo(String s, CommandSender sender) {

        String ver = this.getDescription().getVersion();
        String aut = String.valueOf(this.getDescription().getAuthors());
        String verNuk = this.getServer().getNukkitVersion();
        String servVer = this.getServer().getVersion();
        String apiVer = this.getServer().getApiVersion();

        switch (s) {
            case "admin":
                sender.sendMessage(ChatColor.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ChatColor.replaceColorCode("&aOsTag version:&3 " + ver));
                sender.sendMessage(ChatColor.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                sender.sendMessage(ChatColor.replaceColorCode("&aNukkit Version:&3 " + verNuk));
                sender.sendMessage(ChatColor.replaceColorCode("&aNukkit Api Version:&3 " + apiVer));
                sender.sendMessage(ChatColor.replaceColorCode("&aServer Version:&3 " + servVer));
                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&1Modules"));
                sender.sendMessage(ChatColor.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ChatColor.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&1Plugins"));
                sender.sendMessage(ChatColor.replaceColorCode("&aDeathSkulls&3: " + OtherUtils.getDeathSkullsStatus()));
                sender.sendMessage(ChatColor.replaceColorCode("&aLuckPerms&3: " + OtherUtils.getLuckPermStatus()));
                sender.sendMessage(ChatColor.replaceColorCode("&aKotlinLib & PlaceholderAPI&3: " + OtherUtils.getKotOrPapiStatus()));

                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&b-------------------------------"));
                break;
            case "normal":
                sender.sendMessage(ChatColor.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ChatColor.replaceColorCode("&aOsTag version:&3 " + ver));
                sender.sendMessage(ChatColor.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                sender.sendMessage(ChatColor.replaceColorCode("&aServer Version:&3 " + servVer));
                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&1Modules"));
                sender.sendMessage(ChatColor.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ChatColor.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&b-------------------------------"));
                break;
        }
    }

    public void registerPlaceholders() {
        PlaceholderAPI api = PlaceholderAPI.getInstance();
        String prefix = "ostag_";
        api.builder(prefix +"cps", Integer.class)
                .visitorLoader(entry -> {
                    return InputListener.getCPS(entry.getPlayer());
                })
                .build();

        api.builder(prefix +"test", String.class)
                .visitorLoader(entry -> {
                    return "test placeholder";
                })
                .build();
        api.builder(prefix +"device", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getDevice(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"controler", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getControler(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"skull", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getSkulll(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"prefix", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getLuckPermPrefix(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"suffix", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getLuckPermSufix(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"version", String.class)
                .visitorLoader(entry -> {
                    return entry.getPlayer().getLoginChainData().getGameVersion();
                })
                .build();
    }
}
