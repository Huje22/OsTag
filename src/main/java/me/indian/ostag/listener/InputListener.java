package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.types.InputMode;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.scheduler.NukkitRunnable;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.ThreadUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InputListener implements Listener {

    private final OsTag plugin;
    private final PluginLogger logger;
    private final Map<String, InputMode> controller;
    private final String debugPrefix;
    private final ExecutorService executorService;

    public InputListener(final OsTag plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
        this.controller = new HashMap<>();
        this.debugPrefix = MessageUtil.colorize(this.plugin.publicDebugPrefix + "&8[&dInputListener&8] ");
        this.executorService = Executors.newSingleThreadExecutor(new ThreadUtil("Ostag InputListener Thread"));
    }


    @SuppressWarnings("unused")
    @EventHandler
    private void inputListener(final DataPacketReceiveEvent event) {
        this.executorService.execute(() -> {
            final DataPacket packet = event.getPacket();
            if (packet instanceof PlayerAuthInputPacket) {
                final InputMode inputMode = ((PlayerAuthInputPacket) packet).getInputMode();
                final String name = event.getPlayer().getName();
                //thanks to Petterim
                //https://github.com/PetteriM1

                if (!controller.containsKey(name)) {
                    controller.put(name, inputMode);
                    if (plugin.debug) {
                        logger.info(MessageUtil.colorize(this.debugPrefix + "&aPlayer: &6" + name + " &ahas been added to controller list with &3" + inputMode));
                    }
                    return;
                }
                if (controller.get(name) != inputMode) {
                    controller.put(name, inputMode);
                    if (plugin.debug) {
                        logger.info(MessageUtil.colorize(this.debugPrefix + "&aPlayer: &6" + name + " &achanged controller to: &3" + inputMode));
                    }
                }
            }
        });
    }

    @SuppressWarnings("unused")
    @EventHandler
    private void removeControl(final PlayerQuitEvent event) {
        this.timeRemove(event.getPlayer().getName());
    }

    public String getController(final Player player) {
        return String.valueOf(controller.get(player.getName()));
    }

    private void timeRemove(final String name) {
        this.executorService.execute(() -> new NukkitRunnable() {
            @Override
            public void run() {
                final Player player = plugin.getServer().getPlayer(name);
                if (player == null) {
                    controller.remove(name);
                    if (plugin.debug) {
                        logger.info(MessageUtil.colorize(InputListener.this.debugPrefix + "&aPlayer &6" + name + "&a has been removed from the map"));
                    }
                }
            }
        }.runTaskLater(plugin, 20 * 30));
    }
}
