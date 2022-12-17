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


            PlaceholderAPI api = PlaceholderAPI.getInstance();
            String test = api.getValue("%time%", p);
            p.sendMessage(api.translateString("%server_online% - get current online players count %server_max_players% - get max player count %server_motd% - get the server motd %server_ram_used% - get used memory %server_ram_free% - get free memory %server_ram_total% - get total memory %server_ram_max% - get max memory %server_cores% - get available processor cores %server_tps% - get current TPS %server_uptime% - get the current uptime" , p));


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
