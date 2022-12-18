package me.indian.pl.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.InputMode;
import me.indian.pl.OsTag;

import java.util.*;

public class InputListener implements Listener {

    private final OsTag plugin;
    public static Map<Player, InputMode> controler = new HashMap<Player, InputMode>();
    private static final SplittableRandom random = new SplittableRandom();
    private static HashMap<Player, List<Long>> cps =  new HashMap<Player, List<Long>>();
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

    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permisions from author

    @EventHandler
    public void onPacket(DataPacketReceiveEvent event) {
        if (!(event.getPacket() instanceof LevelSoundEventPacket)) {
            return;
        }
        LevelSoundEventPacket packet = (LevelSoundEventPacket) event.getPacket();
        if (packet.sound != 41 && packet.sound != 42 && packet.sound != 43) {
            return;
        }
        List<Long> cpsList = this.cps.get(event.getPlayer());
        if (cpsList == null) {
            cpsList = new ArrayList<Long>();
        }
        cpsList.add(System.currentTimeMillis());
        this.cps.remove(event.getPlayer());
        this.cps.put(event.getPlayer(), cpsList);
//        event.getPlayer().sendActionBar(String.valueOf(this.getCPS(event.getPlayer())));
    }

    public static int getCPS(Player player) {
        final List<Long> list = InputListener.cps.get(player);
        if (list == null) {
            return 0;
        }
        list.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        return list.size();
    }

    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permisions from author

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

