package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.SelectableElement;
import ru.contentforge.formconstructor.form.element.StepSlider;

import java.util.ArrayList;
import java.util.List;

public class CpsLimiterForm {

    private final OsTag plugin;
    private final Form mainForm;
    private final Config config;
    private final Player player;

    public CpsLimiterForm(final OsTag plugin, final Form mainForm) {
        this.plugin = plugin;
        this.mainForm = mainForm;
        this.config = this.plugin.getConfig();
        this.player = this.mainForm.getFormPlayer();
    }

    public void cpsLimiter() {
        final CustomForm form = new CustomForm("CpsLimiter Settings");
        final String cpsMessage = this.plugin.getCpsLimiter().getMessage();
        final int maxCps = this.plugin.getCpsLimiter().getMaxCps();
        final List<SelectableElement> elements = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            String format = "&a";
            if (i >= 20) {
                format = "&e";
            }
            if (i >= 40) {
                format = "&c";
            }
            if (i >= 40) {
                format = "&4";
            }

            final String message = MessageUtil.colorize(format + i);
            final SelectableElement element = new SelectableElement(message, i);
            elements.add(element);
        }
        form.addElement("maxcps", new StepSlider("Max Cps", elements, maxCps - 1))
                .addElement("message",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aCps limit reached message"))
                                .setDefaultValue(cpsMessage)
                                .build());

        form.setHandler((p, response) -> {
            final SelectableElement element = response.getStepSlider("maxcps").getValue();
            final String finalMessage = response.getInput("message").getValue();

            if (element.getValue() != null && element.getValue(Integer.class) != maxCps) {
                this.plugin.getCpsLimiter().setMaxCps(element.getValue(Integer.class));
            }

            if (finalMessage != null && !finalMessage.equals(cpsMessage)) {
                this.plugin.getCpsLimiter().setMessage(finalMessage);
            }

            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            this.mainForm.openSettings();
        });

        form.setNoneHandler(p -> this.mainForm.openSettings());
        form.send(player);
    }
}