package me.indian.ostag.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;

public class ReplyCommand extends Command {
    private final OsTag plugin;
    private final Config config;

    public ReplyCommand(final OsTag plugin) {
        super("r", " Ostag reply command");
        this.setAliases(new String[]{"reply"});
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final String lastPlayer = MsgCommand.getLastMsgPlayer(player);
            System.out.println(lastPlayer);
            if (lastPlayer.equals("null")) {
                // fuck java 8 :D
                player.sendMessage(MessageUtil.colorize(this.config.getString("Msg.no-one")));
                return false;
            }
            MessageUtil.playerCommand(player, "omsg " + lastPlayer + " " + MessageUtil.buildMessageFromArgs(args));
        }
        return false;
    }
}
