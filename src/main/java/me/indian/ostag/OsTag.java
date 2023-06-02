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
import me.indian.ostag.util.TextUtil;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.runnnable.OsTimer;
import me.indian.ostag.util.StatusUtil;
import me.indian.ostag.util.PlayerInfoUtil;
import me.indian.ostag.util.UpDateUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class OsTag extends PluginBase {

    private static OsTag instance;
    public String pluginPrefix = TextUtil.replaceColorCode("&f[&bOsTag&f] ");
    public String publicDebugPrefix = TextUtil.replaceColorCode("&8[&7Debug&8] ");
    public boolean luckPerm = false;
    public boolean papiAndKotlinLib = false;
    public boolean serverMovement;
    public boolean nametag;
    public boolean scoreTag;
    public boolean osTag;
    public boolean chatFormatter;
    public boolean debug;
    public boolean upDatechecker;
    private OsTagMetrics osTagMetrics;
    private UpDateUtil upDateUtil;
    private Formater formater;
    private LuckPerms luckPerms;
    private PlaceholderAPI placeholderApi;

    public static OsTag getInstance() {
        return instance;
    }

    public OsTagMetrics getOstagMetrics() {
        return this.osTagMetrics;
    }

    public UpDateUtil getUpdateUtil() {
        return this.upDateUtil;
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
        this.saveDefaultConfig();
        this.upDateUtil = new UpDateUtil();
        this.osTagMetrics = new OsTagMetrics();
        this.debug = this.getConfig().getBoolean("Debug");
        this.serverMovement = this.getConfig().getBoolean("movement-server");
        this.upDatechecker = this.getConfig().getBoolean("UpdateChecker");
        this.osTag = this.getConfig().getBoolean("OsTag");
        this.chatFormatter = this.getConfig().getBoolean("ChatFormatter");
        this.nametag = this.getConfig().getBoolean("NameTag");
        this.scoreTag = this.getConfig().getBoolean("ScoreTag");
        if (!(nametag && scoreTag)) {
            this.osTag = false;
            if (this.debug) {
                this.getLogger().info(TextUtil.replaceColorCode(publicDebugPrefix + "&8[&dMain&8] " + "&bWe disable the&a ostag&b module because&a scoretag&b and&a nametag&b are disabled "));
            }
        }
    }

    @Override
    public void onEnable() {
        final long millisActualTime = System.currentTimeMillis();
        final PluginManager pm = this.getServer().getPluginManager();
        if (pm.getPlugin("LuckPerms") == null) {
            this.getLogger().warning(TextUtil.replaceColorCode("&cYou don't have lucky perms , ChatFormatting don't correctly work"));
        } else {
            this.luckPerms = LuckPermsProvider.get();
            this.luckPerm = true;
        }
        if (pm.getPlugin("PlaceholderAPI") == null || pm.getPlugin("KotlinLib") == null) {
            this.getLogger().warning(TextUtil.replaceColorCode("&cYou don't have PlaceholderAPI or kotlin lib,placeholders from &bPlaceholderAPI&c will not work"));
        } else {
            this.placeholderApi = PlaceholderAPI.getInstance();
            this.papiAndKotlinLib = true;
            this.registerPlaceholders();
        }
        this.formater = new Formater(this, this.getPlaceholderApi());
        if (this.getConfig().getBoolean("Disable")) {
            this.getLogger().warning(TextUtil.replaceColorCode("&4Disabling plugin due to disable in config"));
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new CpsListener(), this);
        if (this.serverMovement) {
            pm.registerEvents(new InputListener(), this);
        }
        ((PluginCommand<?>) this.getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) this.getCommand("tto")).setExecutor(new TestttCommand(this));
        if (this.osTag) {
            pm.registerEvents(new OsTimer(), this);
            int refreshTime = this.getConfig().getInt("refresh-time");
            if (refreshTime <= 0) {
                refreshTime = 1;
                this.getConfig().set("refresh-time", 1);
                this.getConfig().save();
                this.getLogger().warning(TextUtil.replaceColorCode("&cRefresh time must be higer than &b0 &c,we will set it up for you!"));
            }
            this.getServer().getScheduler().scheduleRepeatingTask(new OsTimer(), 20 * refreshTime);
        } else {
            this.getLogger().info(TextUtil.replaceColorCode("&bOsTag module is disabled "));
        }
        if (this.chatFormatter) {
            pm.registerEvents(new Formater(this, this.getPlaceholderApi()), this);
        } else {
            this.getLogger().info(TextUtil.replaceColorCode("&bChatFormatter module is disabled"));
        }
        pm.registerEvents(new PlayerJoinListener(this), this);
        this.pluginInfo("admin", this.getServer().getConsoleSender());
        this.info();
        this.getUpdateUtil().autoUpDate();
        this.getOstagMetrics().run();

        final double executionTimeInSeconds = (System.currentTimeMillis() - millisActualTime) / 1000.0;
        this.getLogger().info(TextUtil.replaceColorCode("&aStarted in &b" + executionTimeInSeconds + " &aseconds"));
    }

    public void pluginInfo(final String type, final CommandSender sender) {
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
                sender.sendMessage(TextUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(TextUtil.replaceColorCode("&aOsTag version:&3 " + pluginVersion));
                sender.sendMessage(TextUtil.replaceColorCode("&aLatest version:&3 " + latest));
                sender.sendMessage(TextUtil.replaceColorCode("&aPlugin by:&6 " + authors));
                sender.sendMessage(TextUtil.replaceColorCode("&aNukkit Version:&3 " + nukkitVersion));
                sender.sendMessage(TextUtil.replaceColorCode("&aNukkit Api Version:&3 " + apiVersion));
                sender.sendMessage(TextUtil.replaceColorCode("&aServer Version:&3 " + serverVersion));
                sender.sendMessage(TextUtil.replaceColorCode(" "));
                sender.sendMessage(TextUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(TextUtil.replaceColorCode("&aFormatter&3: " + StatusUtil.getFormaterStatus()));
                sender.sendMessage(TextUtil.replaceColorCode("&aOsTag&3: " + StatusUtil.getOsTagStatus()));
                sender.sendMessage(TextUtil.replaceColorCode(" "));
                sender.sendMessage(TextUtil.replaceColorCode("&1Plugins"));
                sender.sendMessage(TextUtil.replaceColorCode("&aLuckPerms&3: " + StatusUtil.getLuckPermStatus()));
                sender.sendMessage(TextUtil.replaceColorCode("&aKotlinLib & PlaceholderAPI&3: " + StatusUtil.getKotOrPapiStatus()));
                sender.sendMessage(TextUtil.replaceColorCode(" "));
                sender.sendMessage(TextUtil.replaceColorCode("&b-------------------------------"));
                break;
            case "normal":
                sender.sendMessage(TextUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(TextUtil.replaceColorCode("&aOsTag version:&3 " + pluginVersion));
                sender.sendMessage(TextUtil.replaceColorCode("&aLatest version:&3 " + latest));
                sender.sendMessage(TextUtil.replaceColorCode("&aPlugin by:&6 " + authors));
                sender.sendMessage(TextUtil.replaceColorCode("&aServer Version:&3 " + serverVersion));
                sender.sendMessage(TextUtil.replaceColorCode(" "));
                sender.sendMessage(TextUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(TextUtil.replaceColorCode("&aFormatter&3: " + StatusUtil.getFormaterStatus()));
                sender.sendMessage(TextUtil.replaceColorCode("&aOsTag&3: " + StatusUtil.getOsTagStatus()));
                sender.sendMessage(TextUtil.replaceColorCode(" "));
                sender.sendMessage(TextUtil.replaceColorCode("&b-------------------------------"));
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
                    .visitorLoader(entry -> this.getFormater().cooldown(entry.getPlayer()))
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
            this.getLogger().info(TextUtil.replaceColorCode("&aLoaded placeholderapi placeholders"));
        } catch (final Exception exception) {
            this.getLogger().error(TextUtil.replaceColorCode("&cLoading placeholders failed "));
            System.out.println(exception.getMessage());
        }
    }

    private void info() {
        if (this.getDescription().getVersion().contains("Beta") || this.getDescription().getVersion().contains("beta")) {
            this.getLogger().warning(TextUtil.replaceColorCode("&4You are running beta version, it may not be stable"));
        }
        this.getLogger().info(GithubUtil.checkTagCompatibility());
    }
}
