package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CpsListener implements Listener {

    private static final HashMap<Player, List<Long>> cps = new HashMap<>();

    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permissions from author
    @SuppressWarnings("unused")
    @EventHandler
    public void onPacket(final DataPacketReceiveEvent event) {
        if (!(event.getPacket() instanceof LevelSoundEventPacket)) return;
        LevelSoundEventPacket packet = (LevelSoundEventPacket) event.getPacket();
        if (packet.sound != LevelSoundEventPacket.SOUND_ATTACK && packet.sound != LevelSoundEventPacket.SOUND_ATTACK_NODAMAGE &&
                packet.sound != LevelSoundEventPacket.SOUND_ATTACK_STRONG) return;

        List<Long> cpsList = cps.get(event.getPlayer());
        if (cpsList == null) {
            cpsList = new ArrayList<>();
        }
        cpsList.add(System.currentTimeMillis());
        cps.remove(event.getPlayer());
        cps.put(event.getPlayer(), cpsList);
    }

    public static int getCPS(Player player) {
        final List<Long> list = CpsListener.cps.get(player);
        if (list == null) {
            return 0;
        }
        list.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        return list.size();
    }

    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permisions from author
    @SuppressWarnings("unused")
    @EventHandler
    public void removeCps(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        cps.remove(player);
    }
}