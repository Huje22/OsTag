package me.indian.pl.others;

import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.pl.OsTag;
import me.indian.pl.listeners.InputListener;
import me.indian.pl.utils.PlayerInfoUtil;

public class OsTagPlaceholderAPI {

    /*
    This is only test , but now corectly working
     */


    private static OsTag plugin = OsTag.getInstance();

    private static PlaceholderAPI api = PlaceholderAPI.getInstance();

    public static void registerPlaceholders() {
        api.builder("cps", Integer.class)
                .autoUpdate(true)
                .updateInterval(1)
                .loader(entry -> InputListener.getCPS(entry.getPlayer()))
                .build();

        api.builder("device", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getDevice(entry.getPlayer());
                });
        api.builder("test", String.class)
                .visitorLoader(entry -> {
                    return "dupa";
                });
    }
}
