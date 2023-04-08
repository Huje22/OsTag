package me.indian.ostag.utils;

import cn.nukkit.Player;
import me.indian.ostag.OsTag;
import me.indian.ostag.listeners.CpsListener;

import static me.indian.ostag.utils.PlayerInfoUtil.*;

public class ReplaceUtil {

    private static final OsTag plugin = OsTag.getInstance();

    public static String replace(Player player, String msg) {
        return msg
                .replace("<cooldown>", plugin.getFormater().cooldown(player))
                .replace("<name>", player.getDisplayName())
                .replace("<suffix>", getLuckPermSuffix(player))
                .replace("<preffix>", getLuckPermPreffix(player))
                .replace("<groupDisName>", getLuckPermGroupDisName(player))
                .replace("<unique_description>", getUnique(player))
                .replace("<xp>", getXp(player))
                .replace("<device>", getDevice(player))
                .replace("<controller>", getController(player))
                .replace("<health>", getHealth(player))
                .replace("<max_health>", player.getMaxHealth() + "")
                .replace("<model>", player.getLoginChainData().getDeviceModel() + "")
                .replace("<food>", player.getFoodData().getLevel() / 2 + "")
                .replace("<max_food>", player.getFoodData().getMaxLevel() / 2 + "")
                .replace("<version>", player.getLoginChainData().getGameVersion())
                .replace("<xuid>", getXuid(player))
                .replace("<language>", player.getLoginChainData().getLanguageCode())
                .replace("<ping>", getPing(player))
                .replace("<cps>", String.valueOf(CpsListener.getCPS(player)))
                .replace("<gamemode>", getGameMode(player))
                .replace("<dimension>", getDimension(player)
                );
    }
}
