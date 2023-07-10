package me.indian.ostag.runnable;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.OsTimerStatus;
import me.indian.ostag.util.TagAddUtil;
import me.indian.ostag.util.ThreadUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OsTimer {

    private final OsTag plugin;
    private final Config config;
    private final ExecutorService executorService;
    private final PluginLogger logger;
    private final String debugPrefix;
    private OsTimerStatus status;
    private boolean sented = false;
    private CommandSender sender;

    public OsTimer(final OsTag plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.executorService = Executors.newSingleThreadExecutor(new ThreadUtil("OsTimer Thread"));
        this.logger = this.plugin.getLogger();
        this.debugPrefix = MessageUtil.colorize(this.plugin.publicDebugPrefix + "&8[&dOsTimer&8] ");
    }


    private void runTimer(final int i) {
        executorService.execute(() -> new NukkitRunnable() {
            @Override
            public void run() {
                if (getStatus() == OsTimerStatus.RESTART) {
                    if (plugin.debug) {
                        logger.info(debugPrefix + "Stopping timer...");
                    }
                    cancel();
                    startTimer();
                    if (plugin.debug) {
                        logger.info(debugPrefix + "Timer restarted");
                    }
                    return;
                }

                if (getStatus() == OsTimerStatus.DISABLED) {
                    cancel();
                    return;
                }
                if (!plugin.osTag) {
                    setStatus(OsTimerStatus.DISABLED);
                    cancel();
                    return;
                }
                if (getStatus() == OsTimerStatus.STOPPED) {
                    if (plugin.debug) {
                        logger.info(debugPrefix + "Stopping timer...");
                    }
                    cancel();
                    if (plugin.debug) {
                        logger.info(debugPrefix + "Timer stoped");
                    }
                    return;
                }
                for (final Player all : plugin.getServer().getOnlinePlayers().values()) {
                    addOsTag(all);
                }
            }
        }.runTaskTimer(plugin, i, i));
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

    public void startTimer() {
        if (this.getStatus() == OsTimerStatus.RUNNING) {
            throw new RuntimeException("OsTimer already running");
        }
        if (this.getStatus() == OsTimerStatus.STOPPED || this.getStatus() == OsTimerStatus.RESTART) {
            if (this.plugin.debug) {
                this.logger.info(debugPrefix + "Enabling timer...");
            }
            this.runTimer(getRefreshTime());
            if (this.plugin.debug) {
                this.logger.info(debugPrefix + "Timer enabled");
            }
            this.setStatus(OsTimerStatus.RUNNING);
        }
    }

    public OsTimerStatus getStatus() {
        return this.status;
    }

    public void setStatus(final OsTimerStatus status) {
        if (!this.plugin.osTag || this.getStatus() == OsTimerStatus.DISABLED) {
            if (!sented) {
                sented = true;
                final String cantSet = MessageUtil.colorize("&aCan't set status , ostag module or status is disabled");
                this.logger.info(cantSet);
                if (sender instanceof Player) {
                    sender.sendMessage(cantSet);
                }
                if (this.getStatus() != OsTimerStatus.DISABLED) this.setStatus(OsTimerStatus.DISABLED);
            }
        } else {
            this.status = status;
            if (this.plugin.debug) {
                final String statusSeted = MessageUtil.colorize(debugPrefix + "&aStatus seted to&b " + status);
                this.logger.info(statusSeted);
                if (sender instanceof Player) {
                    sender.sendMessage(statusSeted);
                }
            }
        }
    }

    public void setSender(final CommandSender sender) {
        this.sender = sender;
    }

    public int getRefreshTime() {
        int refreshTime = this.config.getInt("refresh-time", 1);
        if (refreshTime <= 0) {
            refreshTime = 1;
            this.config.set("refresh-time", 1);
            this.config.save();
            this.plugin.getLogger().warning(MessageUtil.colorize("&cRefresh time must be higer than &b0 &c,we will set it up for you!"));
        }
        return refreshTime;
    }
}