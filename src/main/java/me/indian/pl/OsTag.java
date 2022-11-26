package me.indian.pl;

import cn.nukkit.Server;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import me.indian.pl.Commands.OsTagCommand;
import me.indian.pl.Commands.TestttCommand;
import me.indian.pl.Listeners.Formater;
import me.indian.pl.Listeners.InputListener;
import me.indian.pl.Others.Metrics;
import me.indian.pl.Utils.OsTagAdd;
import me.indian.pl.Utils.OsTimer;
import me.indian.pl.Utils.OtherUtils;

public class OsTag extends PluginBase implements Listener {

    private static int pluginId = 16838;
    public static Boolean luckPerm;
    public static Boolean deathSkulls;

    @Override
    public void onEnable() {

        PluginManager pm = getServer().getPluginManager();
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().warning("You don't have lucky perms , ChatFormating dont corectly work");
            luckPerm = false;
        } else {
            luckPerm = true;
        }
        if (getServer().getPluginManager().getPlugin("DeathSkulls") == null) {
            getLogger().info("You don't have DeathSkulls plugin, <deathskull> placeholder will not workg");
            deathSkulls = false;
        } else {
            deathSkulls = true;
        }
        saveDefaultConfig();
        sendOnEnableInfo();

        pm.registerEvents(new InputListener(this), this);

        ((PluginCommand<?>) this.getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) this.getCommand("tto")).setExecutor(new TestttCommand(this));


        if (this.getConfig().getBoolean("OsTag")) {
            pm.registerEvents(new OsTimer(this), this);
            this.getServer().getScheduler().scheduleRepeatingTask(new OsTimer(this), 20 * this.getConfig().getInt("refresh-time"));
        }
        if(this.getConfig().getBoolean("ChatFormater")){
            pm.registerEvents(new Formater(this), this);
        }

        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("server_movement", () -> {
            return String.valueOf(this.getConfig().getBoolean("PowerNukkiX-movement-server"));
        }));
        metrics.addCustomChart(new Metrics.SimplePie("nukkit_version", () -> {
            return Server.getInstance().getNukkitVersion();
        }));

        metrics.addCustomChart(new Metrics.SimplePie("ostag_vs_chatformater", () -> {
            String info = "";
            Boolean ostag = this.getConfig().getBoolean("OsTag");
            Boolean chatFormater = this.getConfig().getBoolean("ChatFormater");
            if(ostag == true && chatFormater == true) {
                info = "OsTag and ChatFormater";
            }
            if(ostag == true && chatFormater == false){
                info = "OsTag";
            }
            if(ostag == false && chatFormater == true){
                info = "ChatFormater";
            }
            return info;
        }));
    }

    private void sendOnEnableInfo(){
        String ver = this.getDescription().getVersion();
        String aut = this.getDescription().getAuthors() + "";
        String verNuk = this.getServer().getNukkitVersion();
        String servVer = this.getServer().getVersion();
        String  apiVer =  this.getServer().getApiVersion();
        String ip = this.getServer().getIp();
        String port = this.getServer().getPort() + "";

        this.getLogger().info("§b-------------------------------");
        this.getLogger().info("§aOsTagPNX version:§3 " + ver );
        this.getLogger().info("§aPlugin by:§6 " + aut.replace("[" , "").replace("]", ""));
        this.getLogger().info("§aNukkit Version:§3 " + verNuk);
        this.getLogger().info("§aNukkit Api Version:§3 " + apiVer);
        this.getLogger().info("§aServer Version:§3 " + servVer);
        this.getLogger().info(" ");
        this.getLogger().info("§6Modules");
        this.getLogger().info("§aFormater§3: " + OtherUtils.getFormaterStatus(this));
        this.getLogger().info("§aOsTag§3: " + OtherUtils.getOsTagStatus(this));
        this.getLogger().info(" ");
        this.getLogger().info("§ePlugins§3");
        this.getLogger().info("§aDeathSkulls§3: " + OtherUtils.getDeathSkullsStatus(this));
        this.getLogger().info("§aLuckPerms§3: " + OtherUtils.getLuckPermStatus(this));
        this.getLogger().info(" ");
        this.getLogger().info("§b-------------------------------");
    }

}
