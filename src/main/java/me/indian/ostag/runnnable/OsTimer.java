package me.indian.ostag.runnnable;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.TagAddUtil;
import me.indian.ostag.util.ThreadUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OsTimer extends Task implements Runnable {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadUtil("Ostag OsTimer Thread"));

    @Override
    public void onRun(final int i) {
        executorService.execute(() -> {
            for (final Player all : Server.getInstance().getOnlinePlayers().values()) {
                this.addOsTag(all);
            }
        });
    }

    private void addOsTag(final Player player) {
        final List<String> advancedPlayers = config.getStringList("advanced-players");
        final List<String> disabledWorlds = config.getStringList("disabled-worlds");

        for (final String dis : disabledWorlds) {
            if (player.getLevel().getName().equalsIgnoreCase(dis)) {
                //disabled worlds is a experimental option, maybe not good working
                return;
            }
        }
        if (advancedPlayers.contains(player.getDisplayName())) {
            TagAddUtil.addDevAdvanced(player);
        } else {
            TagAddUtil.addDevNormal(player);
        }
    }
}