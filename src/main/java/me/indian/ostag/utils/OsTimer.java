package me.indian.ostag.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.scheduler.Task;
import me.indian.ostag.OsTag;

import java.util.List;

public class OsTimer extends Task implements Runnable, Listener {

    private static final OsTag plugin = OsTag.getInstance();

    @Override
    public void onRun(int i) {
        for (Player p : Server.getInstance().getOnlinePlayers().values()) {
            addOsTag(p);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        addOsTag(p);
    }

    private void addOsTag(Player p) {
        List<String> advancedPlayers = plugin.getConfig().getStringList("advanced-players");
        List<String> disabledWorld = plugin.getConfig().getStringList("disabled-worlds");
        for (String dis : disabledWorld) {
            if (p.getLevel().getName().equalsIgnoreCase(dis)) {
                //disabled worlds is a experimental option, maybe not good working
                return;
            }
        }
        if (advancedPlayers.contains(p.getDisplayName())) {
            OsTagAdd.addDevAdvanced(p);
        } else {
            OsTagAdd.addDevNormal(p);
        }
    }
}