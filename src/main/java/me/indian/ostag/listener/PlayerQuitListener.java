package me.indian.ostag.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.OsTimerStatus;

public class PlayerQuitListener implements Listener {

    private final OsTag plugin;

    public PlayerQuitListener(final OsTag plugin) {
        this.plugin = plugin;
    }


    @SuppressWarnings("unused")
    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        if ((this.plugin.getServer().getOnlinePlayers().size() - 1) == 0) {
            this.plugin.getOsTimer().setStatus(OsTimerStatus.STOPPED);
        }
    }
}