package me.indian.ostag.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginLogger;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;

public class TestttCommand implements CommandExecutor {

    private final OsTag plugin;

    public TestttCommand(final OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender.isOp()) {
            final PluginLogger logger = this.plugin.getLogger();

            logger.info("Debug: " + this.plugin.debug);

            logger.info("Ostag: " + this.plugin.osTag);

            logger.info("Formatter: " + this.plugin.chatFormatter);

            logger.error("Test");

            logger.info(GithubUtil.getLatestTag());

            logger.info(GithubUtil.getBehindCount());

        }
        return false;
    }
}