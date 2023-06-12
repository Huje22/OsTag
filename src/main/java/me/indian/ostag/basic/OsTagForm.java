package me.indian.ostag.basic;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.StatusUtil;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Label;
import ru.contentforge.formconstructor.form.element.Toggle;

import java.util.List;


public class OsTagForm {

    private final OsTag plugin;
    private final Config config;
    private final Player player;
    final List<String> advancedPlayers;

    public OsTagForm(final Player player) {
        this.player = player;
        this.plugin = OsTag.getInstance();
        this.config = this.plugin.getConfig();
        this.advancedPlayers = this.config.getStringList("advanced-players");
    }


    public void runOstagFrom() {
        if(plugin.getServer().getPluginManager().getPlugin("FormConstructor") == null){
            String error = MessageUtil.colorize("&cYou dont have &bFormConstructor &b plugin !");
            String error2 = MessageUtil.colorize("&cDownload it from here &bhttps://github.com/OpenPlugins-Minecraft/OsTag/tree/main/libs!");
            plugin.getLogger().error(error);
            plugin.getLogger().error(error2);
            player.sendMessage(error);
            player.sendMessage(error2);
            return;
        }

        final SimpleForm form = new SimpleForm(MessageUtil.colorize("&bOsTag &fMenu"));

        form.setContent(MessageUtil.colorize("&aJoin our discord\n"))
                .addContent(MessageUtil.colorize("&bhttps://discord.gg/k69htTFCVk"))
                .addButton("Version", ImageType.PATH, "textures/ui/infobulb", (p, button) -> versionInfo());

        if (player.isOp() || player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Reload config", ImageType.PATH, "textures/ui/refresh", (p, button) -> MessageUtil.playerCommand(player, "ostag reload"))
                    .addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> upDate())
                    .addButton("Settings", ImageType.PATH, "textures/ui/icon_setting", (p, button) -> settings());
        }

