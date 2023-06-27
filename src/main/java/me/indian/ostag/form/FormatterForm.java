package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerMentionConfig;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Dropdown;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Label;
import ru.contentforge.formconstructor.form.element.SelectableElement;
import ru.contentforge.formconstructor.form.element.StepSlider;
import ru.contentforge.formconstructor.form.element.Toggle;

import java.util.ArrayList;
import java.util.List;

public class FormatterForm {

    private final Form mainForm;
    private final OsTag plugin;
    private final Config config;
    private final Player player;
    private List<String> blockedWords;

    public FormatterForm(final Form mainForm, final Config config) {
        this.mainForm = mainForm;
        this.plugin = OsTag.getInstance();
        this.config = config;
        this.player = this.mainForm.getFormPlayer();
        this.blockedWords = config.getStringList("BlackWords");
    }

    public void formatterSettings() {
        final SimpleForm form = new SimpleForm("Formatter Settings");

        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Chat Format", ImageType.PATH, "textures/ui/editIcon", (p, button) -> messageFormatSettings());
        }
        if (this.config.getBoolean("MentionSound")) {
            form.addButton("Mention", ImageType.PATH, "textures/ui/icon_bell", (p, button) -> mentionForm());
        }
        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Cooldown", ImageType.PATH, "textures/ui/timer", (p, button) -> cooldownSettings())
                    .addButton("Censorship", ImageType.PATH, "textures/ui/mute_on", (p, button) -> censorShipSettings());
        }

        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.openSettings());
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
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });
        form.setNoneHandler(p -> formatterSettings());
        form.send(player);
    }

    private void cooldownSettings() {
        final CustomForm form = new CustomForm("Cooldown Settings");
        final boolean enabled = config.getBoolean("cooldown.enable");

        form.addElement(new Label(MessageUtil.colorize("&aEnable cooldown")))
                .addElement("cooldown_enable", new Toggle("Cooldown", enabled));

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
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(player);
    }

    private void censorShipSettings() {
        final CustomForm form = new CustomForm("CensorShip Settings");
        final boolean enabled = config.getBoolean("censorship.enable");

        form.addElement(new Label(MessageUtil.colorize("&aEnable censor ship")))
                .addElement("censor_enable", new Toggle("Censorship", enabled));

        if (enabled) {
            form.addElement("censor",
                    Input.builder()
                            .setName(MessageUtil.colorize("&aCensorShip message"))
                            .setDefaultValue(config.getString("censorship.word"))
                            .build());

            form.addElement(new Label(MessageUtil.colorize("&aBlocked words")));
            for (int i = 0; i < blockedWords.size(); i++) {
                form.addElement("blackwords_" + i,
                        Input.builder()
                                .setName((i + 1) + ".")
                                .setDefaultValue(blockedWords.get(i))
                                .build());

            }
            form.addElement("blockword",
                    Input.builder()
                            .setName(MessageUtil.colorize("&lBlock an word"))
                            .setDefaultValue("ExampleWord")
                            .build());
        }

        form.setHandler((p, response) -> {
            config.set("censorship.enable", response.getToggle("censor_enable").getValue());
            if (enabled) {
                final List<String> finalBlocked = new ArrayList<>();
                config.set("censorship.word", response.getInput("censor").getValue().charAt(0));

                for (int i = 0; i < blockedWords.size(); i++) {
                    final String word = response.getInput("blackwords_" + i).getValue();
                    if (!word.isEmpty()) {
                        finalBlocked.add(word);
                    }
                }

                final String blockedWord = response.getInput("blockword").getValue();
                if (!blockedWord.isEmpty() && !blockedWord.equalsIgnoreCase("ExampleWord")) {
                    finalBlocked.add(blockedWord);
                }

                blockedWords = finalBlocked;
                config.set("BlackWords", blockedWords);
            }
            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(player);
    }


    private void mentionForm() {
          /*
           This is for Mention Sound function and it is still experimental
         */

        final CustomForm form = new CustomForm("Mentions Settings");
        final PlayerMentionConfig playerMentionConfig = this.plugin.getPlayersConfig();
        final boolean enabled = playerMentionConfig.hasEnabledMentions(this.player);
        final boolean title = playerMentionConfig.hasEnabledTitle(this.player);
        final int soundsValue = playerMentionConfig.getSoundsValue();
        final List<SelectableElement> elements = new ArrayList<>();
        final List<SelectableElement> maxsounds = new ArrayList<>();
        final int currentSoundIndex = playerMentionConfig.getSoundIndex(playerMentionConfig.getMentionSound(this.player));

        if (enabled) {
            int counter = 0;
            for (final Sound sound : Sound.values()) {
                final SelectableElement element = new SelectableElement(sound.name(), counter);
                elements.add(element);
                counter++;
                if (counter == soundsValue) {
                    if (currentSoundIndex > counter) {
                        final SelectableElement currentSound = new SelectableElement(playerMentionConfig.getSoundByIndex(currentSoundIndex), counter);
                        playerMentionConfig.setPlayerCustomIndex(this.player, counter);
                        elements.add(currentSound);
                    }
                    break;
                }
            }
        }

        form.addElement(new Label(MessageUtil.colorize("&aEnable mentions")))
                .addElement("mentions_enable", new Toggle("Mention", enabled));

        if (enabled) {
            form.addElement(new Label(MessageUtil.colorize("&aEnable title info")))
                    .addElement("title_info_enable", new Toggle("Title info", title))
                    .addElement(new Label(MessageUtil.colorize("&aYour mention sound: &b" + playerMentionConfig.getMentionSound(this.player))))
                    .addElement("soundname", new Dropdown(MessageUtil.colorize("&aMention Sound"), elements, playerMentionConfig.getPlayerCustomIndex(this.player)))
                    .addElement("customsound",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aAdd sound by index"))
                                    .build());
        }

        if (this.player.hasPermission(Permissions.ADMIN)) {
            for (int i = 1; i <= soundsValue + 100; i++) {
                final SelectableElement element = new SelectableElement(String.valueOf(i), i);
                maxsounds.add(element);
            }
            form.addElement("maxsounds", new StepSlider("Sounds value", maxsounds, soundsValue - 1));
        }

        form.setHandler((p, response) -> {
            final boolean finalEnabled = response.getToggle("mentions_enable").getValue();
            if (finalEnabled != enabled) {
                playerMentionConfig.setEnabledMentions(this.player, finalEnabled);
            }

            if (enabled) {
                final boolean finalTitle = response.getToggle("title_info_enable").getValue();
                final SelectableElement element = response.getDropdown("soundname").getValue();
                playerMentionConfig.setEnabledTitle(this.player, finalTitle);
                if (element.getValue() != null) {
                    if (element.getValue(Integer.class) != playerMentionConfig.getPlayerCustomIndex(this.player)) {
                        playerMentionConfig.setMentionSound(this.player, playerMentionConfig.getSoundByIndex(element.getValue(Integer.class)));
                    }
                }
            }

            final String customSound = response.getInput("customsound").getValue();
            if (customSound != null) {
                int index;
                try {
                    index = Integer.parseInt(customSound);
                    playerMentionConfig.setMentionSound(this.player, playerMentionConfig.getSoundByIndex(index));
                } catch (final NumberFormatException ex) {
                    player.sendMessage(MessageUtil.colorize("&cIndex must be an integer"));
                }
            }

            if (this.player.hasPermission(Permissions.ADMIN)) {
                final SelectableElement sounds = response.getStepSlider("maxsounds").getValue();
                if (sounds.getValue() != null && sounds.getValue(Integer.class) != soundsValue) {
                    playerMentionConfig.setSoundsValue(sounds.getValue(Integer.class));
                }
            }

            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(player);
    }
}