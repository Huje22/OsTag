package me.indian.pl.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import me.indian.pl.OsTag;
import me.indian.pl.utils.ChatColor;

import java.util.List;

public class OsTagCommand implements CommandExecutor {

    private final OsTag plugin;
    public OsTagCommand(OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> playerlist = plugin.getConfig().getStringList("advanced-players");
        if (sender.hasPermission("ostagpnx.admin")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.replaceColorCode("&aUsage &b/ostag &8[version , reload , add <player>]"));
                return false;
            }
            if (args[0].equalsIgnoreCase("add")) {
                Player cel = Server.getInstance().getPlayer(args[1]);
                if (cel == null) {
                    sender.sendMessage(ChatColor.replaceColorCode("&cplayer &6" + args[1] + "&c does not exist"));
                    return false;
                }
                if (playerlist.contains(cel.getDisplayName())) {
                    playerlist.remove(cel.getDisplayName());
                    sender.sendMessage(ChatColor.replaceColorCode("&6" + cel.getDisplayName() + " &chas been removed from advanced player list"));
                    plugin.getConfig().set("advanced-players", playerlist);
                } else {
                    playerlist.add(cel.getDisplayName());
                    sender.sendMessage(ChatColor.replaceColorCode("&6" + cel.getDisplayName() + " &ahas been added to advanced player list"));
                    plugin.getConfig().set("advanced-players", playerlist);
                }
                plugin.getConfig().save();
            }

            if (args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")) {
                OsTag.getInstance().sendOnEnableInfo(false, sender);
            }

            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                try {
                    long millisActualTime = System.currentTimeMillis();
                    plugin.getConfig().reload();
                    sender.sendMessage(ChatColor.replaceColorCode("&aConfig Reloaded"));
                    long executionTime = System.currentTimeMillis() - millisActualTime;
                    sender.sendMessage(ChatColor.replaceColorCode("&aReloaded in &b" + executionTime + " &ams"));
                } catch (Exception e) {
                    String error = ChatColor.replaceColorCode("&cCan't reload config , check console to see error");
                    sender.sendMessage(error);
                    plugin.getLogger().warning(error);
                    System.out.println(e);
                }
            }

        } else {
            sender.sendMessage(ChatColor.replaceColorCode("&cYou don't have permisions"));
        }

        return false;
    }
}
