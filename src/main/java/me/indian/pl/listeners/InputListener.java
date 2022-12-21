package me.indian.pl.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.InputMode;
import me.indian.pl.OsTag;

import java.util.HashMap;
import java.util.Map;

public class InputListener implements Listener {

    private final OsTag plugin;
    public static Map<Player, InputMode> controler = new HashMap<Player, InputMode>();

    public InputListener(OsTag plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void inputListener(DataPacketReceiveEvent e) {
        if (e.getPacket() instanceof PlayerAuthInputPacket) {
            InputMode inputMode = ((PlayerAuthInputPacket) e.getPacket()).getInputMode();
            Player player = e.getPlayer();
            //thanks to Petterim
            //https://github.com/PetteriM1
            controler.put(player, inputMode);
        }
    }

    @EventHandler
    public void removeControl(PlayerQuitEvent e) {
        Player p = (Player) e.getPlayer();
        controler.remove(p);
    }

    public static String getControler(Player p) {
        String control = controler.get(p) + "";
        return control;
    }



}

