package me.indian.ostag.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class OsTagCommand extends Command {

    private final OsTag plugin;
    private final List<String> confirmations = new ArrayList<>();

    public OsTagCommand(final OsTag plugin) {
        super("ostag", "ostag management" );

        commandParameters.clear();
//        /ostag reload
        commandParameters.put("reload", new CommandParameter[]{
                CommandParameter.newEnum("reload", new CommandEnum("reload", "reload")),
        });
//        /ostag update
        commandParameters.put("update", new CommandParameter[]{
                CommandParameter.newEnum("update", new CommandEnum("update", "update"))
        });
//        /ostag update
        commandParameters.put("version", new CommandParameter[]{
                CommandParameter.newEnum("version", new CommandEnum("version", "version"))
        });
//        /ostag add <player>
        commandParameters.put("add", new CommandParameter[]{
                CommandParameter.newEnum("add", new CommandEnum("add", "add")),
                CommandParameter.newType("add", CommandParamType.TARGET)
        });
        this.plugin = plugin;
    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        final Config config = this.plugin.getConfig();
        final List<String> advancedPlayers = config.getStringList("advanced-players");

        if (args.length == 0) {
            sender.sendMessage(TextUtil.replaceColorCode("&aUsage &b/ostag &8[version , reload , add <player>, update]"));
            return false;
        }
        if (args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")) {
            if (sender.hasPermission(Permissions.ADMIN)) {
                this.plugin.pluginInfo("admin", sender);
            } else {
                this.plugin.pluginInfo("normal", sender);
            }
            return false;
        }
        if (sender.hasPermission(Permissions.ADMIN)) {
            if (args[0].equalsIgnoreCase("add")) {
                try {
                    final Player target = Server.getInstance().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(TextUtil.replaceColorCode("&cplayer &6" + args[1] + "&c does not exist"));
                        return false;
                    }
                    if (advancedPlayers.contains(target.getDisplayName())) {
                        advancedPlayers.remove(target.getDisplayName());
                        sender.sendMessage(TextUtil.replaceColorCode("&6" + target.getDisplayName() + " &chas been removed from advanced player list"));
                        config.set("advanced-players", advancedPlayers);
                    } else {
                        advancedPlayers.add(target.getDisplayName());
                        sender.sendMessage(TextUtil.replaceColorCode("&6" + target.getDisplayName() + " &ahas been added to advanced player list"));
                        config.set("advanced-players", advancedPlayers);
                    }
                } catch (final Exception e) {
                    sender.sendMessage(TextUtil.replaceColorCode("&cYou must give player name! "));
                }
            }
            if (args[0].equalsIgnoreCase("update")) {
                if (this.confirmations.contains(sender.getName())) {
                    this.confirmations.remove(sender.getName());
                    sender.sendMessage(TextUtil.replaceColorCode("&aAction confirmed"));
                    this.plugin.getUpdateUtil().manualUpDate(sender);
                } else {
                    this.confirmations.add(sender.getName());
                    sender.sendMessage(TextUtil.replaceColorCode("&cAre you sure you want to confirm this action? enter the command again to confirm, you have 30 seconds"));
                    this.timeRemove(sender);
                }
            }
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                try {
                    final long millisActualTime = System.currentTimeMillis();
                    config.reload();
                    sender.sendMessage(TextUtil.replaceColorCode("&aConfig Reloaded"));
                    final long executionTime = System.currentTimeMillis() - millisActualTime;
                    sender.sendMessage(TextUtil.replaceColorCode("&aReloaded in &b" + executionTime + " &ams"));
                } catch (final Exception exception) {
                    sender.sendMessage(TextUtil.replaceColorCode("&cCan't reload config , check console to see error"));
                    this.plugin.getLogger().error(exception.getMessage());
                }
            }
            config.save();
        } else {
            sender.sendMessage(TextUtil.replaceColorCode("&cYou don't have permissions"));
        }
        return false;
    }

    private void timeRemove(final CommandSender sender) {
        new NukkitRunnable() {
            @Override
            public void run() {
                if (OsTagCommand.this.confirmations.contains(sender.getName())) {
                    OsTagCommand.this.confirmations.remove(sender.getName());
                    sender.sendMessage(TextUtil.replaceColorCode("&cYour time to confirm has expired"));
                }
            }
        }.runTaskLater(this.plugin, 30 * 20);
    }
}