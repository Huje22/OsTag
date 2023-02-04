package me.indian.ostag.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.InputMode;

import java.util.HashMap;
import java.util.Map;

public class InputListener implements Listener {

    public static Map<Player, InputMode> controler = new HashMap<>();

    @SuppressWarnings("unused")
    @EventHandler
    public void inputListener(DataPacketReceiveEvent e) {
        if (e.getPacket() instanceof PlayerAuthInputPacket) {
            InputMode inputMode = ((PlayerAuthInputPacket) e.getPacket()).getInputMode();
            final Player player = e.getPlayer();
            //thanks to Petterim
            //https://github.com/PetteriM1
            controler.put(player, inputMode);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void removeControl(PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        controler.remove(p);
    }

    public static String getControler(Player p) {
        return controler.get(p) + "";
    }

}