package me.indian.ostag.from;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Label;
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

        form.addButton("Chat Format", ImageType.PATH, "textures/ui/text_color_paintbrush", (p, button) -> messageFormatSettings())
                .addButton("Cooldown", ImageType.PATH, "textures/ui/timer", (p, button) -> cooldownSettings());

        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.getSettings().settings());
        form.send(player);
    }

    private void messageFormatSettings() {
        final CustomForm form = new CustomForm("Chat Format");
        final boolean andForAll = config.getBoolean("And-for-all");
        final boolean breaksBetweenMessages = config.getBoolean("break-between-messages.enable");

        form.addElement("message_format",
                        Input.builder()
                                .setName(MessageUtil.colorize("&lMessage format"))
                                .setDefaultValue(config.getString("message-format"))
                                .build())
                .addElement(new Label(MessageUtil.colorize("&lAdditional chat features")))
                .addElement("and-for-all", new Toggle("And for all", andForAll))
                .addElement("breaks", new Toggle("Breaks between messages", breaksBetweenMessages));

        form.setHandler((p, response) -> {
            final String messageFormat = response.getInput("message_format").getValue();
            if (!messageFormat.isEmpty()) {
                config.set("message-format", messageFormat);
            }

            final boolean finalAndForAll = response.getToggle("and-for-all").getValue();
            if (finalAndForAll != andForAll) {
                config.set("And-for-all", finalAndForAll);
            }

            final boolean finalBreaksBetweenMessages = response.getToggle("breaks").getValue();
            if (finalBreaksBetweenMessages != breaksBetweenMessages) {
                config.set("break-between-messages.enable", finalBreaksBetweenMessages);
            }

            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));

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
