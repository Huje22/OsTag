package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import com.formconstructor.form.SimpleForm;
import com.formconstructor.form.element.simple.ImageType;
import java.util.List;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.PluginInfoUtil;

/*
 Settings in forms is a pre-feature
 */

public class Form {

    private final OsTag plugin;
    private final Player player;
    private final String debugPrefix;
    private final Config config;
    private final List<String> advancedPlayers;
    private final SettingsFrom settings;

    public Form(final Player player) {
        this.player = player;
        this.plugin = OsTag.getInstance();
        this.debugPrefix = MessageUtil.colorize(plugin.publicDebugPrefix + "&8[&dForms&8] ");
        this.config = this.plugin.getConfig();
        this.advancedPlayers = config.getStringList("advanced-players");
        this.settings = new SettingsFrom(this, config, this.advancedPlayers);
    }

    public void runOstagFrom() {
        final SimpleForm form = new SimpleForm(MessageUtil.colorize("&bOsTag &fMenu"));

        form.setContent(MessageUtil.colorize("&aJoin our discord\n"))
                .addContent(MessageUtil.colorize("&bhttps://discord.gg/k69htTFCVk"))
                .addButton("Version", ImageType.PATH, "textures/ui/infobulb", (p, button) -> versionInfo());

        if (player.isOp() || player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Reload", ImageType.PATH, "textures/ui/refresh", (p, button) -> reloadConfig())
                    .addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> upDate());
        }
        form.addButton("Settings", ImageType.PATH, "textures/ui/icon_setting", (p, button) -> this.openSettings());

        form.send(player);
    }

    private void versionInfo() {
        final SimpleForm form = new SimpleForm("Version info");
        final PluginInfoUtil infoUtil = new PluginInfoUtil(player, true);

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
            this.formLogger("&aPlayer&6 " + p.getName() + "&a reloading config");
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
                form.addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> {
                    plugin.getUpdateUtil().manualUpDate(player);
                    this.formLogger("&aPlayer&6 " + p.getName() + "&a trying to update plugin");
                });
            }
        } else {
            if (!GithubUtil.getFastTagInfo().contains("true")) {
                form.setContent(MessageUtil.colorize("&cYou can't download latest version"))
                        .addButton("Force UpDate", ImageType.PATH, "textures/ui/ErrorGlyph_small", (p, button) -> {
                            plugin.getUpdateUtil().manualUpDate(player);
                            this.formLogger("&aPlayer&6 " + p.getName() + "&a trying to force update plugin");
                        });
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

    public void openSettings() {
       this.settings.settings();
    }

    public void formLogger(final String log) {
        if (this.config.getBoolean("FormsDebug")) {
            plugin.getLogger().info(MessageUtil.colorize(debugPrefix + log));
            MessageUtil.sendMessageToAdmins(debugPrefix + log);
        }
    }

    public void test() {
        final SimpleForm form = new SimpleForm("Sample title");
        form.setContent("This is a text")
                .addContent("\nThis is addition :3")
                .addButton("Test1", ImageType.PATH, "textures/ui/settings_glyph_color_2x", (p, button) -> p.sendMessage(String.valueOf(button.getIndex())))
                .addButton("Test2", ImageType.PATH, "textures/ui/icon_setting", (p, button) -> p.sendMessage(String.valueOf(button.getIndex())))
                .addButton("Test3", ImageType.PATH, "textures/ui/refresh", (p, button) -> p.sendMessage(String.valueOf(button.getIndex())))
                .addButton("Test4", ImageType.PATH, "textures/ui/refresh_light", (p, button) -> p.sendMessage(String.valueOf(button.getIndex())));

        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }
}