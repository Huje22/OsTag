package me.indian.pl.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import me.indian.pl.Listeners.InputListener;
import me.indian.pl.OsTag;
import me.indian.pl.Utils.LuckPermUtil;
import me.indian.pl.Utils.PlayerInfoUtil;

public class TestttCommand implements CommandExecutor {

    private final OsTag plugin;

    public TestttCommand(OsTag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String input = "";
        Player p = (Player) sender;
            p.sendMessage(PlayerInfoUtil.getGroupDisName(p, plugin));
            p.sendMessage(PlayerInfoUtil.getSkulll(p, plugin));

        return false;
    }
}
