package me.indian.ostag;

import cn.nukkit.command.CommandMap;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.basic.OsTagMetrics;
import me.indian.ostag.command.OsTagCommand;
import me.indian.ostag.command.TestttCommand;
import me.indian.ostag.listener.CpsListener;
import me.indian.ostag.listener.Formater;
import me.indian.ostag.listener.InputListener;
import me.indian.ostag.listener.PlayerJoinListener;
import me.indian.ostag.listener.PlayerPreLoginListener;
import me.indian.ostag.listener.PlayerQuitListener;
import me.indian.ostag.runnable.OsTimer;
import me.indian.ostag.util.GithubUtil;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.PlayerInfoUtil;
import me.indian.ostag.util.PluginInfoUtil;
import me.indian.ostag.util.Status;
import me.indian.ostag.util.UpDateUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public class OsTag extends PluginBase {

    private static OsTag instance;
    public String pluginPrefix = MessageUtil.colorize("&f[&bOsTag&f] ");
    public String publicDebugPrefix = MessageUtil.colorize("&8[&7Debug&8] ");
    public boolean luckPerm = false;
    public boolean papiAndKotlinLib = false;
    public boolean formConstructor = false;
    public boolean serverMovement;
    public boolean nametag;
    public boolean scoreTag;
    public boolean osTag;
    public boolean chatFormatter;
    public boolean debug;
    public boolean upDatechecker;
    private OsTagCommand osTagCommand;
    private OsTimer osTimer;
    private UpDateUtil upDateUtil;
    private Formater formater;
    private InputListener inputListener;
    private LuckPerms luckPerms;
    private PlaceholderAPI placeholderApi;

    public static OsTag getInstance() {
        return instance;
    }

    public OsTagCommand getOsTagCommand() {
        return this.osTagCommand;
    }

    public OsTimer getOsTimer() {
        return this.osTimer;
    }

    public UpDateUtil getUpdateUtil() {
        return this.upDateUtil;
    }

    public Formater getFormater() {
        return this.formater;
    }

    public InputListener getInputListener() {
        return this.inputListener;
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
        this.osTagCommand = new OsTagCommand(this);
        this.osTimer = new OsTimer(this);
        this.upDateUtil = new UpDateUtil();
        this.debug = this.getConfig().getBoolean("Debug");
        this.serverMovement = this.getConfig().getBoolean("Movement-server");
        this.upDatechecker = this.getConfig().getBoolean("UpdateChecker");
        this.osTag = this.getConfig().getBoolean("OsTag");
        this.chatFormatter = this.getConfig().getBoolean("ChatFormatter");
        this.nametag = this.getConfig().getBoolean("NameTag");
        this.scoreTag = this.getConfig().getBoolean("ScoreTag");
        if (!(nametag && scoreTag)) {
            this.osTag = false;
            if (this.debug) {
                this.getLogger().info(MessageUtil.colorize(publicDebugPrefix + "&8[&dMain&8] " + "&bWe disable the&a ostag&b module because&a scoretag&b and&a nametag&b are disabled "));
            }
        }
    }

    @Override
    public void onEnable() {
        final long millisActualTime = System.currentTimeMillis();
        final PluginManager pm = this.getServer().getPluginManager();
        if (pm.getPlugin("LuckPerms") == null) {
            this.getLogger().warning(MessageUtil.colorize("&cYou don't have lucky perms , ChatFormatting don't correctly work"));
        } else {
            this.luckPerms = LuckPermsProvider.get();
            this.luckPerm = true;
        }
        if (pm.getPlugin("PlaceholderAPI") == null || pm.getPlugin("KotlinLib") == null) {
            this.getLogger().warning(MessageUtil.colorize("&cYou don't have PlaceholderAPI or kotlin lib,placeholders from &bPlaceholderAPI&c will not work"));
        } else {
            this.placeholderApi = PlaceholderAPI.getInstance();
            this.papiAndKotlinLib = true;
            this.registerPlaceholders();
        }
        if (pm.getPlugin("FormConstructor") == null) {
            this.getLogger().error(MessageUtil.colorize("&cYou don't have &bFormConstructor &c plugin !"));
            this.getLogger().error(MessageUtil.colorize("&cDownload it from here &bhttps://github.com/OpenPlugins-Minecraft/OsTag/tree/main/libs!"));
        } else {
            formConstructor = true;
        }

        this.formater = new Formater(this, this.getPlaceholderApi());
        if (this.getConfig().getBoolean("Disable")) {
            this.getLogger().warning(MessageUtil.colorize("&4Disabling plugin due to disable in config"));
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new CpsListener(), this);
        if (this.serverMovement) {
            this.inputListener = new InputListener(this);
            pm.registerEvents(this.getInputListener(), this);
        }

        final CommandMap commandMap = this.getServer().getCommandMap();
        commandMap.register("OsTag", this.getOsTagCommand());
        commandMap.register("OsTag", new TestttCommand(this));

        if (this.osTag) {
            this.getOsTimer().setStatus(Status.STOPPED);
            this.getOsTimer().startTimer();
        } else {
            this.getLogger().info(MessageUtil.colorize("&bOsTag module is disabled "));
        }
        if (this.chatFormatter) {
            pm.registerEvents(this.getFormater(), this);
        } else {
            this.getLogger().info(MessageUtil.colorize("&bChatFormatter module is disabled"));
        }
        pm.registerEvents(new PlayerJoinListener(this), this);
        pm.registerEvents(new PlayerPreLoginListener(this), this);
        pm.registerEvents(new PlayerQuitListener(this), this);

        this.onEnableInfo();
        this.getServer().getScheduler().scheduleDelayedTask(this::info, 30);
        this.getUpdateUtil().autoUpDate();
        new OsTagMetrics().run();

        final double executionTimeInSeconds = (System.currentTimeMillis() - millisActualTime) / 1000.0;
        this.getLogger().info(MessageUtil.colorize("&aStarted in &b" + executionTimeInSeconds + " &aseconds"));
    }

    private void onEnableInfo() {
        final PluginInfoUtil infoUtil = new PluginInfoUtil(this.getServer().getConsoleSender() , true);

        for (final String s : infoUtil.getAdminInfo()) {
            this.getLogger().info(MessageUtil.colorize(s));
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
                    .visitorLoader(entry -> this.getFormater().getCooldown(entry.getPlayer()))
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
            this.getLogger().info(MessageUtil.colorize("&aLoaded placeholderapi placeholders"));
        } catch (final Exception exception) {
            this.getLogger().error(MessageUtil.colorize("&cLoading placeholders failed "));
            exception.printStackTrace();
        }
    }

    private void info() {
        final String version = this.getDescription().getVersion();
        if (version.contains("Beta") || version.contains("beta")) {
            this.getLogger().warning(MessageUtil.colorize("&4You are running beta version, it may not be stable"));
        }
        if (version.contains("Dev") || version.contains("dev")) {
            this.getLogger().warning(MessageUtil.colorize("&4You running dev build , so many things which this version contains  may be removed"));
        }
        this.getLogger().info(GithubUtil.checkTagCompatibility());
    }
}
