package me.indian.pl;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import me.indian.pl.Commands.OsTagCommand;
import me.indian.pl.Commands.TestttCommand;
import me.indian.pl.Listeners.Formater;
import me.indian.pl.Listeners.InputListener;
import me.indian.pl.Others.Metrics;
import me.indian.pl.Utils.OsTimer;
import me.indian.pl.Utils.OtherUtils;

public class OsTag extends PluginBase implements Listener {

    private static int pluginId = 16838;
    public static Boolean luckPerm;
    public static Boolean deathSkulls;

    private static OsTag instance;
    public OsTag() {
        instance = this;
    }
    public static OsTag getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        long millisActualTime = System.currentTimeMillis();

        getLogger().warning("§4The plugin now has so many naming changes, see the plugin page on CloudBurst to know them all and get it right ");
        getLogger().warning("§4If you have used the OsTag plugin before, rename the folder from §bOsTagPNX §4to §bOsTag");
        getLogger().warning("§4Permision names changed! From §bostagpnc.admin §4to §bostag.admin §4and added permision §bostag.colors §4for using §b& §4and disable §b§ §4in chat");


        PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("LuckPerms") == null) {
            getLogger().warning("§4You don't have lucky perms , ChatFormating don't corectly work");
            luckPerm = false;
        } else {
            luckPerm = true;
        }
        if (pm.getPlugin("DeathSkulls") == null) {
            getLogger().info("§cYou don't have DeathSkulls plugin, <deathskull> placeholder will not workg");
            deathSkulls = false;
        } else {
            deathSkulls = true;
        }
        saveDefaultConfig();
        sendOnEnableInfo(true, null);

        pm.registerEvents(new InputListener(this), this);

        ((PluginCommand<?>) this.getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) this.getCommand("tto")).setExecutor(new TestttCommand(this));


        if (this.getConfig().getBoolean("OsTag")) {
            pm.registerEvents(new OsTimer(this), this);
            this.getServer().getScheduler().scheduleRepeatingTask(new OsTimer(this), 20 * this.getConfig().getInt("refresh-time"));
        }
        if (this.getConfig().getBoolean("ChatFormater")) {
            pm.registerEvents(new Formater(this), this);
        }

        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("server_movement", () -> {
            return String.valueOf(this.getConfig().getBoolean("PowerNukkiX-movement-server"));
        }));
        metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> {
            return Server.getInstance().getNukkitVersion();
        }));


        long executionTime = System.currentTimeMillis() - millisActualTime;
        getLogger().info("§aStarted in §b" + executionTime + " §ams");
    }

    public void sendOnEnableInfo(boolean console, CommandSender sender) {

            String ver = this.getDescription().getVersion();
            String aut = this.getDescription().getAuthors() + "";
            String verNuk = this.getServer().getNukkitVersion();
            String servVer = this.getServer().getVersion();
            String apiVer = this.getServer().getApiVersion();
            String ip = this.getServer().getIp();
            String port = this.getServer().getPort() + "";
            if(console) {
                this.getLogger().info("§b-------------------------------");
                this.getLogger().info("§aOsTag version:§3 " + ver);
                this.getLogger().info("§aPlugin by:§6 " + aut.replace("[", "").replace("]", ""));
                this.getLogger().info("§aNukkit Version:§3 " + verNuk);
                this.getLogger().info("§aNukkit Api Version:§3 " + apiVer);
                this.getLogger().info("§aServer Version:§3 " + servVer);
                this.getLogger().info(" ");
                this.getLogger().info("§1Modules");
                this.getLogger().info("§aFormater§3: " + OtherUtils.getFormaterStatus(this));
                this.getLogger().info("§aOsTag§3: " + OtherUtils.getOsTagStatus(this));
                this.getLogger().info(" ");
                this.getLogger().info("§1Plugins§3");
                this.getLogger().info("§aDeathSkulls§3: " + OtherUtils.getDeathSkullsStatus(this));
                this.getLogger().info("§aLuckPerms§3: " + OtherUtils.getLuckPermStatus(this));
                this.getLogger().info(" ");
                this.getLogger().info("§b-------------------------------");
            } else {
            sender.sendMessage("§b-------------------------------");
            sender.sendMessage("§aOsTag version:§3 " + ver);
            sender.sendMessage("§aPlugin by:§6 " + aut.replace("[", "").replace("]", ""));
            sender.sendMessage("§aNukkit Version:§3 " + verNuk);
            sender.sendMessage("§aNukkit Api Version:§3 " + apiVer);
            sender.sendMessage("§aServer Version:§3 " + servVer);
            sender.sendMessage(" ");
            sender.sendMessage("§1Modules");
            sender.sendMessage("§aFormater§3: " + OtherUtils.getFormaterStatus(this));
            sender.sendMessage("§aOsTag§3: " + OtherUtils.getOsTagStatus(this));
            sender.sendMessage(" ");
            sender.sendMessage("§1Plugins");
            sender.sendMessage("§aDeathSkulls§3: " + OtherUtils.getDeathSkullsStatus(this));
            sender.sendMessage("§aLuckPerms§3: " + OtherUtils.getLuckPermStatus(this));
            sender.sendMessage(" ");
            sender.sendMessage("§b-------------------------------");
            }


    }

}
