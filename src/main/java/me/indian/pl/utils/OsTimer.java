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
    private final OsTag plugin;

    public OsTimer(OsTag plugin) {
        this.plugin = plugin;
    }


    @Override
    public void onRun(int i) {
        List<String> playerlist = plugin.getConfig().getStringList("advanced-players");
        Config conf = plugin.getConfig();
        for (Player p : Server.getInstance().getOnlinePlayers().values()) {
            if (playerlist.contains(p.getDisplayName())) {
                if (conf.getBoolean("ScoreTagOnly")) {
                    OsTagAdd.scoreTagAdvancd(p);
                } else {
                    p.setScoreTag("");
                    OsTagAdd.addDevAdvanced(p);
                }
            } else {
                if (conf.getBoolean("ScoreTagOnly")) {
                    OsTagAdd.scoreTagNormal(p);
                } else {
                    p.setScoreTag("");
                    OsTagAdd.addDevNormal(p);
                }
            }
        }
    }


    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        Player p = (Player) e.getPlayer();
        Config conf = plugin.getConfig();
        List<String> playerlist = plugin.getConfig().getStringList("advanced-players");
        if (playerlist.contains(p.getDisplayName())) {
            if (conf.getBoolean("ScoreTagOnly")) {
                OsTagAdd.scoreTagAdvancd(p);
            } else {
                OsTagAdd.addDevAdvanced(p);
            }
        } else {
            if (conf.getBoolean("ScoreTagOnly")) {
                OsTagAdd.addDevNormal(p);
            } else {
                OsTagAdd.addDevNormal(p);
            }
        }
    }

}
