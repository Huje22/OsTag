package me.indian.ostag.util;

import cn.nukkit.Player;
import me.indian.ostag.OsTag;
import me.indian.ostag.listener.CpsListener;

import static me.indian.ostag.util.PlayerInfoUtil.getController;
import static me.indian.ostag.util.PlayerInfoUtil.getDevice;
import static me.indian.ostag.util.PlayerInfoUtil.getDimension;
import static me.indian.ostag.util.PlayerInfoUtil.getGameMode;
import static me.indian.ostag.util.PlayerInfoUtil.getHealth;
import static me.indian.ostag.util.PlayerInfoUtil.getLuckPermGroupDisName;
import static me.indian.ostag.util.PlayerInfoUtil.getLuckPermPreffix;
import static me.indian.ostag.util.PlayerInfoUtil.getLuckPermSuffix;
import static me.indian.ostag.util.PlayerInfoUtil.getPing;
import static me.indian.ostag.util.PlayerInfoUtil.getUnique;
import static me.indian.ostag.util.PlayerInfoUtil.getXp;
import static me.indian.ostag.util.PlayerInfoUtil.getXuid;

public class ReplaceUtil {

    private static final OsTag plugin = OsTag.getInstance();

    public static String replace(final Player player, final String msg) {
        return msg
                .replace("<cooldown>", plugin.getFormater().cooldown(player))
                .replace("<name>", player.getName())
                .replace("<dis_name>", player.getDisplayName())
                .replace("<suffix>", getLuckPermSuffix(player))
                .replace("<preffix>", getLuckPermPreffix(player))
                .replace("<groupDisName>", getLuckPermGroupDisName(player))
                .replace("<unique_description>", getUnique(player))
                .replace("<xp>", getXp(player))
                .replace("<device>", getDevice(player))
                .replace("<controller>", getController(player))
                .replace("<health>", getHealth(player))
                .replace("<max_health>", String.valueOf(player.getMaxHealth()))
                .replace("<model>", player.getLoginChainData().getDeviceModel())
                .replace("<food>", String.valueOf(player.getFoodData().getLevel() / 2))
                .replace("<max_food>", String.valueOf(player.getFoodData().getMaxLevel() / 2))
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
