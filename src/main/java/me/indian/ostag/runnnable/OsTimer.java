package me.indian.ostag.runnnable;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.TagAddUtil;
import me.indian.ostag.util.ThreadUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OsTimer extends Task implements Runnable {


    private final OsTag plugin;
    private final Config config;
    private final ExecutorService executorService;
    private final PluginLogger logger;
    private final String debugPrefix;
    private Status status = Status.STOPPED;
    private boolean sented = false;

    public OsTimer() {
        this.plugin = OsTag.getInstance();
        this.config = plugin.getConfig();
        this.executorService = Executors.newSingleThreadExecutor(new ThreadUtil("Ostag OsTimer Thread"));
        this.logger = this.plugin.getLogger();
        this.debugPrefix = MessageUtil.colorize(this.plugin.publicDebugPrefix + "&8[&dOsTimer&8] ");
    }

    @Override
    public void onRun(final int i) {
        if (this.getStatus() == Status.STOPPED) {
            if (this.plugin.debug) {
                this.logger.info(debugPrefix + "Stopping timer...");
            }
            try {
                cancel();
                if (this.plugin.debug) {
                    this.logger.info(debugPrefix + "Timer stoped");
                }
            } catch (final Exception e) {
                if (this.plugin.debug) {
                    this.logger.error(debugPrefix + "Can't stop timer!");
                    e.printStackTrace();
                }
            }
            return;
        }
        executorService.execute(() -> {
            for (final Player all : Server.getInstance().getOnlinePlayers().values()) {
                this.addOsTag(all);
            }
        });
    }

    private void addOsTag(final Player player) {
        final List<String> advancedPlayers = config.getStringList("advanced-players");
        final List<String> disabledWorlds = config.getStringList("disabled-worlds");

        for (final String dis : disabledWorlds) {
            if (player.getLevel().getName().equalsIgnoreCase(dis)) {
                //disabled worlds is a experimental option, maybe not good working
                return;
            }
        }
        if (advancedPlayers.contains(player.getDisplayName())) {
            TagAddUtil.addDevAdvanced(player);
        } else {
            TagAddUtil.addDevNormal(player);
        }
    }

    public void runTimer() {
        int refreshTime = this.config.getInt("refresh-time");
        if (refreshTime <= 0) {
            refreshTime = 1;
            this.config.set("refresh-time", 1);
            this.config.save();
            this.plugin.getLogger().warning(MessageUtil.colorize("&cRefresh time must be higer than &b0 &c,we will set it up for you!"));
        }
        if (!(this.getStatus() == Status.RUNNING)) {
            if (this.plugin.debug) {
                this.logger.info(debugPrefix + "Enabling timer...");
            }
            try {
                this.plugin.getServer().getScheduler().scheduleRepeatingTask(this, 20 * refreshTime);
                if (this.plugin.debug) {
                    this.logger.info(debugPrefix + "Timer enabled");
                }
                this.setStatus(Status.RUNNING);
            } catch (final Exception e) {
                if (this.plugin.debug) {
                    this.logger.error(debugPrefix + "Can't enable timer!");
                    e.printStackTrace();
                }
            }
        } else {
            throw new RuntimeException("OsTimer already running");
        }
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(final Status status) {
        if (!this.plugin.osTag) {
            if (!sented) {
                sented = true;
                this.logger.info(MessageUtil.colorize("&aCan't set status , ostag module is disabled"));
            }
        } else {
            this.status = status;
        }
    }

    public enum Status {
        RUNNING,
        STOPPED
    }
}