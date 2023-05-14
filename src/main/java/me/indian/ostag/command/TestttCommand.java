package me.indian.ostag.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginLogger;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;

public class TestttCommand implements CommandExecutor {

    private final OsTag plugin;

    public TestttCommand(OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            final PluginLogger logger = plugin.getLogger();

            logger.info("Debug: " + plugin.debug);

            logger.info("Ostag: " + plugin.osTag);

            logger.info("Formatter: " + plugin.chatFormatter);

            logger.error("Test");

            logger.info(GithubUtil.getLatestTag());

            logger.info(GithubUtil.getBehindCount());

        }
        return false;
    }
}