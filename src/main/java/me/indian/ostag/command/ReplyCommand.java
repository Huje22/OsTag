package me.indian.ostag.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;

public class ReplyCommand extends Command {
    private final OsTag plugin;
    private final Config config;

    public ReplyCommand(final OsTag plugin) {
        super("r", " Ostag reply command" , "/r <message>");
        this.setAliases(new String[]{"reply"});
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("message", false, CommandParamType.TEXT)
        });
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final String lastPlayer = MsgCommand.getLastMsgPlayer(player);
            if (lastPlayer.equals("null")) {
                player.sendMessage(MessageUtil.colorize(this.config.getString("Msg.no-one")));
                return false;
            }
            MessageUtil.playerCommand(player, "omsg " + lastPlayer + " " + MessageUtil.buildMessageFromArgs(args));
        }
        return false;
    }
}
