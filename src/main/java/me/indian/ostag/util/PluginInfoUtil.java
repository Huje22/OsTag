package me.indian.ostag.util;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginDescription;
import me.indian.ostag.OsTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginInfoUtil {

    private final boolean lines;
    private final String pluginVersion;
    private final String authors;
    private final String contributors;
    private final String nukkitVersion;
    private final String serverVersion;
    private final String apiVersion;
    private final String latest;

    public PluginInfoUtil(final CommandSender sender, final boolean lines) {
        final OsTag plugin = OsTag.getInstance();
        final PluginDescription descriptor = plugin.getDescription();
        final Server server = plugin.getServer();
        this.lines = lines;
        this.pluginVersion = descriptor.getVersion();
        this.authors = MessageUtil.listToString(descriptor.getAuthors(), " , ");
        this.contributors = MessageUtil.listToString(Arrays.asList("Techno neziw", "Test"), " , ");
        this.nukkitVersion = server.getNukkitVersion();
        this.serverVersion = server.getVersion();
        this.apiVersion = server.getApiVersion();
        this.latest = GithubUtil.getFastTagInfo() + GithubUtil.getBehindCount(sender);
    }


    public List<String> getNormalInfo() {
        final List<String> normalInfo = new ArrayList<>();

        if (this.lines) {
            normalInfo.add("&b-------------------------------");
        }
        normalInfo.add("&aOsTag version:&3 " + pluginVersion);
        normalInfo.add("&aLatest version:&3 " + latest);
        normalInfo.add("&aPlugin by:&6 " + authors);
        normalInfo.add("&aContributors:&6 " + contributors);
        normalInfo.add("&aServer Version:&3 " + serverVersion);
        normalInfo.add(" ");
        normalInfo.add("&1Modules");
        normalInfo.add("&aFormatter&3: " + StatusUtil.getFormaterStatus());
        normalInfo.add("&aOsTag&3: " + StatusUtil.getOsTagStatus());
        normalInfo.add(" ");
        if (this.lines) {
            normalInfo.add("&b-------------------------------");
        }

        return normalInfo;
    }

    public List<String> getAdminInfo() {
        final List<String> adminInfo = new ArrayList<>();

        if (this.lines) {
            adminInfo.add("&b-------------------------------");
        }
        adminInfo.add("&aOsTag version:&3 " + pluginVersion);
        adminInfo.add("&aLatest version:&3 " + latest);
        adminInfo.add("&aPlugin by:&6 " + authors);
        adminInfo.add("&aContributors:&6 " + contributors);
        adminInfo.add("&aNukkit Version:&3 " + nukkitVersion);
        adminInfo.add("&aNukkit Api Version:&3 " + apiVersion);
        adminInfo.add("&aServer Version:&3 " + serverVersion);
        adminInfo.add(" ");
        adminInfo.add("&1Modules");
        adminInfo.add("&aFormatter&3: " + StatusUtil.getFormaterStatus());
        adminInfo.add("&aOsTag&3: " + StatusUtil.getOsTagStatus());
        adminInfo.add(" ");
        adminInfo.add("&1Plugins");
        adminInfo.add("&aLuckPerms&3: " + StatusUtil.getLuckPermStatus());
        adminInfo.add("&aKotlinLib & PlaceholderAPI&3: " + StatusUtil.getKotOrPapiStatus());
        adminInfo.add("&aFormConstructor&3: " + StatusUtil.getFormConstructor());
        adminInfo.add(" ");
        if (this.lines) {
            adminInfo.add("&b-------------------------------");
        }

        return adminInfo;
    }
}