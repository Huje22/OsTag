package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerMentionConfig;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.ModalForm;
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
    private final Config config;
    private final Player player;
    private List<String> blockedWords;
    private final PlayerMentionConfig playerMentionConfig;

    public FormatterForm(final Form mainForm, final Config config) {
        this.mainForm = mainForm;
        final OsTag plugin = OsTag.getInstance();
        this.config = config;
        this.player = this.mainForm.getFormPlayer();
        this.blockedWords = config.getStringList("BlackWords");
        this.playerMentionConfig = plugin.getPlayersMentionConfig();
    }

    public void formatterSettings() {
        final SimpleForm form = new SimpleForm("Formatter Settings");

        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Chat Format", ImageType.PATH, "textures/ui/editIcon", (p, button) -> this.messageFormat());
        }
        form.addButton("Mention", ImageType.PATH, "textures/ui/icon_bell", (p, button) -> this.mentionModal());
        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Cooldown", ImageType.PATH, "textures/ui/timer", (p, button) -> this.cooldown())
                    .addButton("Censorship", ImageType.PATH, "textures/ui/mute_on", (p, button) -> this.censorShip());
        }

        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.openSettings());
        form.send(this.player);
    }

    private void messageFormat() {
        final CustomForm form = new CustomForm("Chat Format");
        final boolean andForAll = this.config.getBoolean("And-for-all");
        final boolean breaksBetweenMessages = this.config.getBoolean("break-between-messages.enable");

        form.addElement("message_format",
                        Input.builder()
                                .setName(MessageUtil.colorize("&lMessage format"))
                                .setDefaultValue(this.config.getString("message-format"))
                                .build())
                .addElement(new Label(MessageUtil.colorize("&lAdditional chat features")))
                .addElement("and-for-all", new Toggle("And for all", andForAll))
                .addElement("breaks", new Toggle("Breaks between messages", breaksBetweenMessages));

        form.setHandler((p, response) -> {
            final String messageFormat = response.getInput("message_format").getValue();
            if (!messageFormat.isEmpty()) {
                this.config.set("message-format", messageFormat);
            }

            final boolean finalAndForAll = response.getToggle("and-for-all").getValue();
            if (finalAndForAll != andForAll) {
                this.config.set("And-for-all", finalAndForAll);
            }

            final boolean finalBreaksBetweenMessages = response.getToggle("breaks").getValue();
            if (finalBreaksBetweenMessages != breaksBetweenMessages) {
                this.config.set("break-between-messages.enable", finalBreaksBetweenMessages);
            }

            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });
        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void cooldown() {
        final CustomForm form = new CustomForm("Cooldown Settings");
        final boolean enabled = this.config.getBoolean("cooldown.enable");

        form.addElement(new Label(MessageUtil.colorize("&aEnable cooldown")))
                .addElement("cooldown_enable", new Toggle("Cooldown", enabled));

        if (enabled) {
            form.addElement("delay",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown time"))
                                    .setDefaultValue(String.valueOf(this.config.getLong("cooldown.delay")))
                                    .build())
                    .addElement("message",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown message"))
                                    .setDefaultValue(this.config.getString("cooldown.message"))
                                    .build())
                    .addElement("over",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown over message"))
                                    .setDefaultValue(this.config.getString("cooldown.over"))
                                    .build())
                    .addElement("disabled",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown disabled message"))
                                    .setDefaultValue(this.config.getString("cooldown.disabled"))
                                    .build())

                    .addElement("bypass",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aCooldown bypass message"))
                                    .setDefaultValue(this.config.getString("cooldown.bypass"))
                                    .build());
        }

        form.setHandler((p, response) -> {
            this.config.set("cooldown.enable", response.getToggle("cooldown_enable").getValue());
            if (enabled) {
                this.config.set("cooldown.delay", Integer.valueOf(response.getInput("delay").getValue()));
                this.config.set("cooldown.message", response.getInput("message").getValue());
                this.config.set("cooldown.over", response.getInput("over").getValue());
                this.config.set("cooldown.disabled", response.getInput("disabled").getValue());
                this.config.set("cooldown.bypass", response.getInput("bypass").getValue());
            }
            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void censorShip() {
        final CustomForm form = new CustomForm("CensorShip Settings");
        final boolean enabled = this.config.getBoolean("censorship.enable");

        form.addElement(new Label(MessageUtil.colorize("&aEnable censor ship")))
                .addElement("censor_enable", new Toggle("Censorship", enabled));

        if (enabled) {
            form.addElement("censor",
                    Input.builder()
                            .setName(MessageUtil.colorize("&aCensorShip message"))
                            .setDefaultValue(this.config.getString("censorship.word"))
                            .build());

            form.addElement(new Label(MessageUtil.colorize("&aBlocked words")));
            for (int i = 0; i < this.blockedWords.size(); i++) {
                form.addElement("blackwords_" + i,
                        Input.builder()
                                .setName((i + 1) + ".")
                                .setDefaultValue(this.blockedWords.get(i))
                                .build());

            }
            form.addElement("blockword",
                    Input.builder()
                            .setName(MessageUtil.colorize("&lBlock an word"))
                            .setDefaultValue("ExampleWord")
                            .build());
        }

        form.setHandler((p, response) -> {
            this.config.set("censorship.enable", response.getToggle("censor_enable").getValue());
            if (enabled) {
                final List<String> finalBlocked = new ArrayList<>();
                this.config.set("censorship.word", response.getInput("censor").getValue().charAt(0));

                for (int i = 0; i < this.blockedWords.size(); i++) {
                    final String word = response.getInput("blackwords_" + i).getValue();
                    if (!word.isEmpty()) {
                        finalBlocked.add(word);
                    }
                }

                final String blockedWord = response.getInput("blockword").getValue();
                if (!blockedWord.isEmpty() && !blockedWord.equalsIgnoreCase("ExampleWord")) {
                    finalBlocked.add(blockedWord);
                }

                this.blockedWords = finalBlocked;
                this.config.set("BlackWords", this.blockedWords);
            }
            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void mentionModal() {
         /*
           This is for Mention Sound function and it is still experimental
         */

        final ModalForm form = new ModalForm("Mention admin settings");

        form.setContent("Open mention admin settings?\n")
                .setPositiveButton("Yes")
                .setNegativeButton("No");

        if (!this.player.hasPermission(Permissions.ADMIN)) {
            mentionForm();
            return;
        }

        form.setNoneHandler(p -> this.mentionForm());

        form.setHandler((p, result) -> {
            if (result) {
                this.mentionAdminForm();
                this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a opended&b Mentions Admin Settings");
            } else {
                this.mentionForm();
            }
        });
        form.send(this.player);
    }

    private void mentionAdminForm() {
          /*
           This is for Mention Sound function and it is still experimental
         */

        final CustomForm form = new CustomForm("Mentions Admin Settings");
        final boolean functionEnabled = this.playerMentionConfig.mentionSoundFunctionEnabled();
        final int soundsValue = this.playerMentionConfig.getSoundsValue();
        final List<SelectableElement> maxSounds = new ArrayList<>();


        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addElement(MessageUtil.colorize("&4Admin Settings"))
                    .addElement("mentionsound", new Toggle("Mention Sound Function", this.playerMentionConfig.mentionSoundFunctionEnabled()));
            if (functionEnabled) {
                for (int i = 1; i <= soundsValue + 100; i++) {
                    final SelectableElement element = new SelectableElement(String.valueOf(i), i);
                    maxSounds.add(element);
                }
                form.addElement("maxsounds", new StepSlider("Sounds value", maxSounds, soundsValue - 1));
            }
        }

        form.setHandler((p, response) -> {
            final boolean finalFunctionEnabled = response.getToggle("mentionsound").getValue();
            if (this.player.hasPermission(Permissions.ADMIN)) {
                if (finalFunctionEnabled != functionEnabled) {
                    this.playerMentionConfig.setMentionSoundFunctionEnabled(finalFunctionEnabled);
                }
                if (functionEnabled) {
                    final SelectableElement sounds = response.getStepSlider("maxsounds").getValue();
                    if (sounds.getValue() != null && sounds.getValue(Integer.class) != soundsValue) {
                        this.playerMentionConfig.setSoundsValue(sounds.getValue(Integer.class));
                    }
                }
            }

            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });
        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void mentionForm() {
          /*
           This is for Mention Sound function and it is still experimental
         */

        final CustomForm form = new CustomForm("Mentions Settings");
        final boolean functionEnabled = this.playerMentionConfig.mentionSoundFunctionEnabled();
        final boolean playerEnabled = this.playerMentionConfig.hasEnabledMentions(this.player);
        final boolean title = this.playerMentionConfig.hasEnabledTitle(this.player);
        final int soundsValue = this.playerMentionConfig.getSoundsValue();
        final List<SelectableElement> elements = new ArrayList<>();
        final int currentSoundIndex = this.playerMentionConfig.getSoundIndex(this.playerMentionConfig.getMentionSound(this.player));

        if (!functionEnabled) {
            this.player.sendMessage(MessageUtil.colorize("&cThis function disabled by Admin"));
            this.formatterSettings();
            return;
        }

        if (playerEnabled) {
            int counter = 0;
            for (final Sound sound : Sound.values()) {
                final SelectableElement element = new SelectableElement(sound.name(), counter);
                elements.add(element);
                counter++;
                if (counter == soundsValue) {
                    if (currentSoundIndex > counter) {
                        final SelectableElement currentSound = new SelectableElement(this.playerMentionConfig.getSoundByIndex(currentSoundIndex), counter);
                        this.playerMentionConfig.setPlayerCustomIndex(this.player, counter);
                        elements.add(currentSound);
                    }
                    break;
                }
            }
        }

        form.addElement(new Label(MessageUtil.colorize("&aEnable mentions")))
                .addElement("mentions_enable", new Toggle("Mention", playerEnabled));

        if (playerEnabled) {
            form.addElement(new Label(MessageUtil.colorize("&aEnable title info")))
                    .addElement("title_info_enable", new Toggle("Title info", title))
                    .addElement(new Label(MessageUtil.colorize("&aYour mention sound: &b" + this.playerMentionConfig.getMentionSound(this.player))))
                    .addElement("soundname", new Dropdown(MessageUtil.colorize("&aMention Sound"), elements, this.playerMentionConfig.getPlayerCustomIndex(this.player)))
                    .addElement("customsound",
                            Input.builder()
                                    .setName(MessageUtil.colorize("&aAdd sound by index"))
                                    .build());
        }

        form.setHandler((p, response) -> {
            final boolean finalPlayerEnabled = response.getToggle("mentions_enable").getValue();
            if (finalPlayerEnabled != playerEnabled) {
                this.playerMentionConfig.setEnabledMentions(this.player, finalPlayerEnabled);
            }

            if (playerEnabled) {
                final boolean finalTitle = response.getToggle("title_info_enable").getValue();
                final SelectableElement element = response.getDropdown("soundname").getValue();
                this.playerMentionConfig.setEnabledTitle(this.player, finalTitle);
                if (element.getValue() != null) {
                    if (element.getValue(Integer.class) != this.playerMentionConfig.getPlayerCustomIndex(this.player)) {
                        this.playerMentionConfig.setMentionSound(this.player, this.playerMentionConfig.getSoundByIndex(element.getValue(Integer.class)));
                    }
                }
            }

            final String customSound = response.getInput("customsound").getValue();
            if (customSound != null) {
                int index;
                try {
                    index = Integer.parseInt(customSound);
                    this.playerMentionConfig.setMentionSound(this.player, this.playerMentionConfig.getSoundByIndex(index));
                } catch (final NumberFormatException ex) {
                    this.player.sendMessage(MessageUtil.colorize("&cIndex must be an integer"));
                }
            }
            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.formatterSettings();
        });
        form.setNoneHandler(p -> this.formatterSettings());
        form.send(this.player);
    }
}