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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.ColorUtil;

public class InputListener implements Listener {

    private static final OsTag plugin = OsTag.getInstance();
    private static final PluginLogger logger = plugin.getLogger();
    private final String debugPrefix = ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&8[&dInputListener&8] ");
    public static final Map<String, InputMode> controller = new HashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SuppressWarnings("unused")
    @EventHandler
    public void inputListener(DataPacketReceiveEvent event) {
        DataPacket packet = event.getPacket();
        if (packet instanceof PlayerAuthInputPacket) {
            final InputMode inputMode = ((PlayerAuthInputPacket) packet).getInputMode();
            final Player player = event.getPlayer();
            final String name = player.getName();
            //thanks to Petterim
            //https://github.com/PetteriM1
            executorService.execute(() -> {
                if (!controller.containsKey(name)) {
                    controller.put(name, inputMode);
                    if (plugin.debug) {
                        logger.info(ColorUtil.replaceColorCode(debugPrefix + "&aPlayer: &6" + player.getName() + " &ahas been added to controller list with &3" + inputMode));
                    }
                    return;
                }
                if (controller.get(name) != inputMode) {
                    controller.put(name, inputMode);
                    if (plugin.debug) {
                        logger.info(ColorUtil.replaceColorCode(debugPrefix + "&aPlayer: &6" + player.getName() + " &achanged controller to: &3" + inputMode));
                    }
                }
            });
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void removeControl(PlayerQuitEvent event) {
        final String name = event.getPlayer().getName();
        timeRemove(name);
    }

    public static String getController(Player player) {
        return String.valueOf(controller.get(player.getName()));
    }

    private void timeRemove(String name) {
        executorService.execute(() -> {
            new NukkitRunnable() {
                @Override
                public void run() {
                    final Player player = plugin.getServer().getPlayer(name);
                    if (player == null) {
                        controller.remove(name);
                        if (plugin.debug) {
                            logger.info(ColorUtil.replaceColorCode(debugPrefix + "&aPlayer &6" + name + "&a has been removed from the map"));
                        }
                    }
                }
            }.runTaskLater(plugin, 20 * 20);
        });
    }
}
