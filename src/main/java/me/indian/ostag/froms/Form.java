package me.indian.ostag.froms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.StatusUtil;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;

import java.util.List;


public class Form {

    private final OsTag plugin;
    private final Config config;
    private final Player player;
    private final List<String> advancedPlayers;
    private final SettingsFrom settings;

    public Form(final Player player) {
        this.player = player;
        this.plugin = OsTag.getInstance();
        this.config = this.plugin.getConfig();
        this.advancedPlayers = this.config.getStringList("advanced-players");
        this.settings = new SettingsFrom(this, this.config, this.advancedPlayers);
    }

    public void runOstagFrom() {
        if (plugin.getServer().getPluginManager().getPlugin("FormConstructor") == null) {
            String error = MessageUtil.colorize("&cYou dont have &bFormConstructor &b plugin !");
            String error2 = MessageUtil.colorize("&cDownload it from here &bhttps://github.com/OpenPlugins-Minecraft/OsTag/tree/main/libs!");
            plugin.getLogger().error(error);
            plugin.getLogger().error(error2);
            player.sendMessage(error);
            player.sendMessage(error2);
            return;
        }

        final SimpleForm form = new SimpleForm(MessageUtil.colorize("&bOsTag &fMenu"));

        form.setContent(MessageUtil.colorize("&aJoin our discord\n"))
                .addContent(MessageUtil.colorize("&bhttps://discord.gg/k69htTFCVk"))
                .addButton("Version", ImageType.PATH, "textures/ui/infobulb", (p, button) -> versionInfo());

        if (player.isOp() || player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Reload", ImageType.PATH, "textures/ui/refresh", (p, button) -> reloadConfig())
                    .addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> upDate())
                    .addButton("Settings", ImageType.PATH, "textures/ui/icon_setting", (p, button) -> settings.settings());
        }

        form.send(player);
    }

    public void versionInfo() {
        final SimpleForm form = new SimpleForm("Version info");

        final PluginDescription descriptor = plugin.getDescription();
        final Server server = plugin.getServer();

        final String pluginVersion = descriptor.getVersion();
        final String authors = String.valueOf(descriptor.getAuthors()).replace("[", "").replace("]", "");
        final String nukkitVersion = server.getNukkitVersion();
        final String serverVersion = server.getVersion();
        final String apiVersion = server.getApiVersion();
        final String latest = GithubUtil.getFastTagInfo() + GithubUtil.getBehindCount(player);

        if (player.hasPermission(Permissions.ADMIN) || player.isOp()) {
            form.addContent(MessageUtil.colorize("&aOsTag version:&3 " + pluginVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aLatest version:&3 " + latest + "\n"))
                    .addContent(MessageUtil.colorize("&aPlugin by:&6 " + authors + "\n"))
                    .addContent(MessageUtil.colorize("&aNukkit Version:&3 " + nukkitVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aNukkit Api Version:&3 " + apiVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aServer Version:&3 " + serverVersion + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"))
                    .addContent(MessageUtil.colorize("&1Modules" + "\n"))
                    .addContent(MessageUtil.colorize("&aFormatter&3: " + StatusUtil.getFormaterStatus() + "\n"))
                    .addContent(MessageUtil.colorize("&aOsTag&3: " + StatusUtil.getOsTagStatus() + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"))
                    .addContent(MessageUtil.colorize("&1Plugins" + "\n"))
                    .addContent(MessageUtil.colorize("&aLuckPerms&3: " + StatusUtil.getLuckPermStatus() + "\n"))
                    .addContent(MessageUtil.colorize("&aKotlinLib & PlaceholderAPI&3: " + StatusUtil.getKotOrPapiStatus() + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"));
        } else {
            form.addContent(MessageUtil.colorize("&aOsTag version:&3 " + pluginVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aLatest version:&3 " + latest + "\n"))
                    .addContent(MessageUtil.colorize("&aPlugin by:&6 " + authors + "\n"))
                    .addContent(MessageUtil.colorize("&aServer Version:&3 " + serverVersion + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"))
                    .addContent(MessageUtil.colorize("&1Modules" + "\n"))
                    .addContent(MessageUtil.colorize("&aFormatter&3: " + StatusUtil.getFormaterStatus() + "\n"))
                    .addContent(MessageUtil.colorize("&aOsTag&3: " + StatusUtil.getOsTagStatus() + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"));
        }

        form.addButton(MessageUtil.colorize("Run &7/ostag version"), (p, button) -> MessageUtil.playerCommand(player, "ostag version"));

        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    public void reloadConfig() {
        final SimpleForm form = new SimpleForm("Reload Config");
        final String lastException = plugin.getOsTagCommand().getLastException();

        form.addContent(MessageUtil.colorize("&aLast error\n"));
        if (lastException.isEmpty()) {
            form.addContent(MessageUtil.colorize("&aEverything is fine"));
        } else {
            form.addContent(MessageUtil.colorize("&c") + lastException);
        }

        form.addButton("Reload config", ImageType.PATH, "textures/ui/refresh", (p, button) -> {
            plugin.getOsTagCommand().reloadConfig(player);
            this.reloadConfig();
        });

        this.addCloseButton(form);
        form.setNoneHandler((p -> runOstagFrom()));
        form.send(player);
    }

    public void upDate() {
        final SimpleForm form = new SimpleForm("UpDate menu");

        if (GithubUtil.getFastTagInfo().contains("false")) {
            form.setContent(MessageUtil.colorize("&aYou can download latest version"))
                    .addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> plugin.getUpdateUtil().manualUpDate(player));
        } else {
            if (!GithubUtil.getFastTagInfo().contains("true")) {
                form.setContent(MessageUtil.colorize("&aYou can't download lates"))
                        .addButton("Force UpDate", ImageType.PATH, "textures/ui/ErrorGlyph_small", (p, button) -> plugin.getUpdateUtil().manualUpDate(player));
            } else {
                form.setContent(MessageUtil.colorize("&aYou have latest version!"));
            }
        }

        this.addCloseButton(form);
        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    public void test() {
        final SimpleForm form = new SimpleForm("Sample title");
        form.setContent("This is a text")
                .addContent("\nThis is addition :3")
                .addButton("Test1", ImageType.PATH, "textures/ui/settings_glyph_color_2x", (p, button) -> p.sendMessage(String.valueOf(button.index)))
                .addButton("Test2", ImageType.PATH, "textures/ui/icon_setting", (p, button) -> p.sendMessage(String.valueOf(button.index)))
                .addButton("Test3", ImageType.PATH, "textures/ui/refresh", (p, button) -> p.sendMessage(String.valueOf(button.index)))
                .addButton("Test4", ImageType.PATH, "textures/ui/refresh_light", (p, button) -> p.sendMessage(String.valueOf(button.index)));

        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    public void addCloseButton(final SimpleForm form) {
        form.addButton("Close", ImageType.PATH, "textures/ui/redX1", null);
    }

    public boolean online(final String name) {
        final Player player = plugin.getServer().getPlayer(name);
        return player != null;
    }

    public boolean isAdvancedPlayer(final String name) {
        return advancedPlayers.contains(name);
    }

    public Player getFormPlayer() {
        return this.player;
    }

    public SettingsFrom getSettings() {
        return this.settings;
    }
}