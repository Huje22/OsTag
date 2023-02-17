package me.indian.ostag.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import me.indian.ostag.OsTag;
import me.indian.ostag.utils.ColorUtil;

import java.util.List;

public class OsTagCommand implements CommandExecutor {

    private final OsTag plugin;

    public OsTagCommand(OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final List<String> advancedPlayers = plugin.getConfig().getStringList("advanced-players");
        if (args.length == 0) {
            sender.sendMessage(ColorUtil.replaceColorCode("&aUsage &b/ostag &8[version , reload , add <player>]"));
            return false;
        }
        if (args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")) {
            if (sender.hasPermission("ostag.admin")) {
                OsTag.getInstance().sendOnEnableInfo("admin", sender);
            } else {
                OsTag.getInstance().sendOnEnableInfo("normal", sender);
            }
            return false;
        }
        if (sender.hasPermission("ostag.admin")) {
            if (args[0].equalsIgnoreCase("add")) {
                final Player target = Server.getInstance().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ColorUtil.replaceColorCode("&cplayer &6" + args[1] + "&c does not exist"));
                    return false;
                }
                if (advancedPlayers.contains(target.getDisplayName())) {
                    advancedPlayers.remove(target.getDisplayName());
                    sender.sendMessage(ColorUtil.replaceColorCode("&6" + target.getDisplayName() + " &chas been removed from advanced player list"));
                    plugin.getConfig().set("advanced-players", advancedPlayers);
                } else {
                    advancedPlayers.add(target.getDisplayName());
                    sender.sendMessage(ColorUtil.replaceColorCode("&6" + target.getDisplayName() + " &ahas been added to advanced player list"));
                    plugin.getConfig().set("advanced-players", advancedPlayers);
                }
            }
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                try {
                    final long millisActualTime = System.currentTimeMillis();
                    plugin.getConfig().reload();
                    sender.sendMessage(ColorUtil.replaceColorCode("&aConfig Reloaded"));
                    final long executionTime = System.currentTimeMillis() - millisActualTime;
                    sender.sendMessage(ColorUtil.replaceColorCode("&aReloaded in &b" + executionTime + " &ams"));
                } catch (final Exception exception) {
                    String error = ColorUtil.replaceColorCode("&cCan't reload config , check console to see error");
                    sender.sendMessage(error);
                    plugin.getLogger().warning(error);
                    System.out.println(exception + "");
                }
            }
            plugin.getConfig().save();
        } else {
            sender.sendMessage(ColorUtil.replaceColorCode("&cYou don't have permisions"));
        }
        return false;
    }
}