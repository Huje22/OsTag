package me.indian.ostag.froms;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import ru.contentforge.formconstructor.form.CustomForm;
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

        if (plugin.osTag) {
            form.addButton("OsTag", ImageType.PATH, "textures/items/name_tag", (p, button) -> new OsTagForm(this.mainForm, this.config, this.advancedPlayers).osTagSettings());
        }
        if (plugin.chatFormatter) {
            form.addButton("Formatter", ImageType.PATH, "textures/ui/chat_send", (p, button) -> new FormatterForm(this.mainForm, this.config).formatterSettings());
        }
        form.addButton("Modules", ImageType.PATH, "textures/ui/servers", (p, button) -> modulesSettings());

        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.runOstagFrom());
        form.send(player);
    }


    private void modulesSettings() {
        final CustomForm form = new CustomForm("Modules settings");

        form.addElement(new Label(MessageUtil.colorize("&a&lRestart no needed!")));
        form.addElement("autoupdate", new Toggle("Auto Update", this.config.getBoolean("AutoUpdate")));


        form.addElement(new Label(MessageUtil.colorize("&c&lThis booleans needed restart server to reload!")));
        form.addElement("update", new Toggle("Update checker", plugin.upDatechecker));
        form.addElement("ostag", new Toggle("Ostag", plugin.osTag));
        form.addElement("formatter", new Toggle("ChatFormatter", plugin.chatFormatter));
        form.addElement("debug", new Toggle("Debug", plugin.debug));
        form.addElement("movement", new Toggle("Server movement", plugin.serverMovement));


        form.setHandler((p, response) -> {

            //restart

            config.set("UpdateChecker", response.getToggle("update").getValue());
            config.set("OsTag", response.getToggle("ostag").getValue());
            config.set("ChatFormatter", response.getToggle("formatter").getValue());
            config.set("Debug", response.getToggle("debug").getValue());
            config.set("Movement-server", response.getToggle("movement").getValue());

            // not restart

            config.set("AutoUpdate", response.getToggle("autoupdate").getValue());
            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            settings();
        });

        form.setNoneHandler(p -> this.settings());
        form.send(player);
    }
}