package me.indian.ostag.config;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class PlayerMentionConfig {

    /*
    This is for Mention Sound function and it is still experimental
     */

    private final Config playersConfig;
    private final String defaultSound;
    private final int defaultCustomIndex;

    public PlayerMentionConfig(final OsTag plugin) {
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
        defaultMap.put("Sounds" , 100);

        this.playersConfig.setDefault(defaultMap);
        this.playersConfig.save();
        this.defaultSound = "BLOCK_SCAFFOLDING_BREAK";
        this.defaultCustomIndex = 100;
    }

    public boolean hasPlayer(final Player player) {
        return !this.playersConfig.getSection(player.getName()).isEmpty();
    }

    public void createPlayerSection(final Player player) {
        if (this.hasPlayer(player)) return;
        final String playerName = player.getName();
        this.playersConfig.set(playerName + ".enabled", true);
        this.playersConfig.set(playerName + ".title", true);
        this.playersConfig.set(playerName + ".sound", defaultSound);
        this.playersConfig.set(playerName + ".custom-index", defaultCustomIndex);
        System.out.println("Created " + playerName);
        this.playersConfig.save();
    }

    public boolean hasEnabledMentions(final Player player) {
        return this.playersConfig.getBoolean(player.getName() + ".enabled", true);
    }

    public void setEnabledMentions(final Player player, final boolean enabled) {
        this.playersConfig.set(player.getName() + ".enabled", enabled);
        this.playersConfig.save();
    }

    public String getMentionSound(final Player player) {
        return this.playersConfig.getString(player.getName() + ".sound", defaultSound);
    }

    public void setMentionSound(final Player player, final String sound) {
        this.playersConfig.set(player.getName() + ".sound", sound);
        this.playersConfig.save();
    }

    public int getPlayerCustomIndex(final Player player){
        return this.playersConfig.getInt(player.getName() + ".custom-index", defaultCustomIndex);
    }

    public void setPlayerCustomIndex(final Player player, final int index) {
        this.playersConfig.set(player.getName() + ".custom-index" , index);
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

    public boolean hasEnabledTitle(final Player player) {
        return this.playersConfig.getBoolean(player.getName() + ".title", true);
    }

    public void setEnabledTitle(final Player player, final boolean enabled) {
        this.playersConfig.set(player.getName() + ".title", enabled);
        this.playersConfig.save();
    }

    public String getSubTitleMessage(final Player player) {
        return MessageUtil.colorize(this.playersConfig.getString("SubTitle", "<player> mentioned you in chat")).replace("<player>", player.getName());
    }

    public void setSoundsValue(final int sounds){
        this.playersConfig.set("Sounds", sounds);
        this.playersConfig.save();
    }

    public int getSoundsValue(){
        return this.playersConfig.getInt("Sounds", 100);
    }

    public Config getConfig() {
        return playersConfig;
    }
}
