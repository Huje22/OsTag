package me.indian.pl.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import me.indian.pl.OsTag;

import java.util.List;

public class OsTimer extends Task implements Runnable, Listener {
    private static OsTag plugin = OsTag.getInstance();

    @Override
    public void onRun(int i) {
        for (Player p : Server.getInstance().getOnlinePlayers().values()) {
            addOsTag(p);
//            OsTagAdd.addDevNormal(p);
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = (Player) e.getPlayer();
        addOsTag(p);
    }

    private void addOsTag(Player p) {

        Config conf = plugin.getConfig();

        List<String> playerlist = conf.getStringList("advanced-players");
        List<String> disabledWorld = conf.getStringList("disabled-worlds");

        for (String dis : disabledWorld) {
            if (p.getLevel().getName().equalsIgnoreCase(dis)) {
                //disabled worlds is a experimental option, maybe not good working
                return;
            }
        }
        if (playerlist.contains(p.getDisplayName())) {
            OsTagAdd.addDevAdvanced(p);
        } else {
            OsTagAdd.addDevNormal(p);
        }
    }

}
