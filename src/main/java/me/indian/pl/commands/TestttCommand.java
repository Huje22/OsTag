package me.indian.pl.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.pl.OsTag;

public class TestttCommand implements CommandExecutor {

    private final OsTag plugin;

    public TestttCommand(OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            String input = "";
            Player p = (Player) sender;
//            p.sendMessage(PlayerInfoUtil.getGroupDisName(p));
//            p.sendMessage(PlayerInfoUtil.getSkulll(p));
//            p.sendMessage(PlayerInfoUtil.getXuid(p));
//            p.sendMessage(PlayerInfoUtil.getXp(p));
//            p.sendMessage(PlayerInfoUtil.getDevice(p));
//            p.sendMessage(PlayerInfoUtil.getControler(p));
//            p.sendMessage(PlayerInfoUtil.getGameMode(p));
//            p.sendMessage("Konsola");
//            OsTag.getInstance().sendOnEnableInfo("console" , null);
//            p.sendMessage("admin");
//            OsTag.getInstance().sendOnEnableInfo("admin" , p);
//            p.sendMessage("Normal");
//            OsTag.getInstance().sendOnEnableInfo("normal" , p);
//            p.sendMessage(TextFormat.colorize('&' , "&aTest"));
            p.sendMessage(p.getLevel().getName());


            PlaceholderAPI api = PlaceholderAPI.getInstance();
            String test = api.getValue("%time%", p);
            p.sendMessage(api.translateString("%device%"));


        }
        // for later
//        switch(args[0]) {
//            case "banuj":
//                sender.sendMessage("ban");
//                break;
//            case "odbanuj":
//                sender.sendMessage("Unban");
//                break;
//            default:
//                sender.sendMessage("Niepoprawne użycie komendy");
//        }



        return false;
    }
}
