package me.indian.pl.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import me.indian.pl.OsTag;

public class TestttCommand implements CommandExecutor {

    private final OsTag plugin;

    public TestttCommand(OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("ostagpnx.admin")) {
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
