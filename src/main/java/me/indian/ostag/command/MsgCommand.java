package me.indian.ostag.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerSettingsConfig;
import me.indian.ostag.util.MessageUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MsgCommand extends Command {

    private final OsTag plugin;
    private final Config config;
    private final PlayerSettingsConfig playerSettingsConfig;
    private final static Map<String, String> lastPlayer = new HashMap<>();

    public MsgCommand(final OsTag plugin) {
        super("omsg", "Ostag private messages", MessageUtil.colorize("&aUsage &b/omsg &8<player> <message>"));
        this.setAliases(new String[]{"w", "msg", "tell"});
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("player", false, CommandParamType.TARGET),
                CommandParameter.newType("message", false, CommandParamType.TEXT)
        });

        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.playerSettingsConfig = this.plugin.getPlayerSettingsConfig();
    }

    public static String getLastMsgPlayer(final Player player) {
        return lastPlayer.get(player.getName()) != null ? lastPlayer.get(player.getName()) : "null";
    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        if (sender instanceof Player && !this.playerSettingsConfig.hasEnabledMsg((Player) sender)) {
            sender.sendMessage(MessageUtil.colorize(this.config.getString("Msg.you-disabled")));
            return false;
        }
        final Player recipient = this.plugin.getServer().getPlayer(args[0]);
        if (recipient != null) {
            if (!this.playerSettingsConfig.hasEnabledMsg(recipient)) {
                sender.sendMessage(MessageUtil.colorize(this.config.getString("Msg.has-disabled").replace("<player>", args[0])));
                return true;
            }
            if(this.playerSettingsConfig.getIgnoredPlayers(recipient.getName()).contains(sender.getName())){
                sender.sendMessage(MessageUtil.colorize(this.config.getString("Msg.cant-msg").replace("<player>", recipient.getName())));
                return false;
            }
            if(this.playerSettingsConfig.getIgnoredPlayers(sender.getName()).contains(recipient.getName())){
                sender.sendMessage(MessageUtil.colorize(this.config.getString("Msg.cant-msg").replace("<player>", recipient.getName())));
                return false;
            }

            if (!Objects.equals(lastPlayer.get(sender.getName()), recipient.getName())) {
                lastPlayer.put(sender.getName(), recipient.getName());
            }
            if (!Objects.equals(lastPlayer.get(recipient.getName()), sender.getName())) {
                lastPlayer.put(recipient.getName(), sender.getName());
            }

            final String message = MessageUtil.buildMessageFromArgs(args, args[0]);

            if (message.isEmpty() || recipient.getName().equals(sender.getName())) {
                sender.sendMessage(this.usageMessage);
                return false;
            }

            final String messageToPlayer = MessageUtil.colorize(this.config.getString("Msg.to-player")
                    .replace("<me>", sender.getName())
                    .replace("<player>", recipient.getName())) + message;

            sender.sendMessage(messageToPlayer);
            recipient.sendMessage(MessageUtil.colorize(this.config.getString("Msg.from-player")
                    .replace("<me>", recipient.getName())
                    .replace("<player>", sender.getName())) + message);

            this.sendToAdmins(messageToPlayer, sender, recipient);
            if (this.config.getBoolean("Msg.console-logs", true)) {
                this.plugin.getLogger().info(MessageUtil.colorize("&8[&dMessages&8]") + messageToPlayer);
            }

        } else {
            sender.sendMessage(MessageUtil.colorize(this.config.getString("Msg.null-recipient").replace("<player>", args[0])));
        }
        return true;
    }

    private void sendToAdmins(final String msg, final CommandSender sender, final Player recipient) {
        for (final Player all : this.plugin.getServer().getOnlinePlayers().values()) {
            if (this.playerSettingsConfig.hasEnabledPrivateMsg(all)) {
                if (all != sender && all != recipient) {
                    all.sendMessage(MessageUtil.colorize(msg));
                }
            }
        }
    }
}
