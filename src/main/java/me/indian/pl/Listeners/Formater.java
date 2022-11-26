package me.indian.pl.Listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import me.indian.pl.OsTag;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.util.HashMap;
import java.util.UUID;

import static me.indian.pl.Utils.PlayerInfoUtil.*;

public class Formater implements Listener {
    private HashMap<UUID, Long> cooldown = new HashMap<>();
    private final OsTag plugin;
    public Formater(OsTag plugin){
        this.plugin = plugin;
    }

    // IndianPL
    //Chat formater for Nukkit
    //https://github.com/IndianBartonka/LuckPermChatFormater


    @EventHandler
    public void playerChatFormat(PlayerChatEvent e) {
        Player p =  e.getPlayer();
        String msg = e.getMessage();
        Config conf = plugin.getConfig();


        String cenzura = plugin.getConfig().getString("censorship");

        for (String czarnalista : plugin.getConfig().getStringList("BlackWords")) {
            if (e.getMessage().toLowerCase().contains(czarnalista.toLowerCase())) {
                if (e.getMessage().toLowerCase().contains("Huje22".toLowerCase())) {
                    return;
                }
            }
            if(plugin.getConfig().getBoolean("enable-censorship")) {
                if (!(p.isOp())) {
                    msg = e.getMessage().toLowerCase().replace(czarnalista.toLowerCase(), cenzura);
                    e.setMessage(msg);
                }
            }

            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getPlayerAdapter(Player.class).getUser(p);



            e.setFormat(plugin.getConfig().getString("message.format")
                            .replace("<name>", p.getName())
                            .replace("<suffix>" ,getLuckPermSufix(p, plugin) )
                            .replace("<prefix>" ,  getLuckPermPrefix(p, plugin))
                            .replace("<msg>", msg)
                            .replace("<groupDisName>" , getGroupDisName(p , plugin))
                            .replace("<device>", getDevice(p, plugin))
                            .replace("<health>", p.getHealth() + "")
                            .replace("<model>", p.getLoginChainData().getDeviceModel() + "")
                            .replace("<version>", p.getLoginChainData().getGameVersion())
                            .replace("<language>", p.getLoginChainData().getLanguageCode())
                            .replace("<ping>", getPing(p))
                            .replace("<deathskull>" , getSkulll(p , plugin))
                    //message.format: "<prefix> <player> <suffix> >> <msg>
            );
        }
    }

    @EventHandler
    public void cooldownMessage(PlayerChatEvent e){
        Player p = (Player) e.getPlayer();
        Config conf = plugin.getConfig();
        Long time = conf.getLong("cooldown") * 100;
        if(conf.getBoolean("cooldown-enable")) {
            if(!(p.isOp())) {
                if (!cooldown.containsKey(p.getUniqueId()) || System.currentTimeMillis() - cooldown.get(p.getUniqueId()) > time) {
                    cooldown.put(p.getUniqueId(), System.currentTimeMillis());
                } else {
                    long cooldownTime = (time - (System.currentTimeMillis() - cooldown.get(p.getUniqueId()))) / 100;
                    e.setCancelled(true);
                    p.sendMessage(conf.getString("cooldown-message")
                            .replace("<left>", cooldownTime + ""));
                }
            }
        }
    }
}