package me.indian.ostag;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginManager;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.command.OsTagCommand;
import me.indian.ostag.command.TestttCommand;
import me.indian.ostag.listener.CpsListener;
import me.indian.ostag.listener.Formater;
import me.indian.ostag.listener.InputListener;
import me.indian.ostag.listener.PlayerJoinListener;
import me.indian.ostag.other.OsTagMetrics;
import me.indian.ostag.util.UpDateUtil;
import me.indian.ostag.util.ColorUtil;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.OsTimer;
import me.indian.ostag.util.OtherUtils;
import me.indian.ostag.util.PlayerInfoUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class OsTag extends PluginBase {

    public String pluginPrefix = ColorUtil.replaceColorCode("&f[&bOsTag&f]");
    public String publicDebugPrefix = ColorUtil.replaceColorCode("&8[&7Debug&8] ");
    public boolean luckPerm = false;
    public boolean papiAndKotlinLib = false;
    public boolean serverMovement;
    public boolean osTag;
    public boolean chatFormatter;
    public boolean debug;
    private static OsTag instance;
    private Formater formater;
    private LuckPerms luckPerms;
    private PlaceholderAPI placeholderApi;
    private OsTagMetrics osTagMetrics;

    public static OsTag getInstance() {
        return instance;
    }

    public OsTagMetrics getOstagMetrics() {
        return this.osTagMetrics;
    }

    public Formater getFormater() {
        return this.formater;
    }

    public LuckPerms getLuckperms() {
        return this.luckPerms;
    }

    public PlaceholderAPI getPlaceholderApi() {
        return this.placeholderApi;
    }

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        osTagMetrics = new OsTagMetrics();
        serverMovement = getConfig().getBoolean("movement-server");
        osTag = getConfig().getBoolean("OsTag");
        chatFormatter = getConfig().getBoolean("ChatFormatter");
        debug = getConfig().getBoolean("Debug");
    }

    @Override
    public void onEnable() {
        final long millisActualTime = System.currentTimeMillis();
        final PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("LuckPerms") == null) {
            getLogger().warning(ColorUtil.replaceColorCode("&cYou don't have lucky perms , ChatFormatting don't correctly work"));
        } else {
            this.luckPerms = LuckPermsProvider.get();
            luckPerm = true;
        }
        if (pm.getPlugin("PlaceholderAPI") == null || pm.getPlugin("KotlinLib") == null) {
            getLogger().warning(ColorUtil.replaceColorCode("&cYou don't have PlaceholderAPI or kotlin lib,placeholders from &bPlaceholderAPI&c will not work"));
        } else {
            this.placeholderApi = PlaceholderAPI.getInstance();
            papiAndKotlinLib = true;
            registerPlaceholders();
        }
        this.formater = new Formater(this, this.getPlaceholderApi());
        if (getConfig().getBoolean("Disable")) {
            getLogger().warning(ColorUtil.replaceColorCode("&4Disabling plugin due to disable in config"));
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new CpsListener(), this);
        if (serverMovement) {
            pm.registerEvents(new InputListener(), this);
        }
        ((PluginCommand<?>) getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) getCommand("tto")).setExecutor(new TestttCommand(this));
        if (osTag) {
            pm.registerEvents(new OsTimer(), this);
            int refreshTime = getConfig().getInt("refresh-time");
            if (refreshTime <= 0) {
                refreshTime = 1;
                getConfig().set("refresh-time", 1);
                getConfig().save();
                getLogger().warning(ColorUtil.replaceColorCode("&cRefresh time must be higer than &b0 &c,we will set it up for you!"));
            }
            getServer().getScheduler().scheduleRepeatingTask(new OsTimer(), 20 * refreshTime);
        } else {
            getLogger().info(ColorUtil.replaceColorCode("&bOsTag module is disabled "));
        }
        if (chatFormatter) {
            pm.registerEvents(new Formater(this, this.getPlaceholderApi()), this);
        } else {
            getLogger().info(ColorUtil.replaceColorCode("&bChatFormatter module is disabled"));
        }
        pm.registerEvents(new PlayerJoinListener(this), this);
        pluginInfo("admin", getServer().getConsoleSender());
        info();
        UpDateUtil.autoUpDate();
        this.getOstagMetrics().run();

        final double executionTimeInSeconds = (System.currentTimeMillis() - millisActualTime) / 1000.0;
        getLogger().info(ColorUtil.replaceColorCode("&aStarted in &b" + executionTimeInSeconds + " &aseconds"));
    }

    public void pluginInfo(String type, CommandSender sender) {
        final PluginDescription descriptor = this.getDescription();
        final Server server = this.getServer();

        final String pluginVersion = descriptor.getVersion();
        final String authors = String.valueOf(descriptor.getAuthors()).replace("[", "").replace("]", "");
        final String nukkitVersion = server.getNukkitVersion();
        final String serverVersion = server.getVersion();
        final String apiVersion = server.getApiVersion();
        final String latest = GithubUtil.getFastTagInfo() + GithubUtil.getBehindCount();

        switch (type) {
            case "admin":
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + pluginVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aLatest version:&3 " + latest));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + authors));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Version:&3 " + nukkitVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Api Version:&3 " + apiVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + serverVersion));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aFormatter&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Plugins"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aLuckPerms&3: " + OtherUtils.getLuckPermStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aKotlinLib & PlaceholderAPI&3: " + OtherUtils.getKotOrPapiStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                break;
            case "normal":
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + pluginVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aLatest version:&3 " + latest));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + authors));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + serverVersion));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aFormatter&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                break;
            default:
                sender.sendMessage("Unknown OnEnableInfo type");
                break;
        }
    }

    private void registerPlaceholders() {
        try {
            final PlaceholderAPI api = this.getPlaceholderApi();
            final String prefix = "ostag_";
            api.builder(prefix + "cps", Integer.class)
                    .visitorLoader(entry -> CpsListener.getCPS(entry.getPlayer()))
                    .build();
            api.builder(prefix + "cooldown", String.class)
                    .visitorLoader(entry -> getFormater().cooldown(entry.getPlayer()))
                    .build();
            api.builder(prefix + "device", String.class)
                    .visitorLoader(entry -> PlayerInfoUtil.getDevice(entry.getPlayer()))
                    .build();
            api.builder(prefix + "controller", String.class)
                    .visitorLoader(entry -> PlayerInfoUtil.getController(entry.getPlayer()))
                    .build();
            api.builder(prefix + "preffix", String.class)
                    .visitorLoader(entry -> PlayerInfoUtil.getLuckPermPreffix(entry.getPlayer()))
                    .build();
            api.builder(prefix + "suffix", String.class)
                    .visitorLoader(entry -> PlayerInfoUtil.getLuckPermSuffix(entry.getPlayer()))
                    .build();
            api.builder(prefix + "group", String.class)
                    .visitorLoader(entry -> PlayerInfoUtil.getLuckPermGroupDisName(entry.getPlayer()))
                    .build();
            api.builder(prefix + "version", String.class)
                    .visitorLoader(entry -> entry.getPlayer().getLoginChainData().getGameVersion())
                    .build();
            api.builder(prefix + "xp", String.class)
                    .visitorLoader(entry -> PlayerInfoUtil.getXp(entry.getPlayer()))
                    .build();
            getLogger().info(ColorUtil.replaceColorCode("&aLoaded placeholderapi placeholders"));
        } catch (Exception exception) {
            getLogger().error(ColorUtil.replaceColorCode("&cLoading placeholders failed "));
            System.out.println(exception.getMessage());
        }
    }

    private void info() {
        if (this.getDescription().getVersion().contains("Beta") || this.getDescription().getVersion().contains("beta")) {
            getLogger().warning(ColorUtil.replaceColorCode("&4You are running beta version, it may not be stable"));
        }
        getLogger().info(GithubUtil.checkTagCompatibility());
    }
}
