package me.indian.pl;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import me.indian.pl.commands.OsTagCommand;
import me.indian.pl.commands.TestttCommand;
import me.indian.pl.listeners.Formater;
import me.indian.pl.listeners.InputListener;
import me.indian.pl.others.Metrics;
import me.indian.pl.utils.*;

public class OsTag extends PluginBase implements Listener {

    public static boolean luckPerm;

    public static boolean deathSkulls;

    public static boolean factions;

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
        getLogger().warning(ChatColor.replaceColorCode("&4The plugin now has so many naming changes, see the plugin page on CloudBurst to know them all and get it right "));
        getLogger().warning(ChatColor.replaceColorCode("&4If you have used the OsTag plugin before, rename the folder from &bOsTagPNX &4to &bOsTag"));
        getLogger().warning(ChatColor.replaceColorCode("&4Permision names changed! From &bostagpnc.admin &4to &bostag.admin &4and added permision &bostag.colors &4for using §b& &4in chat"));
        getLogger().warning(ChatColor.replaceColorCode("&4See chnage log!"));


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
        if (pm.getPlugin("Factions") == null) {
            getLogger().info(ChatColor.replaceColorCode("&cYou don't have Factions plugin, <faction> placeholder will not working"));
            factions = false;
        } else {
            factions = true;
        }

        saveDefaultConfig();

        sendOnEnableInfo("console", null);

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

        metricsStart();

        long executionTime = System.currentTimeMillis() - millisActualTime;
        getLogger().info(ChatColor.replaceColorCode("&aStarted in &b" + executionTime + " &ams"));
    }

    private void metricsStart(){
        int pluginId = 16838;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("server_movement", () -> {
            return String.valueOf(this.getConfig().getBoolean("PowerNukkiX-movement-server"));
        }));
        metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> {
            return Server.getInstance().getNukkitVersion();
        }));
        metrics.addCustomChart(new Metrics.SimplePie("ostag_vs_chatformater", () -> {
            String info = "";
            boolean ostag = this.getConfig().getBoolean("OsTag");
            boolean chatFormater = this.getConfig().getBoolean("ChatFormater");
            if (ostag && chatFormater) {
                info = "OsTag and ChatFormater";
            }
            if (ostag && !chatFormater) {
                info = "OsTag";
            }
            if (!ostag && chatFormater) {
                info = "ChatFormater";
            }
            return info;
        }));
    }

    public void sendOnEnableInfo(String s, CommandSender sender) {

        String ver = this.getDescription().getVersion();
        String aut = String.valueOf(this.getDescription().getAuthors());
        String verNuk = this.getServer().getNukkitVersion();
        String servVer = this.getServer().getVersion();
        String apiVer = this.getServer().getApiVersion();

        switch (s) {
            case "console":
                this.getLogger().info(ChatColor.replaceColorCode("&b-------------------------------"));
                this.getLogger().info(ChatColor.replaceColorCode("&aOsTag version:&3 " + ver));
                this.getLogger().info(ChatColor.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                this.getLogger().info(ChatColor.replaceColorCode("&aNukkit Version:&3 " + verNuk));
                this.getLogger().info(ChatColor.replaceColorCode("&aNukkit Api Version:&3 " + apiVer));
                this.getLogger().info(ChatColor.replaceColorCode("&aServer Version:&3 " + servVer));
                this.getLogger().info(ChatColor.replaceColorCode(" "));
                this.getLogger().info(ChatColor.replaceColorCode("&1Modules"));
                this.getLogger().info(ChatColor.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                this.getLogger().info(ChatColor.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                this.getLogger().info(ChatColor.replaceColorCode(" "));
                this.getLogger().info(ChatColor.replaceColorCode("&1Plugins&3"));
                this.getLogger().info(ChatColor.replaceColorCode("&aDeathSkulls&3: " + OtherUtils.getDeathSkullsStatus()));
                this.getLogger().info(ChatColor.replaceColorCode("&aLuckPerms&3: " + OtherUtils.getLuckPermStatus()));
                this.getLogger().info(ChatColor.replaceColorCode("&aFactions&3: " + OtherUtils.getFactionsStatus()));


                this.getLogger().info(ChatColor.replaceColorCode(" "));
                this.getLogger().info(ChatColor.replaceColorCode("&b-------------------------------"));
                break;
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
                sender.sendMessage(ChatColor.replaceColorCode("&aFactions&3: " + OtherUtils.getFactionsStatus()));


                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&b-------------------------------"));
                break;
            case "normal":
                sender.sendMessage(ChatColor.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ChatColor.replaceColorCode("&aOsTag version:&3 " + ver));
                sender.sendMessage(ChatColor.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&1Modules"));
                sender.sendMessage(ChatColor.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ChatColor.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ChatColor.replaceColorCode(" "));
                sender.sendMessage(ChatColor.replaceColorCode("&b-------------------------------"));
                break;
        }
    }


}
