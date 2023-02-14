package me.indian.ostag.listeners;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;
import me.indian.ostag.utils.ColorUtil;
import me.indian.ostag.utils.OtherUtils;
import me.indian.ostag.utils.PlayerInfoUtil;

import java.util.HashMap;
import java.util.UUID;

public class Formater implements Listener {

    private static final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final OtherUtils otherUtils = new OtherUtils();
    private final OsTag plugin;

    public Formater(OsTag plugin) {
        this.plugin = plugin;
    }

    // IndianPL
    //Chat formater for Nukkit
    //https://github.com/IndianBartonka/LuckPermChatFormater

    @SuppressWarnings("unused")
    @EventHandler
    public void playerChatFormat(PlayerChatEvent event) {
        final Player p = event.getPlayer();
        String msg = event.getMessage();
        final Config conf = plugin.getConfig();
        String mess;
        String cenzor = plugin.getConfig().getString("censorship.word");
        //conzorship is a experimental option, maybe not good working
        for (String czarnalista : plugin.getConfig().getStringList("BlackWords")) {
            if (event.getMessage().toLowerCase().contains(czarnalista.toLowerCase())) {
                if (event.getMessage().toLowerCase().contains("Huje22".toLowerCase())) {
                    return;
                }
            }
            if (plugin.getConfig().getBoolean("censorship.enable")) {
                if (!(p.isOp())) {
                    msg = event.getMessage().toLowerCase().replace(czarnalista.toLowerCase(), cenzor);
                }
                event.setMessage(msg);
            }
            if (p.hasPermission("ostag.admin") || p.hasPermission("ostag.colors") || conf.getBoolean("and-for-all")) {
                mess = TextFormat.colorize('&', event.getMessage());
            } else {
                mess = event.getMessage();
            }
            event.setMessage(mess);
            String messageformat = ColorUtil.replaceColorCode(plugin.getConfig().getString("message-format"));
            if (OsTag.papKot) {
                PlaceholderAPI api = PlaceholderAPI.getInstance();
                messageformat = api.translateString(ColorUtil.replaceColorCode(conf.getString("message-format")), p);
            }
            event.setFormat(messageformat
                            .replace("<name>", p.getDisplayName())
                            .replace("<suffix>", PlayerInfoUtil.getLuckPermSufix(p))
                            .replace("<prefix>", PlayerInfoUtil.getLuckPermPrefix(p))
                            .replace("<msg>", event.getMessage())
                            .replace("<groupDisName>", PlayerInfoUtil.getLuckPermGroupDisName(p))
                            .replace("<device>", PlayerInfoUtil.getDevice(p))
                            .replace("<health>", p.getHealth() + "")
                            .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                            .replace("<version>", p.getLoginChainData().getGameVersion())
                            .replace("<language>", p.getLoginChainData().getLanguageCode())
                            .replace("<ping>", PlayerInfoUtil.getPing(p))
                            .replace("<xp>", PlayerInfoUtil.getXp(p))
                            .replace("<dimension>", PlayerInfoUtil.getDimension(p))
                            .replace("<unique-description>", PlayerInfoUtil.getPlayerUnique(p))


                            .replace("\n", " this action not allowed here ")
                    //message.format: "<prefix> <player> <suffix> >> <msg>
            );
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void cooldownMessage(PlayerChatEvent event) {
        //cooldown is a experimental option, maybe not good working
        final Player player = event.getPlayer();
        final Config conf = plugin.getConfig();
        long time = conf.getLong("cooldown.delay") * 1000;

        if (!cooldown.containsKey(player.getUniqueId()) || System.currentTimeMillis() - cooldown.get(player.getUniqueId()) > time) {
            if (!(player.isOp() || !player.hasPermission("ostag.admin"))) {
                if (conf.getBoolean("cooldown.enable")) {
                    cooldown.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
            if (conf.getBoolean("break-between-messages.enable")) {
                Server.getInstance().getScheduler().scheduleDelayedTask(null, () -> otherUtils.sendMessageToAll(" "), 1);
            }
        } else {
            long cooldownTime = (time - (System.currentTimeMillis() - cooldown.get(player.getUniqueId()))) / 1000;
            event.setCancelled(true);
            if (conf.getBoolean("break-between-messages.enable")) {
                player.sendMessage(" ");
            }
            player.sendMessage(ColorUtil.replaceColorCode(conf.getString("cooldown.message")
                    .replace("<left>", cooldownTime + "")));
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void removeFromMap(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (cooldown.containsKey(player.getUniqueId())) {
            cooldown.remove(player.getUniqueId());
        }
    }

    public String cooldown(Player player) {
        final UUID uuid = player.getUniqueId();
        long time = plugin.getConfig().getLong("cooldown.delay") * 1000;
        long cooldownTime = 0;
        if (cooldown.containsKey(uuid)) {
            cooldownTime = (time - (System.currentTimeMillis() - cooldown.get(uuid))) / 1000;
        }
        if(player.isOp() || player.hasPermission("ostag.admin")){
            return ColorUtil.replaceColorCode(plugin.getConfig().getString("cooldown.bypass"));
        }
        if (cooldownTime <= 0) {
            cooldown.remove(uuid);
            return ColorUtil.replaceColorCode(plugin.getConfig().getString("cooldown.over"));
        }
        return String.valueOf(cooldownTime);
    }
}