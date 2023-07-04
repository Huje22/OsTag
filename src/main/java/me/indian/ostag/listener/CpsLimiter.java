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


    private final Config config;

    public CpsLimiter(final OsTag plugin) {
        this.config = plugin.getConfig();
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void damageEvent(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();

            if (CpsListener.getCPS(player) > this.getMaxCps()) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.colorize(this.getMessage())
                        .replace("<maxCps>", String.valueOf(this.getMaxCps()))
                        .replace("<cps>", String.valueOf(CpsListener.getCPS(player)))
                );
            }
        }
    }

    public String getMessage() {
        return this.config.getString("Cps.message");
    }

    public void setMessage(final String message) {
        this.config.set("Cps.message", message);
        this.config.save();
    }

    public int getMaxCps() {
        return this.config.getInt("Cps.max", 10);
    }

    public void setMaxCps(final int cps) {
        this.config.set("Cps.max", cps);
        this.config.save();
    }
}