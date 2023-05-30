package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.NukkitRunnable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.ColorUtil;

public class CpsListener implements Listener {

    private static final HashMap<String, List<Long>> cps = new HashMap<>();
    private static final OsTag plugin = OsTag.getInstance();
    private static final PluginLogger logger = plugin.getLogger();
    private final String debugPrefix = ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&8[&dCpsListener&8] ");

    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permissions from author edited by IndianPL and Neziw

    public static int getCPS(final Player player) {
        final List<Long> list = cps.get(player.getName());
        if (list == null) {
            return 0;
        }
        list.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        return list.size();
    }

    @SuppressWarnings("unused")
    @EventHandler
    private void onPacket(final DataPacketReceiveEvent event) {
        if (event.getPacket() instanceof LevelSoundEventPacket) {
            final Player player = event.getPlayer();
            final LevelSoundEventPacket packet = (LevelSoundEventPacket) event.getPacket();
            if (packet.sound != LevelSoundEventPacket.SOUND_ATTACK && packet.sound != LevelSoundEventPacket.SOUND_ATTACK_NODAMAGE &&
                    packet.sound != LevelSoundEventPacket.SOUND_ATTACK_STRONG) return;

            List<Long> cpsList = cps.get(player.getName());
            if (cpsList == null) {
                cpsList = new ArrayList<>();
            }
            cpsList.add(System.currentTimeMillis());
            cps.remove(player.getName());
            cps.put(player.getName(), cpsList);
        }
    }

    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permisions from author edited by IndianPL and Neziw

    @SuppressWarnings("unused")
    @EventHandler
    private void removeCps(final PlayerQuitEvent event) {
        final String name = event.getPlayer().getName();
        this.timeRemove(name);
    }

    private void timeRemove(final String name) {
        new NukkitRunnable() {
            @Override
            public void run() {
                final Player player = plugin.getServer().getPlayer(name);
                if (player == null) {
                    cps.remove(name);
                    if (plugin.debug) {
                        logger.info(ColorUtil.replaceColorCode(CpsListener.this.debugPrefix + "&aPlayer &6" + name + "&a has been removed from the map"));
                    }
                }
            }
        }.runTaskLater(plugin, 20 * 30);
    }
}