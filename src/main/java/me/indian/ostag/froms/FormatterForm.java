package me.indian.ostag.froms;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Toggle;

public class FormatterForm {

    private final Form mainForm;
    private final OsTag plugin;
    private final Config config;
    private final Player player;

    public FormatterForm(final Form mainForm, final Config config) {
        this.mainForm = mainForm;
        this.plugin = OsTag.getInstance();
        this.config = config;
        this.player = this.mainForm.getFormPlayer();
    }

    public void formatterSettings() {
        final SimpleForm form = new SimpleForm("Formatter Settings");

        form.addButton("Message Format", ImageType.PATH, "textures/ui/text_color_paintbrush", (p, button) -> messageFormatSettings())
                .addButton("Cooldown", ImageType.PATH, "textures/ui/timer", (p, button) -> cooldownSettings());

        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.getSettings().settings());
        form.send(player);
    }

    private void messageFormatSettings() {
        final CustomForm form = new CustomForm("Cooldown Settings");

        form.addElement("message_format",
                Input.builder()
                        .setName(MessageUtil.colorize("&lMessage format"))
                        .setDefaultValue(config.getString("message-format"))
                        .build());

        form.setHandler((p, response) -> {
            if (!response.getInput("message_format").getValue().isEmpty()) {
                config.set("message-format", response.getInput("message_format").getValue());
                config.save();
                p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            }
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(player);
    }

    private void cooldownSettings() {
        final CustomForm form = new CustomForm("Cooldown Settings");
        final boolean enabled = config.getBoolean("cooldown.enable");

        form.addElement("cooldown_enable", new Toggle("Cooldown", enabled));


        if (enabled) {
            form.addElement("delay",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown time"))
                                    .setDefaultValue(String.valueOf(config.getLong("cooldown.delay")))
                                    .build())
                    .addElement("message",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown message"))
                                    .setDefaultValue(config.getString("cooldown.message"))
                                    .build())
                    .addElement("over",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown over message"))
                                    .setDefaultValue(config.getString("cooldown.over"))
                                    .build())
                    .addElement("disabled",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown disabled message"))
                                    .setDefaultValue(config.getString("cooldown.disabled"))
                                    .build())

                    .addElement("bypass",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown bypass message"))
                                    .setDefaultValue(config.getString("cooldown.bypass"))
                                    .build());
        }

        form.setHandler((p, response) -> {
            config.set("cooldown.enable", response.getToggle("cooldown_enable").getValue());
            if (enabled) {
                config.set("cooldown.delay", Integer.valueOf(response.getInput("delay").getValue()));
                config.set("cooldown.message", response.getInput("message").getValue());
                config.set("cooldown.over", response.getInput("over").getValue());
                config.set("cooldown.disabled", response.getInput("disabled").getValue());
                config.set("cooldown.bypass", response.getInput("bypass").getValue());
            }
            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(player);
    }
}
