package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerMentionConfig;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.ModalForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Label;
import ru.contentforge.formconstructor.form.element.Toggle;

import java.util.List;

public class SettingsFrom {

    private final Form mainForm;
    private final OsTag plugin;
    private final Config config;
    private final Player player;
    private final List<String> advancedPlayers;

    public SettingsFrom(final Form mainForm, final Config config, final List<String> advancedPlayers) {
        this.mainForm = mainForm;
        this.plugin = OsTag.getInstance();
        this.config = config;
        this.player = this.mainForm.getFormPlayer();
        this.advancedPlayers = advancedPlayers;
    }

    public void settings() {
        final SimpleForm form = new SimpleForm("Settings");

        if (plugin.osTag && this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("OsTag", ImageType.PATH, "textures/ui/sidebar_icons/profile_screen_icon", (p, button) -> new OsTagForm(this.mainForm, this.config, this.advancedPlayers).osTagSettings());
        }
        if (plugin.chatFormatter) {
            form.addButton("Formatter", ImageType.PATH, "textures/ui/mute_off", (p, button) -> new FormatterForm(this.mainForm, this.config).formatterSettings());
        }
        if (plugin.cpsLimiter && this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("CpsLimiter", ImageType.PATH, "textures/ui/cursor_gamecore", (p, button) -> new CpsLimiterForm(this.mainForm, this.config).cpsLimiter());
        }
        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Modules", ImageType.PATH, "textures/ui/servers", (p, button) -> modules());
        }
        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.runOstagFrom());
        form.send(player);
    }

    private void modules() {
        final CustomForm form = new CustomForm("Modules settings");
        final PlayerMentionConfig playerMentionConfig = this.plugin.getPlayersMentionConfig();

        form.addElement(new Label(MessageUtil.colorize("&a&lRestart no needed!")));
        form.addElement("autoupdate", new Toggle("Auto Update", this.config.getBoolean("AutoUpdate")))
                .addElement("formsdebug", new Toggle("Forms debug", this.config.getBoolean("FormsDebug")));



        form.addElement(new Label(MessageUtil.colorize("&c&lThis booleans needed restart server to reload!")))
                .addElement("update", new Toggle("Update checker", plugin.upDatechecker))
                .addElement("ostag", new Toggle("Ostag", plugin.osTag))
                .addElement("formatter", new Toggle("ChatFormatter", plugin.chatFormatter))
                .addElement("cpslimiter", new Toggle("CpsLimiter", plugin.cpsLimiter))
                .addElement("debug", new Toggle("Debug", plugin.debug))
                .addElement("movement", new Toggle("Server movement", plugin.serverMovement));


        form.setHandler((p, response) -> {

            // not restart

            config.set("AutoUpdate", response.getToggle("autoupdate").getValue());
            config.set("FormsDebug", response.getToggle("formsdebug").getValue());
            //restart

            config.set("UpdateChecker", response.getToggle("update").getValue());
            config.set("OsTag", response.getToggle("ostag").getValue());
            config.set("ChatFormatter", response.getToggle("formatter").getValue());
            config.set("CpsLimiter", response.getToggle("cpslimiter").getValue());
            config.set("Debug", response.getToggle("debug").getValue());
            config.set("Movement-server", response.getToggle("movement").getValue());

            config.save();
            p.sendMessage(MessageUtil.colorize("&aThis changes may need restart server"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a trying to edit&b " + form.getTitle());
            if (p.isOp()) {
                this.reloadServer();
            } else {
                settings();
            }
        });
        form.setNoneHandler(p -> this.settings());
        form.send(player);
    }

    private void reloadServer() {
        final ModalForm form = new ModalForm("Server reload");

        form.setContent("Reload the server?\n")
                .addContent(MessageUtil.colorize("&n&m&lThis may negatively affect some plugins!"))
                .setPositiveButton(MessageUtil.colorize("&cYes"))
                .setNegativeButton(MessageUtil.colorize("&aNo"));

        form.setNoneHandler(p -> {
            p.sendMessage(MessageUtil.colorize("&aAction canceled :)"));
            this.settings();
        });

        form.setHandler((p, result) -> {
            if (result) {

                MessageUtil.playerCommand(p, "reload");

            } else {
                p.sendMessage(MessageUtil.colorize("&aAction canceled :)"));
                this.settings();
            }
        });
        form.send(player);
    }
}