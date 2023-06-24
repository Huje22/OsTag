package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.indian.ostag.util.MessageUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.element.*;

import java.util.Arrays;
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

    public void cpsLimiterSettings() {
        final CustomForm form = new CustomForm("Cooldown Settings");
        final String message = config.getString("Cps.message");
        final int maxCps = config.getInt("Cps.max", 1);
        final List<SelectableElement> elements = Arrays.asList(
                new SelectableElement(MessageUtil.colorize("&a1 \n&a"), 1),
                new SelectableElement(MessageUtil.colorize("&a2 \n&a"), 2),
                new SelectableElement(MessageUtil.colorize("&a3 \n&e"), 3),
                new SelectableElement(MessageUtil.colorize("&e4 \n&c"), 4),
                new SelectableElement(MessageUtil.colorize("&e5 \n&c"), 5),
                new SelectableElement(MessageUtil.colorize("&e6 \n&c"), 6),
                new SelectableElement(MessageUtil.colorize("&e7 \n&c"), 7),
                new SelectableElement(MessageUtil.colorize("&c8 \n&c"), 8),
                new SelectableElement(MessageUtil.colorize("&c9 \n&c"), 9),
                new SelectableElement(MessageUtil.colorize("&410 \n&c"), 10),
                new SelectableElement(MessageUtil.colorize("&a11 \n&a"), 11),
                new SelectableElement(MessageUtil.colorize("&a12 \n&a"), 12),
                new SelectableElement(MessageUtil.colorize("&a13 \n&e"), 13),
                new SelectableElement(MessageUtil.colorize("&e14 \n&c"), 14),
                new SelectableElement(MessageUtil.colorize("&e15 \n&c"), 15),
                new SelectableElement(MessageUtil.colorize("&e16 \n&c"), 16),
                new SelectableElement(MessageUtil.colorize("&e17 \n&c"), 17),
                new SelectableElement(MessageUtil.colorize("&c18 \n&c"), 18),
                new SelectableElement(MessageUtil.colorize("&c19 \n&c"), 19),
                new SelectableElement(MessageUtil.colorize("&420 \n&c"), 20)

        );

        form.addElement("maxcps", new StepSlider("Max Cps", elements, maxCps -1))
                .addElement("message",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aCps limit reached message"))
                                .setDefaultValue(message)
                                .build());

        form.setHandler((p, response) -> {
            final SelectableElement element = response.getStepSlider("maxcps").getValue();
            final String finalMessage = response.getInput("message").getValue();

            if ((element.getValue() != null && element.getValue(Integer.class) != maxCps) && (finalMessage != null && finalMessage.equalsIgnoreCase(message))) {
                this.config.set("Cps.max", element.getValue(Integer.class));
                this.config.set("Cps.message", finalMessage);

                this.config.save();
                p.sendMessage(MessageUtil.colorize("&aSaved changes"));
                this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
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