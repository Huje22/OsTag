package me.indian.ostag.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.InputMode;

import java.util.HashMap;
import java.util.Map;

public class InputListener implements Listener {

    public static final Map<Player, InputMode> controler = new HashMap<>();

    @SuppressWarnings("unused")
    @EventHandler
    public void inputListener(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        if (packet instanceof PlayerAuthInputPacket) {
            final InputMode inputMode = ((PlayerAuthInputPacket) packet).getInputMode();
            final Player player = event.getPlayer();
            //thanks to Petterim
            //https://github.com/PetteriM1
            controler.put(player, inputMode);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void removeControl(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        controler.remove(player);
    }

    public static String getControler(Player player) {
        return controler.get(player) + "";
    }
}