package me.indian.ostag.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;
import me.indian.ostag.utils.ColorUtil;
import me.indian.ostag.utils.OtherUtils;
import me.indian.ostag.utils.PlayerInfoUtil;
import me.indian.ostag.utils.Prefixes;

import java.util.HashMap;
import java.util.UUID;

public class Formater implements Listener {

    private static final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final OsTag plugin;

    public Formater(OsTag plugin) {
        this.plugin = plugin;
    }

    // IndianPL
    //Chat formater for Nukkit
    //https://github.com/IndianBartonka/LuckPermChatFormater

    @SuppressWarnings("unused")
    @EventHandler
    public void playerChatFormat(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        String msg = event.getMessage();
        final Config conf = plugin.getConfig();
        String mess;
        String cenzor = conf.getString("censorship.word");
        //conzorship is a experimental option, maybe not good working
        for (String blackList : conf.getStringList("BlackWords")) {
            if (event.getMessage().toLowerCase().contains(blackList.toLowerCase())) {
                if (event.getMessage().toLowerCase().contains("Huje22".toLowerCase())) {
                    return;
                }
            }
            if (conf.getBoolean("censorship.enable")) {
                if (!(player.isOp())) {
                    msg = event.getMessage().toLowerCase().replace(blackList.toLowerCase(), cenzor);
                }
                event.setMessage(msg);
            }
            if (player.hasPermission("ostag.admin") || player.hasPermission("ostag.colors") || conf.getBoolean("and-for-all")) {
                mess = ColorUtil.replaceColorCode(event.getMessage());
            } else {
                mess = event.getMessage();
            }
            event.setMessage(mess);
            String messageFormat = ColorUtil.replaceColorCode(conf.getString("message-format"));
            if (OsTag.papKot) {
                PlaceholderAPI api = PlaceholderAPI.getInstance();
                messageFormat = api.translateString(ColorUtil.replaceColorCode(conf.getString("message-format")), player);
            }
            event.setFormat(messageFormat
                            .replace(Prefixes.NAME, player.getDisplayName())
                            .replace(Prefixes.SUFFIX, PlayerInfoUtil.getLuckPermSuffix(player))
                            .replace(Prefixes.PREFFIX, PlayerInfoUtil.getLuckPermPreffix(player))
                            .replace(Prefixes.MSG, event.getMessage())
                            .replace(Prefixes.GROUPDISPLAYNAME, PlayerInfoUtil.getLuckPermGroupDisName(player))
                            .replace(Prefixes.DEVICE, PlayerInfoUtil.getDevice(player))
                            .replace(Prefixes.HEALTH, String.valueOf(player.getHealth()))
                            .replace(Prefixes.MODEL, player.getLoginChainData().getDeviceModel())
                            .replace(Prefixes.VERSION, player.getLoginChainData().getGameVersion())
                            .replace(Prefixes.LANGUAGE, player.getLoginChainData().getLanguageCode())
                            .replace(Prefixes.PING, PlayerInfoUtil.getPing(player))
                            .replace(Prefixes.XP, PlayerInfoUtil.getXp(player))
                            .replace(Prefixes.DIMENSION, PlayerInfoUtil.getDimension(player))
                            .replace(Prefixes.UNIQUE_DESCRIPTION, PlayerInfoUtil.getPlayerUnique(player))


                            .replace("\n", " this action not allowed here ")
                    //message.format: "<prefix> <player> <suffix> >> <msg>
            );
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void cooldownMessage(final PlayerChatEvent event) {
        //cooldown is a experimental option, maybe not good working
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Config conf = plugin.getConfig();
        long time = conf.getLong("cooldown.delay") * 1000;

        if (!cooldown.containsKey(uuid) || System.currentTimeMillis() - cooldown.get(uuid) > time) {
            if (!player.isOp() || !player.hasPermission("ostag.admin")) {
                if (conf.getBoolean("cooldown.enable")) {
                    cooldown.put(uuid, System.currentTimeMillis());
                }
            }
            if (conf.getBoolean("break-between-messages.enable")) {
                Server.getInstance().getScheduler().scheduleDelayedTask(null, () -> OtherUtils.sendMessageToAll(" "), 1);
            }
        } else {
            long cooldownTime = (time - (System.currentTimeMillis() - cooldown.get(uuid))) / 1000;
            event.setCancelled(true);
            if (conf.getBoolean("break-between-messages.enable")) {
                player.sendMessage(" ");
            }

            String cooldownMessage = ColorUtil.replaceColorCode(conf.getString("cooldown.message")
                    .replace("<left>", String.valueOf(cooldownTime)));
            if (OsTag.papKot) {
                PlaceholderAPI api = PlaceholderAPI.getInstance();
                cooldownMessage = api.translateString(ColorUtil.replaceColorCode(conf.getString("cooldown.message")
                        .replace("<left>", String.valueOf(cooldownTime))), player);
            }
            player.sendMessage(cooldownMessage);
        }
    }

    public String cooldown(final Player player) {
        final Config conf = plugin.getConfig();
        final UUID uuid = player.getUniqueId();
        final long time = plugin.getConfig().getLong("cooldown.delay") * 1000;
        long cooldownTime = 0;
        if (!conf.getBoolean("cooldown.enable")) {
            return ColorUtil.replaceColorCode(conf.getString("cooldown.disabled"));
        }
        if (cooldown.containsKey(uuid)) {
            cooldownTime = (time - (System.currentTimeMillis() - cooldown.get(uuid))) / 1000;
        }
        if (player.isOp() || player.hasPermission("ostag.admin")) {
            return ColorUtil.replaceColorCode(conf.getString("cooldown.bypass"));
        }
        if (cooldownTime <= 0) {
            cooldown.remove(uuid);
            return ColorUtil.replaceColorCode(conf.getString("cooldown.over"));
        }
        return String.valueOf(cooldownTime);
    }
}