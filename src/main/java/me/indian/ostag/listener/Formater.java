package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import java.util.HashMap;
import java.util.UUID;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.ColorUtil;
import me.indian.ostag.util.OtherUtils;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.PlayerInfoUtil;

public class Formater implements Listener {

    private static final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final OsTag plugin;
    private final PlaceholderAPI api;
    private final int second = 1000;

    public Formater(final OsTag plugin, final PlaceholderAPI api) {
        this.plugin = plugin;
        this.api = api;
    }

    // IndianPL
    //Chat formater for Nukkit
    //https://github.com/IndianBartonka/LuckPermChatFormater

    @SuppressWarnings("unused")
    @EventHandler
    private void playerChatFormat(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        final Config config = this.plugin.getConfig();
        String msg = event.getMessage();
        String mess;
        final String cenzor = config.getString("censorship.word");
        //conzorship is a experimental option, maybe not good working
        for (final String blackList : config.getStringList("BlackWords")) {
            if (event.getMessage().toLowerCase().contains(blackList.toLowerCase())) {
                if (event.getMessage().toLowerCase().contains("Huje22".toLowerCase())) {
                    return;
                }
            }
            if (config.getBoolean("censorship.enable")) {
                if (!(player.isOp())) {
                    msg = event.getMessage().toLowerCase().replace(blackList.toLowerCase(), cenzor);
                }
                event.setMessage(msg);
            }
            if (player.hasPermission(Permissions.ADMIN) || player.hasPermission(Permissions.COLORS) || config.getBoolean("and-for-all")) {
                mess = ColorUtil.replaceColorCode(event.getMessage());
            } else {
                mess = event.getMessage();
            }
            event.setMessage(mess);
            String messageFormat = ColorUtil.replaceColorCode(config.getString("message-format"));
            if (this.plugin.papiAndKotlinLib) {
                messageFormat = this.api.translateString(ColorUtil.replaceColorCode(config.getString("message-format")), player);
            }
            event.setFormat(PlayerInfoUtil.replaceAllInfo(player, messageFormat
                    .replace("<msg>", event.getMessage())
                    .replace("\n", " this action not allowed here ")));
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    private void cooldownMessage(final PlayerChatEvent event) {
        //cooldown is a experimental option, maybe not good working
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Config config = this.plugin.getConfig();
        final long time = config.getLong("cooldown.delay") * this.second;

        if (!cooldown.containsKey(uuid) || System.currentTimeMillis() - cooldown.get(uuid) > time) {
            if (!player.isOp() || !player.hasPermission("ostag.admin")) {
                if (config.getBoolean("cooldown.enable")) {
                    cooldown.putIfAbsent(uuid, System.currentTimeMillis());
                }
            }
            if (config.getBoolean("break-between-messages.enable")) {
                Server.getInstance().getScheduler().scheduleDelayedTask(this.plugin, () -> OtherUtils.sendMessageToAll(" "), 1);
            }
        } else {
            final long cooldownTime = (time - (System.currentTimeMillis() - cooldown.get(uuid))) / this.second;
            event.setCancelled(true);
            if (config.getBoolean("break-between-messages.enable")) {
                player.sendMessage(" ");
            }

            String cooldownMessage = ColorUtil.replaceColorCode(config.getString("cooldown.message")
                    .replace("<left>", String.valueOf(cooldownTime)));
            if (this.plugin.papiAndKotlinLib) {
                cooldownMessage = this.api.translateString(ColorUtil.replaceColorCode(config.getString("cooldown.message")
                        .replace("<left>", String.valueOf(cooldownTime))), player);
            }
            player.sendMessage(cooldownMessage);
        }
    }

    public String cooldown(final Player player) {
        final Config config = this.plugin.getConfig();
        final UUID uuid = player.getUniqueId();
        final long time = this.plugin.getConfig().getLong("cooldown.delay") * this.second;
        long cooldownTime = 0;
        if (!config.getBoolean("cooldown.enable")) {
            return ColorUtil.replaceColorCode(config.getString("cooldown.disabled"));
        }
        if (cooldown.containsKey(uuid)) {
            cooldownTime = (time - (System.currentTimeMillis() - cooldown.get(uuid))) / this.second;
        }
        if (player.isOp() || player.hasPermission("ostag.admin")) {
            return ColorUtil.replaceColorCode(config.getString("cooldown.bypass"));
        }
        if (cooldownTime <= 0) {
            cooldown.remove(uuid);
            return ColorUtil.replaceColorCode(config.getString("cooldown.over"));
        }
        return String.valueOf(cooldownTime);
    }
}