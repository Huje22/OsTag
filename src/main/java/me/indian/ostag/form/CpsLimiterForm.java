package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.util.MessageUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.*;

import java.util.ArrayList;
import java.util.List;

public class CpsLimiterForm {

    private final Form mainForm;
    private final Config config;
    private final Player player;

    public CpsLimiterForm(final Form mainForm, final Config config) {
        this.mainForm = mainForm;
        this.config = config;
        this.player = this.mainForm.getFormPlayer();
    }

    public void cpsLimiter() {
        final CustomForm form = new CustomForm("CpsLimiter Settings");
        final String cpsMessage = config.getString("Cps.message");
        final int maxCps = config.getInt("Cps.max", 1);
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

            if (element.getValue() != null && finalMessage != null) {
                if (element.getValue(Integer.class) != maxCps && finalMessage.equalsIgnoreCase(cpsMessage)) {

                    this.config.set("Cps.max", element.getValue(Integer.class));
                    this.config.set("Cps.message", finalMessage);

                    this.config.save();
                    p.sendMessage(MessageUtil.colorize("&aSaved changes"));
                    this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
                }
            } else {
                p.sendMessage(MessageUtil.colorize("&cCant save changes!"));
                this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&c trying edit&b " + form.getTitle() + "&c something went wrong!");
            }
            this.mainForm.openSettings();
        });

        form.setNoneHandler(p -> this.mainForm.openSettings());
        form.send(player);
    }
}