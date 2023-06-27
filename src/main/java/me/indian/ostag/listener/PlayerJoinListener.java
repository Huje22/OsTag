package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.Permissions;

public class PlayerJoinListener implements Listener {

    private final OsTag plugin;

    public PlayerJoinListener(final OsTag plugin) {
        this.plugin = plugin;
    }


    @SuppressWarnings("unused")
    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String compatibility = GithubUtil.checkTagCompatibility();

        this.plugin.getPlayersMentionConfig().createPlayerSection(player);

        if (player.hasPermission(Permissions.ADMIN) || player.isOp()) {
            if (this.plugin.upDatechecker) {
                player.sendMessage(this.plugin.pluginPrefix + compatibility);
            }
        }
    }
}
