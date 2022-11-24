package me.indian.pl.Commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandExecutor;
import cn.nukkit.command.CommandSender;
import me.indian.pl.OsTag;
import me.indian.pl.Utils.OtherUtils;

import java.util.List;

public class OsTagCommand implements CommandExecutor {

    private final OsTag plugin;
    public OsTagCommand(OsTag plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> playerlist = plugin.getConfig().getStringList("advanced-players");
        if(sender.hasPermission("ostagpnx.admin")){
            if(args[0].equalsIgnoreCase("add")) {
                Player cel = Server.getInstance().getPlayer(args[1]);
                if (cel == null) {
                    sender.sendMessage("§cplayer §6" + args[1] + "§c does not exist");
                    return false;
                }
                if (playerlist.contains(cel.getDisplayName())) {
                    playerlist.remove(cel.getDisplayName());
                    sender.sendMessage("§6" + cel.getDisplayName() + " §chas been removed from advanced player list");
                    plugin.getConfig().set("advanced-players", playerlist);
                } else {
                    playerlist.add(cel.getDisplayName());
                    sender.sendMessage("§6" + cel.getDisplayName() + " §ahas been added to advanced player list");
                    plugin.getConfig().set("advanced-players", playerlist);
                }
                plugin.getConfig().save();
            }
            if(args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("v")){
                String ver = plugin.getDescription().getVersion();
                String aut = plugin.getDescription().getAuthors() + "";
                String verNuk = plugin.getServer().getNukkitVersion();
                String servVer = plugin.getServer().getVersion();
                String  apiVer =  plugin.getServer().getApiVersion();
                String ip = plugin.getServer().getIp();
                String port = plugin.getServer().getPort() + "";

                sender.sendMessage("§b-------------------------");
                sender.sendMessage("§aOsTagPNX version:§3 " + ver );
                sender.sendMessage("§aPlugin by:§6 " + aut.replace("[" , "").replace("]", ""));
                sender.sendMessage("§aNukkit Version:§3 " + verNuk);
                sender.sendMessage("§aNukkit Api Version:§3 " + apiVer);
                sender.sendMessage("§aServer Version:§3 " + servVer);
                sender.sendMessage(" ");
                sender.sendMessage("§eModules");
                sender.sendMessage("§aFormater§3: " + OtherUtils.getFormaterStatus(plugin));
                sender.sendMessage("§aOsTag§3: " + OtherUtils.getOsTagStatus(plugin));
                sender.sendMessage(" ");
                sender.sendMessage("§b-------------------------");
            }
            if(args[0].equalsIgnoreCase("reload")){
                plugin.getConfig().reload();
                sender.sendMessage("§aConfig Reloaded");
            }

        } else {
            sender.sendMessage("§cYou don't have permisions");
        }
        return false;
    }
}
