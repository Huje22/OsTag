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

public class OsTag extends PluginBase implements Listener {

    private static int pluginId = 16838;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().warning("You dont have lucky perms , ChatFormating dont corectly work");
        } else {

        }

        saveDefaultConfig();

        new OsTagAdd(this);


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
}