        form.send(player);
    }

    private void versionInfo() {
        final SimpleForm form = new SimpleForm("Version info");

        final PluginDescription descriptor = plugin.getDescription();
        final Server server = plugin.getServer();

        final String pluginVersion = descriptor.getVersion();
        final String authors = String.valueOf(descriptor.getAuthors()).replace("[", "").replace("]", "");
        final String nukkitVersion = server.getNukkitVersion();
        final String serverVersion = server.getVersion();
        final String apiVersion = server.getApiVersion();
        final String latest = GithubUtil.getFastTagInfo() + GithubUtil.getBehindCount(player);

        if (player.hasPermission(Permissions.ADMIN) || player.isOp()) {
            form.addContent(MessageUtil.colorize("&aOsTag version:&3 " + pluginVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aLatest version:&3 " + latest + "\n"))
                    .addContent(MessageUtil.colorize("&aPlugin by:&6 " + authors + "\n"))
                    .addContent(MessageUtil.colorize("&aNukkit Version:&3 " + nukkitVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aNukkit Api Version:&3 " + apiVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aServer Version:&3 " + serverVersion + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"))
                    .addContent(MessageUtil.colorize("&1Modules" + "\n"))
                    .addContent(MessageUtil.colorize("&aFormatter&3: " + StatusUtil.getFormaterStatus() + "\n"))
                    .addContent(MessageUtil.colorize("&aOsTag&3: " + StatusUtil.getOsTagStatus() + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"))
                    .addContent(MessageUtil.colorize("&1Plugins" + "\n"))
                    .addContent(MessageUtil.colorize("&aLuckPerms&3: " + StatusUtil.getLuckPermStatus() + "\n"))
                    .addContent(MessageUtil.colorize("&aKotlinLib & PlaceholderAPI&3: " + StatusUtil.getKotOrPapiStatus() + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"));
        } else {
            form.addContent(MessageUtil.colorize("&aOsTag version:&3 " + pluginVersion + "\n"))
                    .addContent(MessageUtil.colorize("&aLatest version:&3 " + latest + "\n"))
                    .addContent(MessageUtil.colorize("&aPlugin by:&6 " + authors + "\n"))
                    .addContent(MessageUtil.colorize("&aServer Version:&3 " + serverVersion + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"))
                    .addContent(MessageUtil.colorize("&1Modules" + "\n"))
                    .addContent(MessageUtil.colorize("&aFormatter&3: " + StatusUtil.getFormaterStatus() + "\n"))
                    .addContent(MessageUtil.colorize("&aOsTag&3: " + StatusUtil.getOsTagStatus() + "\n"))
                    .addContent(MessageUtil.colorize(" " + "\n"));
        }

        form.addButton(MessageUtil.colorize("Run &7/ostag version"), (p, button) -> MessageUtil.playerCommand(player, "ostag version"));

        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    private void settings() {
        final SimpleForm form = new SimpleForm("Settings");
        form.addButton("NameTag & ScoreTag", ImageType.PATH, "textures/items/diamond", (p, button) -> scoreAndNameSettings())
                .addButton("Advanced Players", ImageType.PATH, "textures/ui/FriendsDiversity", (p, button) -> advancedPlayersSettings(p));
        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    private void upDate() {
        final SimpleForm form = new SimpleForm("UpDate menu");

        if (GithubUtil.getFastTagInfo().contains("false")) {
            form.setContent(MessageUtil.colorize("&aYou can download latest version"))
                    .addButton("UpDate", ImageType.PATH, "textures/ui/up_chevron", (p, button) -> plugin.getUpdateUtil().manualUpDate(player));
        } else {
            if (!GithubUtil.getFastTagInfo().contains("true")) {
                form.setContent(MessageUtil.colorize("&aYou can't download lates"))
                        .addButton("Force UpDate", ImageType.PATH, "textures/ui/ErrorGlyph_small", (p, button) -> plugin.getUpdateUtil().manualUpDate(player));
            } else {
                form.setContent(MessageUtil.colorize("&aYou have latest version!"));
            }
        }


        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    private void advancedPlayersSettings(Player player) {
        final CustomForm form = new CustomForm("Advanced players settings");

        form.addElement(new Label(MessageUtil.colorize("&lOnline Players")));
        for (Player all : plugin.getServer().getOnlinePlayers().values()) {
            form.addElement("online_players_toggle_" + all.getName(), new Toggle(all.getName(), exist(all.getName())));
        }

        form.addElement(new Label(MessageUtil.colorize("&lAdvanced Players")));
        for (String playerXName : advancedPlayers) {
            if (online(playerXName)) {
                continue;
            }
            form.addElement("advanced_players_toggle_" + playerXName, new Toggle(playerXName, exist(playerXName)));
        }
        form.addElement("add_player",
                Input.builder()
                        .setName(MessageUtil.colorize("&lAdd non online player"))
                        .setDefaultValue("ExamplePlayer")
                        .build());

        form.setNoneHandler(p -> settings());


        form.setHandler((p, response) -> {
            for (Player all : plugin.getServer().getOnlinePlayers().values()) {
                if (response.getToggle("online_players_toggle_" + all.getName()).getValue()) {
                    if (exist(all.getName())) {
                        continue;
                    }
                    advancedPlayers.add(all.getName());
                } else {
                    if (!exist(all.getName())) {
                        continue;
                    }
                    advancedPlayers.remove(all.getName());
                }
            }

            for (int i = 0; i < advancedPlayers.size(); i++) {
                String playerXName = advancedPlayers.get(i);
                if (online(playerXName)) {
                    continue;
                }
                if (response.getToggle("advanced_players_toggle_" + playerXName).getValue()) {
                    if (exist(playerXName)) {
                        continue;
                    }
                    advancedPlayers.add(playerXName);
                } else {
                    if (!exist(playerXName)) {
                        continue;
                    }
                    advancedPlayers.remove(playerXName);
                }
            }

            if (!response.getInput("add_player").getValue().isEmpty()) {
                advancedPlayers.add(response.getInput("add_player").getValue());
            }

            config.set("advanced-players", advancedPlayers);
            config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
        });
        form.send(player);
    }

    private void scoreAndNameSettings() {
        final CustomForm form = new CustomForm("NameTag & ScoreTag Settings");

        form.addElement(new Label(MessageUtil.colorize("&aTo start a new line use \"- \"")));
        form.addElement(new Label(MessageUtil.colorize("&lNormal player settings")));
        form.addElement("nick",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aNormal player nick name"))
                                .setDefaultValue(config.getString("nick"))
                                .build())
                .addElement("subtag",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aNormal player subtag name"))
                                .setDefaultValue(MessageUtil.listToString(config.getStringList("subtag")))
                                .build());
        form.addElement(new Label(MessageUtil.colorize("&lAdvanced player settings")))
                .addElement("a-nick",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aAdvanced player nick name"))
                                .setDefaultValue(config.getString("a-nick"))
                                .build())
                .addElement("a-subtag",
                        Input.builder()
                                .setName(MessageUtil.colorize("&aAdvanced player subtag name"))
                                .setDefaultValue(MessageUtil.listToString(config.getStringList("a-subtag")))
                                .build());


        form.setHandler((p, response) -> {
            config.set("nick", response.getInput("nick").getValue());
            config.set("subtag", MessageUtil.stringToList(response.getInput("subtag").getValue()));

            config.set("a-nick", response.getInput("a-nick").getValue());
            config.set("a-subtag", MessageUtil.stringToList(response.getInput("a-subtag").getValue()));
            config.save();

        });

        form.setNoneHandler(p -> settings());

        form.send(player);
    }

    private void test() {
        final SimpleForm form = new SimpleForm("Sample title");
        form.setContent("This is a text")
                .addContent("\nThis is addition :3")
                .addButton("Test1", ImageType.PATH, "textures/ui/settings_glyph_color_2x", (p, button) -> p.sendMessage(String.valueOf(button.index)))
                .addButton("Test2", ImageType.PATH, "textures/ui/icon_setting", (p, button) -> p.sendMessage(String.valueOf(button.index)))
                .addButton("Test3", ImageType.PATH, "textures/ui/refresh", (p, button) -> p.sendMessage(String.valueOf(button.index)))
                .addButton("Test4", ImageType.PATH, "textures/ui/refresh_light", (p, button) -> p.sendMessage(String.valueOf(button.index)));

        form.setNoneHandler(p -> runOstagFrom());
        form.send(player);
    }

    private boolean online(final String name) {
        final Player player = plugin.getServer().getPlayer(name);
        return player != null;
    }

    private boolean exist(final String name) {
        return advancedPlayers.contains(name);
    }

}