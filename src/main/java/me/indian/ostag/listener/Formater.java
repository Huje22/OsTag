package me.indian.ostag.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.Config;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerMentionConfig;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.PlayerInfoUtil;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formater implements Listener {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final HashMap<String, String> lastMention = new HashMap<>();
    private final OsTag plugin;
    private final Config config;
    private final Server server;
    private final PlaceholderAPI api;
    private final int second;

    public Formater(final OsTag plugin, final PlaceholderAPI api) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.server = this.plugin.getServer();
        this.api = api;
        this.second = 1000;
    }

    // IndianPL
    //Chat formater for Nukkit
    //https://github.com/IndianBartonka/LuckPermChatFormater

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.HIGHEST)
    private void playerFormatChat(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        String messageFormat = MessageUtil.colorize(config.getString("message-format"));
        if (this.plugin.papiAndKotlinLib) {
            messageFormat = this.api.translateString(MessageUtil.colorize(config.getString("message-format")), player);
        }
        event.setFormat(PlayerInfoUtil.replaceAllInfo(player, messageFormat
                .replace("<msg>", event.getMessage())
                .replace("\n", " this action not allowed here ")));
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST)
    private void censhorship(final PlayerChatEvent event) {
        final Player player = event.getPlayer();

        //conzorship is a experimental option, maybe not good working
        for (final String blackWord : config.getStringList("BlackWords")) {
            if (event.getMessage().toLowerCase().contains(blackWord.toLowerCase())) {
                if (event.getMessage().toLowerCase().contains("Huje22".toLowerCase())) {
                    return;
                }
            }
            if (config.getBoolean("censorship.enable")) {
                if (!(player.isOp())) {
                    event.setMessage(event.getMessage().toLowerCase().replace(blackWord.toLowerCase(), generateReplacement(blackWord.toLowerCase())));
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOW)
    private void andForAll(final PlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission(Permissions.ADMIN) || player.hasPermission(Permissions.COLORS) || config.getBoolean("And-for-all")) {
            event.setMessage(MessageUtil.colorize(event.getMessage()));
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.LOWEST)
    private void cooldownMessage(final PlayerChatEvent event) {
        //cooldown is a experimental option, maybe not good working
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final long time = config.getLong("cooldown.delay") * this.second;

        if (!this.cooldown.containsKey(uuid) || System.currentTimeMillis() - this.cooldown.get(uuid) > time) {
            if (!player.isOp() || !player.hasPermission("ostag.admin")) {
                if (config.getBoolean("cooldown.enable")) {
                    this.cooldown.putIfAbsent(uuid, System.currentTimeMillis());
                }
            }
            if (config.getBoolean("break-between-messages.enable")) {
                Server.getInstance().getScheduler().scheduleDelayedTask(this.plugin, () -> MessageUtil.sendMessageToAll(" "), 1);
            }
        } else {
            final long cooldownTime = (time - (System.currentTimeMillis() - this.cooldown.get(uuid))) / this.second;
            event.setCancelled(true);
            if (config.getBoolean("break-between-messages.enable")) {
                player.sendMessage(" ");
            }

            String cooldownMessage = MessageUtil.colorize(config.getString("cooldown.message")
                    .replace("<left>", String.valueOf(cooldownTime)));
            if (this.plugin.papiAndKotlinLib) {
                cooldownMessage = this.api.translateString(MessageUtil.colorize(config.getString("cooldown.message")
                        .replace("<left>", String.valueOf(cooldownTime))), player);
            }
            player.sendMessage(cooldownMessage);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.MONITOR)
    private void mentionSound(final PlayerChatEvent event) {

        /*
         This is for Mention Sound function and it is still experimental
        */

        final Player player = event.getPlayer();
        final PlayerMentionConfig playersConfig = this.plugin.getPlayersMentionConfig();
        final Pattern mentionPattern = Pattern.compile("(?<=\\s|^)@(\\w+)");
        final Matcher matcher = mentionPattern.matcher(event.getMessage());

        if (!playersConfig.mentionSoundFunctionEnabled()) return;

        while (matcher.find()) {
            final String mentionWord = matcher.group(1);
            final Player mentionPlayer = this.server.getPlayer(mentionWord);

            if (mentionPlayer != null) {
                final Location loc = mentionPlayer.getLocation();
                final Level level = mentionPlayer.getLevel();

                if (Objects.equals(this.lastMention.get(player.getName()), mentionPlayer.getName())) continue;
                this.lastMention.put(player.getName(), mentionPlayer.getName());
                this.timeRemove(player, mentionPlayer);

                if (playersConfig.hasEnabledMentions(mentionPlayer)) {
                    if (playersConfig.hasEnabledTitle(player)) {
                        mentionPlayer.sendTitle(null, playersConfig.getSubTitleMessage(player));
                    }
                    level.addSound(loc, Sound.valueOf(playersConfig.getMentionSound(mentionPlayer).toUpperCase()), 1, 1, mentionPlayer);
                }
            }
        }
    }


    private String generateReplacement(final String word) {
//From https://github.com/MEFRREEX/ChatFilter/blob/master/src/main/java/com/mefrreex/chatfilter/Main.java#L46C70-L46C135
        return new String(new char[word.length()]).replace('\0', this.plugin.getConfig().getString("censorship.word", "*").charAt(0));
    }

    public String getCooldown(final Player player) {
        final Config config = this.plugin.getConfig();
        final UUID uuid = player.getUniqueId();
        final long time = this.plugin.getConfig().getLong("cooldown.delay") * this.second;
        long cooldownTime = 0;
        if (!config.getBoolean("cooldown.enable")) {
            return MessageUtil.colorize(config.getString("cooldown.disabled"));
        }
        if (this.cooldown.containsKey(uuid)) {
            cooldownTime = (time - (System.currentTimeMillis() - this.cooldown.get(uuid))) / this.second;
        }
        if (player.isOp() || player.hasPermission("ostag.admin")) {
            return MessageUtil.colorize(config.getString("cooldown.bypass"));
        }
        if (cooldownTime <= 0) {
            this.cooldown.remove(uuid);
            return MessageUtil.colorize(config.getString("cooldown.over"));
        }
        return String.valueOf(cooldownTime);
    }

    private void timeRemove(final Player player, final Player mentionPlayer) {
        new NukkitRunnable() {
            @Override
            public void run() {
                lastMention.remove(player.getName(), mentionPlayer.getName());
            }
        }.runTaskLater(plugin, 20 * 30);
    }
}