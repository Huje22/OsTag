package me.indian.ostag.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import me.indian.ostag.OsTag;
import me.indian.ostag.runnnable.OsTimer;

public class PlayerQuitListener implements Listener {

    private final OsTag plugin;

    public PlayerQuitListener(final OsTag plugin) {
        this.plugin = plugin;
    }


    @SuppressWarnings("unused")
    @EventHandler
    private void onPlayerQuit(final PlayerQuitEvent event) {
        final int players = plugin.getServer().getOnlinePlayers().size() - 1;
        if (players == 0) {
            plugin.getOsTimer().setStatus(OsTimer.Status.STOPPED);
        }
    }
}