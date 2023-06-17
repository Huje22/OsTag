package me.indian.ostag.from;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.PluginInfoUtil;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;

import java.util.List;

/*
 Settings in forms is a pre-feature
 */

public class Form {

    private final OsTag plugin;
    private final Player player;
    private final List<String> advancedPlayers;
    private final SettingsFrom settings;

    public Form(final Player player) {
        this.player = player;
        this.plugin = OsTag.getInstance();
        final Config config = this.plugin.getConfig();
        this.advancedPlayers = config.getStringList("advanced-players");
        this.settings = new SettingsFrom(this, config, this.advancedPlayers);
    }

    public void runOstagFrom() {
        if (!plugin.formConstructor) {
            if (player.isOp()) {
                player.sendMessage(MessageUtil.colorize("&cYou don't have &bFormConstructor&b plugin !"));
                player.sendMessage(MessageUtil.colorize("&cDownload it from here &bhttps://github.com/OpenPlugins-Minecraft/OsTag/tree/main/libs!"));
            } else {
                player.sendMessage(MessageUtil.colorize("&cYThis server don't have &bFormConstructor&b plugin !"));
            }
            return;
        }

        final SimpleForm form = new SimpleForm(MessageUtil.colorize("&bOsTag &fMenu"));

        form.setContent(MessageUtil.colorize("&aJoin our discord\n"))
                .addContent(MessageUtil.colorize("&bhttps://discord.gg/k69htTFCVk"))
                .addButton("Version", ImageType.PATH, "textures/ui/infobulb", (p, button) -> versionInfo());

        if (player.isOp() || player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Reload", ImageType.PATH, "textures/ui/refresh", (p, button) -> reloadConfig())
                    .addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> upDate())
                    .addButton("Settings", ImageType.PATH, "textures/ui/icon_setting", (p, button) -> this.getSettings().settings());
        }

        form.send(player);
    }

    private void versionInfo() {
        final SimpleForm form = new SimpleForm("Version info");
        final PluginInfoUtil infoUtil = new PluginInfoUtil(player , true);

        if (player.hasPermission(Permissions.ADMIN)) {
            for (final String adminInfo : infoUtil.getAdminInfo()) {
                form.addContent(MessageUtil.colorize(adminInfo) + "\n");
            }
        } else {
            for (final String normalInfo : infoUtil.getNormalInfo()) {
                form.addContent(MessageUtil.colorize(normalInfo) + "\n");
            }
        }
        form.addButton(MessageUtil.colorize("Run &7/ostag version"), (p, button) -> MessageUtil.playerCommand(player, "ostag version"));

        this.addCloseButton(form);
        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    private void reloadConfig() {
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

    private void upDate() {
        final SimpleForm form = new SimpleForm("UpDate menu");
        final String downloadStatus = MessageUtil.colorize(plugin.getUpdateUtil().getDownloadStatus());

        form.addContent(MessageUtil.colorize("&aDownload status") + "\n")
                .addContent(downloadStatus + "\n");

        if (GithubUtil.getFastTagInfo().contains("false")) {
            if (!downloadStatus.equalsIgnoreCase(MessageUtil.colorize("&cYou have downloaded the latest version but you are not using it"))) {
                form.addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> plugin.getUpdateUtil().manualUpDate(player));
            }
        } else {
            if (!GithubUtil.getFastTagInfo().contains("true")) {
                form.setContent(MessageUtil.colorize("&cYou can't download latest version"))
                        .addButton("Force UpDate", ImageType.PATH, "textures/ui/ErrorGlyph_small", (p, button) -> plugin.getUpdateUtil().manualUpDate(player));
            } else {
                form.setContent(MessageUtil.colorize("&aYou have latest version!"));
            }
        }

        this.addCloseButton(form);
        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    public void addCloseButton(final SimpleForm form) {
        form.addButton("Close", ImageType.PATH, "textures/ui/redX1", null);
    }

    public boolean onlineByName(final String name) {
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
}