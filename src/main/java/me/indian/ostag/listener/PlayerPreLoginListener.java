package me.indian.ostag.listener;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.Status;

public class PlayerPreLoginListener implements Listener {
    private final OsTag plugin;

    public PlayerPreLoginListener(final OsTag plugin) {
        this.plugin = plugin;
    }


    @SuppressWarnings("unused")
    @EventHandler
    private void onPlayerPreLogin(final PlayerPreLoginEvent event) {
        if (this.plugin.getOsTimer().getStatus() == Status.STOPPED) {
            this.plugin.getOsTimer().startTimer();
        }
    }
}