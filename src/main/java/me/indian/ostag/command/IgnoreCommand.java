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

public class IgnoreCommand extends Command {
    private final OsTag plugin;
    private final Config config;
    private final PlayerSettingsConfig playerSettingsConfig;

    public IgnoreCommand(final OsTag plugin) {
        super("ignore", "Ostag ignore command", MessageUtil.colorize("&aUsage &b/ignore &8<player>"));
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("message", false, CommandParamType.TARGET)
        });
        this.playerSettingsConfig = this.plugin.getPlayerSettingsConfig();
    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final Player ignored = this.plugin.getServer().getPlayer(args[0]);

            if (ignored != null) {
                if (this.playerSettingsConfig.getIgnoredPlayers(player.getName()).contains(ignored.getName())) {
                    this.playerSettingsConfig.unIgnorePlayer(player, ignored.getName());
                } else {
                    this.playerSettingsConfig.ignorePlayer(player, ignored.getName());
                }
            } else {
                player.sendMessage(MessageUtil.colorize(this.config.getString("Msg.null-recipient").replace("<player>", args[0])));
            }
        }
        return false;
    }
}
