package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerSettingsConfig;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.Permissions;

public class PlayerJoinListener implements Listener {

    private final OsTag plugin;
    private final PlayerSettingsConfig playerSettingsConfig;

    public PlayerJoinListener(final OsTag plugin) {
        this.plugin = plugin;
        this.playerSettingsConfig = this.plugin.getPlayerSettingsConfig();
    }


    @SuppressWarnings("unused")
    @EventHandler
    private void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String compatibility = GithubUtil.checkTagCompatibility();

        this.playerSettingsConfig.createPlayerSection(player);
        this.playerSettingsConfig.setLastPlayed(player.getName(), player.getLastPlayed());

        if (player.hasPermission(Permissions.ADMIN) || player.isOp()) {
            if (this.plugin.upDatechecker) {
                player.sendMessage(this.plugin.pluginPrefix + compatibility);
            }
        }
    }
}
