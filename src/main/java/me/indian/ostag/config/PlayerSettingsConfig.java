package me.indian.ostag.config;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MathUtil;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.ThreadUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerSettingsConfig {


    private final OsTag plugin;
    private final String mentionKey;
    private final String msgKey;
    private final Config defaulConfig;
    private final Config playersConfig;
    private final String defaultSound;
    private final int defaultCustomIndex;
    private final boolean defaultMsg;
    private final ExecutorService executorService;

    public PlayerSettingsConfig(final OsTag plugin) {
        this.plugin = plugin;
        this.defaulConfig = this.plugin.getConfig();

        File file = new File(this.plugin.getDataFolder(), "players.yml");
        if (!file.exists()) {
            this.plugin.getLogger().info(MessageUtil.colorize("&cFile&6 players.yml&c not found"));
            try {
                file.createNewFile();
                this.plugin.getLogger().info(MessageUtil.colorize("&aFile&6 players.yml&a created!"));
            } catch (IOException e) {
                this.plugin.getLogger().warning(TextFormat.RED + "CANT CREATE FILE");
                e.printStackTrace();
            }
        }

        this.plugin.saveResource("players.yml");
        this.playersConfig = new Config(file, Config.YAML);

        final LinkedHashMap<String, Object> defaultMap = new ConfigSection();
        defaultMap.put("SubTitle", "&6<player>&a mentioned you in chat");
        defaultMap.put("Sounds", 100);

        //confi keyse
        this.mentionKey = ".Mention";
        this.msgKey = ".Msg";

        //config defaults and save
        this.playersConfig.setDefault(defaultMap);
        this.playersConfig.save();

        //defaults
        this.defaultSound = "BLOCK_SCAFFOLDING_BREAK";
        this.defaultCustomIndex = 100;
        this.defaultMsg = true;

        //Management
        this.executorService = Executors.newScheduledThreadPool(2, new ThreadUtil("players.yml Thread"));
    }

    public boolean hasPlayer(final Player player) {
        return !this.playersConfig.getSection(player.getName()).isEmpty();
    }

    public void createPlayerSection(final Player player) {
        if (this.hasPlayer(player)) return;
        final String playerName = player.getName();

        //Management
        this.playersConfig.set(playerName + ".lastPlayed", player.getLastPlayed());

        //mention sound
        this.playersConfig.set(playerName + mentionKey + ".enabled", true);
        this.playersConfig.set(playerName + mentionKey + ".title", true);
        this.playersConfig.set(playerName + mentionKey + ".sound", defaultSound);
        this.playersConfig.set(playerName + mentionKey + ".custom-index", defaultCustomIndex);
        //msg
        this.playersConfig.set(playerName + msgKey + ".enabled", defaultMsg);
        this.playersConfig.set(playerName + msgKey + ".private.enabled", defaultMsg);
        this.playersConfig.set(playerName + msgKey + ".ignored", Arrays.asList("ExamplePlayer"));

        System.out.println("Stworzono " + playerName);

        //save config
        this.playersConfig.save();
    }

     /*
       This is for menagment "players.yml" file
     */


    public void removeOldPlayers() {
        for (final String key : this.playersConfig.getKeys(false)) {
            if (this.playersConfig.isSection(key)) {
                final ConfigSection section = this.playersConfig.getSection(key);
                final int lastPlayed = section.getInt("lastPlayed", 0);
                final int difference = MathUtil.dayDifference(lastPlayed);
                if (difference >= 5) {
                    this.playersConfig.getSection(key).clear();
                    this.plugin.getLogger().info(MessageUtil.colorize("&6<player>&a has been removed in&b players.yml&a because he has not played on the server for more than 5 days")
                            .replace("<player>", key));
                }
            }
        }
        this.playersConfig.save();
    }


    public void setLastPlayed(final String name, final long last) {
        this.playersConfig.set(name + ".lastPlayed", last);
        this.playersConfig.save();
    }

    public Long getLastPlayed(final String name) {
        return this.playersConfig.getLong(name + ".lastPlayed");
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public Config getConfig() {
        return this.playersConfig;
    }

      /*
    This is for Msg function and it is still experimental
     */

    public boolean hasEnabledMsg(final Player player) {
        return this.playersConfig.getBoolean(player.getName() + msgKey + ".enabled", defaultMsg);
    }

    public void setEnabledMsg(final Player player, final boolean enabled) {
        this.playersConfig.set(player.getName() + msgKey + ".enabled", enabled);
        this.playersConfig.save();
    }

    public List<String> getIgnoredPlayers(final String name) {
        return this.playersConfig.getStringList(name + msgKey + ".ignored");
    }

    public boolean isIgnored(final Player player, final String name) {
        return this.playersConfig.getStringList(player.getName() + msgKey + ".ignored").contains(name);
    }

    public void ignorePlayer(final Player player, final String ignore) {
        final List<String> ignored = getIgnoredPlayers(player.getName());
        ignored.add(ignore);
        this.playersConfig.set(player.getName() + msgKey + ".ignored", ignored);
        this.playersConfig.save();
        player.sendMessage(MessageUtil.colorize(this.defaulConfig.getString("Msg.ignored")
                .replace("<player>", ignore)
        ));
    }

    public void unIgnorePlayer(final Player player, final String ignore) {
        final List<String> ignored = getIgnoredPlayers(player.getName());
        ignored.remove(ignore);
        this.playersConfig.set(player.getName() + msgKey + ".ignored", ignored);
        this.playersConfig.save();
        player.sendMessage(MessageUtil.colorize(this.defaulConfig.getString("Msg.un-ignored")
                .replace("<player>", ignore)
        ));
    }

    public boolean hasEnabledPrivateMsg(final Player player) {
        if (!player.hasPermission(Permissions.ADMIN)) {
            return false;
        }
        return this.playersConfig.getBoolean(player.getName() + msgKey + ".private.enabled", defaultMsg);
    }

    public void setEnabledPrivateMsg(final Player player, final boolean enabled) {
        this.playersConfig.set(player.getName() + msgKey + ".private.enabled", enabled);
        this.playersConfig.save();
    }


     /*
    This is for Mention Sound function and it is still experimental
     */

    public boolean mentionSoundFunctionEnabled() {
        return this.defaulConfig.getBoolean("MentionSound", true);
    }

    public void setMentionSoundFunctionEnabled(final boolean enabled) {
        this.defaulConfig.set("MentionSound", enabled);
        this.defaulConfig.save();
    }

    public boolean hasEnabledMentions(final Player player) {
        return this.playersConfig.getBoolean(player.getName() + mentionKey + ".enabled", true);
    }

    public void setEnabledMentions(final Player player, final boolean enabled) {
        this.playersConfig.set(player.getName() + mentionKey + ".enabled", enabled);
        this.playersConfig.save();
    }

    public String getMentionSound(final Player player) {
        return this.playersConfig.getString(player.getName() + mentionKey + ".sound", defaultSound);
    }

    public void setMentionSound(final Player player, final String sound) {
        this.playersConfig.set(player.getName() + mentionKey + ".sound", sound);
        this.playersConfig.save();
    }

    public int getPlayerCustomIndex(final Player player){
        return this.playersConfig.getInt(player.getName() + mentionKey + ".custom-index", defaultCustomIndex);
    }

    public void setPlayerCustomIndex(final Player player, final int index) {
        this.playersConfig.set(player.getName() + mentionKey + ".custom-index", index);
        this.playersConfig.save();
    }

    public boolean hasEnabledTitle(final Player player) {
        return this.playersConfig.getBoolean(player.getName() + mentionKey + ".title", true);
    }

    public void setEnabledTitle(final Player player, final boolean enabled) {
        this.playersConfig.set(player.getName() + mentionKey + ".title", enabled);
        this.playersConfig.save();
    }

    public int getSoundIndex(final String sound) {
        int counter = 0;
        for (final Sound sound2 : Sound.values()) {
            if (sound2.name().equals(sound)) {
                return counter;
            }
            counter++;
        }
        return counter;
    }

    public String getSoundByIndex(final int index) {
        String sound = "";
        int counter = 0;
        for (final Sound sound2 : Sound.values()) {
            if (index == counter) {
                return sound2.name();
            }
            counter++;
        }
        return sound;
    }

    public String getSubTitleMessage(final Player player) {
        return MessageUtil.colorize(this.playersConfig.getString("SubTitle", "<player> mentioned you in chat")).replace("<player>", player.getName());
    }

    public void setSoundsValue(final int sounds) {
        this.playersConfig.set("Sounds", sounds);
        this.playersConfig.save();
    }

    public int getSoundsValue(){
        return this.playersConfig.getInt("Sounds", 100);
    }
}
