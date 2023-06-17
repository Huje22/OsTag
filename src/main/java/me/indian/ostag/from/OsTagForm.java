package me.indian.ostag.from;

import cn.nukkit.Player;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.MessageUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Label;
import ru.contentforge.formconstructor.form.element.Toggle;

import java.util.ArrayList;
import java.util.List;

public class OsTagForm {

    private final Form mainForm;
    private final OsTag plugin;
    private final Config config;
    private final Player player;
    private final List<String> advancedPlayers;
    private List<String> disabledWorlds;

    public OsTagForm(final Form mainForm, final Config config, final List<String> advancedPlayers) {
        this.mainForm = mainForm;
        this.plugin = OsTag.getInstance();
        this.config = config;
        this.player = this.mainForm.getFormPlayer();
        this.advancedPlayers = advancedPlayers;
        this.disabledWorlds = this.config.getStringList("disabled-worlds");
    }

    public void osTagSettings() {
        final SimpleForm form = new SimpleForm("OsTag Settings");

        form.addButton("NameTag & ScoreTag", ImageType.PATH, "textures/items/diamond", (p, button) -> scoreAndNameSettings())
                .addButton("Advanced Players", ImageType.PATH, "textures/ui/FriendsDiversity", (p, button) -> advancedPlayersSettings())
                .addButton("Disabled worlds", ImageType.PATH, "textures/ui/worldsIcon", (p, button) -> disabledWorld());
        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.getSettings().settings());
        form.send(player);
    }

    private void advancedPlayersSettings() {
        final CustomForm form = new CustomForm("Advanced players settings");

        form.addElement(new Label(MessageUtil.colorize("&lOnline Players")));
        for (final Player all : plugin.getServer().getOnlinePlayers().values()) {
            form.addElement("online_players_toggle_" + all.getName(), new Toggle(all.getName(), this.mainForm.isAdvancedPlayer(all.getName())));
        }

        if (!advancedPlayers.isEmpty()) {
            form.addElement(new Label(MessageUtil.colorize("&lAdvanced Players")));
        }
        for (final String playerXName : advancedPlayers) {
            if (this.mainForm.onlineByName(playerXName)) {
                continue;
            }
            form.addElement("advanced_players_toggle_" + playerXName, new Toggle(playerXName, this.mainForm.isAdvancedPlayer(playerXName)));
        }
        form.addElement("add_player",
                Input.builder()
                        .setName(MessageUtil.colorize("&lAdd non online player"))
                        .setDefaultValue("ExamplePlayer")
                        .build());

        form.setNoneHandler(p -> osTagSettings());


        form.setHandler((p, response) -> {
            for (Player all : plugin.getServer().getOnlinePlayers().values()) {
                final String name = all.getName();
                if (response.getToggle("online_players_toggle_" + name).getValue()) {
                    if (this.mainForm.isAdvancedPlayer(name)) {
                        continue;
                    }
                    advancedPlayers.add(name);
                } else {
                    if (!this.mainForm.isAdvancedPlayer(name)) {
                        continue;
                    }
                    advancedPlayers.remove(name);
                }
            }

            for (int i = 0; i < advancedPlayers.size(); i++) {
                final String playerXName = advancedPlayers.get(i);
                if (this.mainForm.onlineByName(playerXName)) {
                    continue;
                }
                if (response.getToggle("advanced_players_toggle_" + playerXName).getValue()) {
                    if (this.mainForm.isAdvancedPlayer(playerXName)) {
                        continue;
                    }
                    advancedPlayers.add(playerXName);
                } else {
                    if (!this.mainForm.isAdvancedPlayer(playerXName)) {
                        continue;
                    }
                    advancedPlayers.remove(playerXName);
                }
            }

            final String playerName = response.getInput("add_player").getValue();
            if (!playerName.isEmpty() && !playerName.equalsIgnoreCase("ExamplePlayer")) {
                advancedPlayers.add(playerName);
            }

            config.set("advanced-players", advancedPlayers);
            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            osTagSettings();
        });
        form.send(player);
    }

    private void scoreAndNameSettings() {
        final CustomForm form = new CustomForm("NameTag & ScoreTag Settings");
        final String split = this.mainForm.getSplit();

        this.mainForm.addNewLineMessage(form);
        form.addElement(new Label(MessageUtil.colorize("&lNormal player settings")));
        form.addElement("nick",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aNameTag"))
                                .setDefaultValue(config.getString("nick"))
                                .build())
                .addElement("subtag",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aSubTag"))
                                .setDefaultValue(MessageUtil.listToString(config.getStringList("subtag"), split))
                                .build());
        form.addElement(new Label(MessageUtil.colorize("&lAdvanced player settings")))
                .addElement("a-nick",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aNameTag"))
                                .setDefaultValue(config.getString("a-nick"))
                                .build())
                .addElement("a-subtag",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aSubTag"))
                                .setDefaultValue(MessageUtil.listToString(config.getStringList("a-subtag"), split))
                                .build());


        form.setHandler((p, response) -> {
            config.set("nick", response.getInput("nick").getValue());
            config.set("subtag", MessageUtil.stringToList(response.getInput("subtag").getValue(), split));

            config.set("a-nick", response.getInput("a-nick").getValue());
            config.set("a-subtag", MessageUtil.stringToList(response.getInput("a-subtag").getValue(), split));
            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            osTagSettings();
        });

        form.setNoneHandler(p -> osTagSettings());
        form.send(player);
    }

    private void disabledWorld() {
        final CustomForm form = new CustomForm("Disabled worlds");

        if (!disabledWorlds.isEmpty()) {
            form.addElement(new Label(MessageUtil.colorize("&lDisabled worlds")));
        }
        for (String world : disabledWorlds) {
            form.addElement("disabled_world_" + world, new Toggle(world, isDisabledWorld(world)));
        }

        form.addElement(new Label(MessageUtil.colorize("&lDetected worlds")));
        for (Level level : plugin.getServer().getLevels().values()) {
            final String levelName = level.getName();
            if (isDisabledWorld(levelName)) {
                continue;
            }
            form.addElement("detected_world_" + levelName, new Toggle(levelName, isDisabledWorld(levelName)));
        }

        form.addElement("add_world",
                Input.builder()
                        .setName(MessageUtil.colorize("&lDisable an undetected world"))
                        .setDefaultValue("ExampleWorld")
                        .build());

        form.setNoneHandler(p -> osTagSettings());

        final List<String> finalDis = new ArrayList<>(disabledWorlds);

        form.setHandler((p, response) -> {
            for (final String world : disabledWorlds) {
                if (response.getToggle("disabled_world_" + world).getValue()) {
                    finalDis.add(world);
                } else {
                    finalDis.remove(world);
                }
            }

            for (final Level level : plugin.getServer().getLevels().values()) {
                final String levelName = level.getName();

                final Toggle toggle = response.getToggle("detected_world_" + levelName);
                if (toggle != null && toggle.getValue()) {
                    finalDis.add(levelName);
                } else {
                    finalDis.remove(levelName);
                }
            }

            final String worldName = response.getInput("add_world").getValue();
            if (!worldName.isEmpty() && !worldName.equalsIgnoreCase("ExampleWorld")) {
                finalDis.add(response.getInput("add_world").getValue());
            }

            disabledWorlds = finalDis;
            config.set("disabled-worlds", disabledWorlds);
            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            osTagSettings();
        });
        form.send(player);
    }

    private boolean isDisabledWorld(final String name) {
        return this.disabledWorlds.contains(name);
    }
}
