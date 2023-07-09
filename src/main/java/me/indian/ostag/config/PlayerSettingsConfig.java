package me.indian.ostag.config;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class PlayerSettingsConfig {


    private final String mentionKey;
    private final String msgKey;
    private final Config defaulConfig;
    private final Config playersConfig;
    private final String defaultSound;
    private final int defaultCustomIndex;
    private final boolean defaultMsg;

    public PlayerSettingsConfig(final OsTag plugin) {
        this.defaulConfig = plugin.getConfig();

        File file = new File(plugin.getDataFolder(), "players.yml");
        if (!file.exists()) {
            plugin.getLogger().info(MessageUtil.colorize("&cFile&6 players.yml&c not found"));
            try {
                file.createNewFile();
                plugin.getLogger().info(MessageUtil.colorize("&aFile&6 players.yml&a created!"));
            } catch (IOException e) {
                plugin.getLogger().warning(TextFormat.RED + "CANT CREATE FILE");
                e.printStackTrace();
            }
        }

        plugin.saveResource("players.yml");
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
    }

    public boolean hasPlayer(final Player player) {
        return !this.playersConfig.getSection(player.getName()).isEmpty();
    }

    public void createPlayerSection(final Player player) {
        if (this.hasPlayer(player)) return;
        final String playerName = player.getName();
        //mention sound
        this.playersConfig.set(playerName + mentionKey + ".enabled", true);
        this.playersConfig.set(playerName + mentionKey + ".title", true);
        this.playersConfig.set(playerName + mentionKey + ".sound", defaultSound);
        this.playersConfig.set(playerName + mentionKey + ".custom-index", defaultCustomIndex);
        //msg
        this.playersConfig.set(playerName + msgKey + ".enabled", defaultMsg);
        this.playersConfig.set(playerName + msgKey + ".private.enabled", defaultMsg);
        this.playersConfig.set(playerName + msgKey + ".ignored" , Arrays.asList("ExamplePlayer"));

        //save config
        this.playersConfig.save();
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

    public List<String> getIgnoredPlayers(final String name){
        return this.playersConfig.getStringList(name + msgKey + ".ignored");
    }

    public void ignorePlayer(final Player player , final String ignore) {
        final List<String> ignored = getIgnoredPlayers(player.getName());
        ignored.add(ignore);
        this.playersConfig.set(player.getName() + msgKey + ".ignored", ignored);
        this.playersConfig.save();
        player.sendMessage(MessageUtil.colorize(this.defaulConfig.getString("Msg.ignored")
                .replace("<player>" , ignore)
        ));
    }

    public void unIgnorePlayer(final Player player , final String ignore) {
        final List<String> ignored = getIgnoredPlayers(player.getName());
        ignored.remove(ignore);
        this.playersConfig.set(player.getName() + msgKey + ".ignored", ignored);
        this.playersConfig.save();
        player.sendMessage(MessageUtil.colorize(this.defaulConfig.getString("Msg.un-ignored")
                .replace("<player>" , ignore)
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

    public Config getConfig() {
        return this.playersConfig;
    }
}
