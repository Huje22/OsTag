package me.indian.ostag.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.ColorUtil;
import me.indian.ostag.util.Permissions;

public class OsTagCommand implements CommandExecutor {

    private final OsTag plugin;
    private final String usage = "&aUsage &b/ostag &8[version , reload , add <player>, update]";
    private final List<String> confirmations = new ArrayList<>();

    public OsTagCommand(OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Config config = plugin.getConfig();
        final List<String> advancedPlayers = config.getStringList("advanced-players");

        if (args.length == 0) {
            sender.sendMessage(ColorUtil.replaceColorCode(usage));
            return false;
        }
        if (args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")) {
            if (sender.hasPermission(Permissions.ADMIN)) {
                OsTag.getInstance().pluginInfo("admin", sender);
            } else {
                OsTag.getInstance().pluginInfo("normal", sender);
            }
            return false;
        }
        if (sender.hasPermission(Permissions.ADMIN)) {
            if (args[0].equalsIgnoreCase("add")) {
                final Player target = Server.getInstance().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(ColorUtil.replaceColorCode("&cplayer &6" + args[1] + "&c does not exist"));
                    return false;
                }
                if (advancedPlayers.contains(target.getDisplayName())) {
                    advancedPlayers.remove(target.getDisplayName());
                    sender.sendMessage(ColorUtil.replaceColorCode("&6" + target.getDisplayName() + " &chas been removed from advanced player list"));
                    config.set("advanced-players", advancedPlayers);
                } else {
                    advancedPlayers.add(target.getDisplayName());
                    sender.sendMessage(ColorUtil.replaceColorCode("&6" + target.getDisplayName() + " &ahas been added to advanced player list"));
                    config.set("advanced-players", advancedPlayers);
                }
            }
            if (args[0].equalsIgnoreCase("update")) {
                if (confirmations.contains(sender.getName())) {
                    confirmations.remove(sender.getName());
                    sender.sendMessage(ColorUtil.replaceColorCode("&aAction confirmed"));
                    plugin.getUpdateUtil().manualUpDate();
                    if (sender instanceof Player)
                        sender.sendMessage(ColorUtil.replaceColorCode("&aCheck console for results"));
                } else {
                    confirmations.add(sender.getName());
                    sender.sendMessage(ColorUtil.replaceColorCode("&cAre you sure you want to confirm this action? enter the command again to confirm"));
                }
            }
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                try {
                    final long millisActualTime = System.currentTimeMillis();
                    config.reload();
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
            config.save();
        } else {
            sender.sendMessage(ColorUtil.replaceColorCode("&cYou don't have permissions"));
        }
        return false;
    }
}