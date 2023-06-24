package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;


public class CpsLimiter implements Listener {

    /*
    Code by IndianBartonka
    From: https://github.com/GommeAWM/CPSCounter/commit/78069051c10d3c6770d523edb48ec1d1a78cdabb
    With Using https://github.com/GommeAWM/CPSCounter project with permissions (https://cloudburstmc.org/attachments/1668275979165-png.3853/)
     */


    private final OsTag plugin;

    public CpsLimiter(final OsTag plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void damageEvent(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();
            final Config config = this.plugin.getConfig();
            final int maxCps = config.getInt("Cps.max");
            final String message = config.getString("Cps.message");

            if (CpsListener.getCPS(player) > maxCps) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.colorize(message)
                        .replace("<maxCps>", String.valueOf(maxCps))
                        .replace("<cps>", String.valueOf(CpsListener.getCPS(player)))
                );
            }
        }
    }
}