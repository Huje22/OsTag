package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpDateUtil {

    private final OsTag plugin;
    private final PluginLogger logger;
    private final Config config;
    private final String debugPrefix;
    private final String pluginsPath;
    private String latestVersion;
    private final String currentVersion;
    private final String currentFileName;
    private String latestUrl;
    private final String latestFileName;
    private final ExecutorService executorService;
    private boolean redownload = false;
    private String downloadStatus = "";

    public UpDateUtil() {
        this.refresh();
        this.plugin = OsTag.getInstance();
        this.logger = this.plugin.getLogger();
        this.config = this.plugin.getConfig();
        this.debugPrefix = MessageUtil.colorize(this.plugin.publicDebugPrefix + "&8[&dAutoUpdate&8] ");
        this.pluginsPath = Server.getInstance().getPluginPath();
        this.currentVersion = this.plugin.getDescription().getVersion();
        this.currentFileName = "Ostag-" + this.currentVersion + ".jar";
        this.latestFileName = "OsTag-" + this.latestVersion + ".jar";
        this.executorService = Executors.newSingleThreadExecutor(new ThreadUtil("Ostag Update Thread"));
    }

    //Refreshing the tag so that it is as latest as possible regardless of the instance
    private void refresh() {
        this.latestVersion = GithubUtil.getLatestTag();
        this.latestUrl = "https://github.com/OpenPlugins-Minecraft/OsTag/releases/download/" + this.latestVersion + "/OsTag-" + this.latestVersion + ".jar";
    }

    public void autoUpDate() {
        if (this.config.getBoolean("AutoUpdate")) {
            this.upDate(null);
        }
    }

    public void manualUpDate(final CommandSender sender) {
        this.upDate(sender);
    }

    private void upDate(final CommandSender sender) {
        this.refresh();
        this.downloadStatus = "";
        this.executorService.execute(() -> {
            if (GithubUtil.getFastTagInfo().contains("false")) {
                final File latest = new File(this.pluginsPath + "/" + this.latestFileName);
                final File current = new File(this.pluginsPath + "/" + this.currentFileName);

                if (current.exists() && latest.exists()) {
                    if (!this.currentVersion.equals(this.latestVersion)) {
                        final String notUsingLatest = MessageUtil.colorize("&cYou have downloaded the latest version but you are not using it");
                        this.downloadStatus = notUsingLatest;
                        this.logger.info(notUsingLatest);
                        if (sender instanceof Player) {
                            sender.sendMessage(notUsingLatest);
                        }
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (!latest.exists()) {
                    this.logger.info(MessageUtil.colorize("&aDownloading latest ostag version..."));
                    this.downloadLatestVersion(sender);
                }
            } else {
                final String unNecessaryNotPossible = MessageUtil.colorize("&aDownloading the latest version is unnecessary or not possible");
                this.downloadStatus = unNecessaryNotPossible;
                if (this.plugin.debug) {
                    this.logger.info(MessageUtil.colorize(this.debugPrefix) + unNecessaryNotPossible);
                }
                if (sender instanceof Player) {
                    sender.sendMessage(unNecessaryNotPossible);
                }
                Thread.currentThread().interrupt();
            }
        });
    }

    private void downloadLatestVersion(final CommandSender sender) {
        try {
            final long millisActualTime = System.currentTimeMillis();
            final URL url = new URL(this.latestUrl);
            final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            final int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                final String contentType = httpConnection.getContentType();
                final int contentLength = httpConnection.getContentLength();

                if (this.plugin.debug) {
                    this.logger.info(MessageUtil.colorize(this.debugPrefix + "&b" + this.latestUrl));
                    this.logger.info(MessageUtil.colorize(this.debugPrefix + "&aVersion: &b" + this.latestVersion));
                    this.logger.info(MessageUtil.colorize(this.debugPrefix + "&aContent type: &b" + contentType));
                    this.logger.info(MessageUtil.colorize(this.debugPrefix + "&aContent length: &b" + contentLength));
                    this.logger.info(MessageUtil.colorize(this.debugPrefix + "&eStarting downloading"));
                }

                final InputStream inputStream = httpConnection.getInputStream();
                final String saveFilePath = this.pluginsPath + File.separator + this.latestFileName;

                final FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                final byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    this.downloadStatus = MessageUtil.colorize("&aDownload progress: &b" + (totalBytesRead * 100) / contentLength + "%" + " &8(&b" + bytesToKb(totalBytesRead) + "kb&8)");
                    if (this.plugin.debug) {
                        this.logger.info(this.debugPrefix + downloadStatus);
                    }
                    if (sender instanceof Player) {
                        ((Player) sender).sendActionBar(this.plugin.pluginPrefix + downloadStatus);
                    }
                }

                outputStream.close();
                inputStream.close();

                if (totalBytesRead != contentLength) {
                    final String badDownload = MessageUtil.colorize("&cDownload failed: Incomplete download");
                    this.downloadStatus = badDownload;
                    this.logger.warning(badDownload);
                    if (sender instanceof Player) {
                        sender.sendMessage(badDownload);
                        sender.sendMessage(MessageUtil.colorize("&aTrying to redownload"));
                    }
                    this.reDownload(sender);
                    return;
                }

                final double executionTimeInSeconds = (System.currentTimeMillis() - millisActualTime) / 1000.0;
                this.logger.info(MessageUtil.colorize("&aDownload completed in &b" + executionTimeInSeconds + " &aseconds"));
                if (sender instanceof Player) {
                    sender.sendMessage(MessageUtil.colorize("&aDownload completed in &b" + executionTimeInSeconds + " &aseconds"));
                }
                this.downloadStatus = "&aDownloaded";
            } else {
                final String incorrectResponse = MessageUtil.colorize("&cThe file could not be used. HTTP response code:" + responseCode);
                this.downloadStatus = incorrectResponse;
                this.logger.warning(incorrectResponse);
                Thread.currentThread().interrupt();
            }
            httpConnection.disconnect();
        } catch (final Exception e) {
            this.logger.warning(MessageUtil.colorize("&cCan't download latest ostag version!"));
            if (this.plugin.debug) {
                e.printStackTrace();
            }
            this.reDownload(sender);
            Thread.currentThread().interrupt();
        }
    }

    private void reDownload(final CommandSender sender) {
        if (this.redownload) {
            this.redownload = false;
            this.logger.warning(MessageUtil.colorize("&cRedownload failed"));
            if (sender instanceof Player) {
                sender.sendMessage(MessageUtil.colorize("&cRedownload failed"));
            }
        } else {
            this.redownload = true;
            this.logger.info(MessageUtil.colorize("&aTrying to redownload"));
            if (sender instanceof Player) {
                sender.sendMessage(MessageUtil.colorize("&aTrying to redownload"));
            }
            this.upDate(sender);
        }
    }

    private static double bytesToKb(final long bytes) {
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        final String kb = df.format((double) bytes / 1024);

        return Double.parseDouble(kb);
    }

    public String getDownloadStatus() {
        return this.downloadStatus;
    }

    public void setDownloadStatus(final String status) {
        if (!status.equalsIgnoreCase(MessageUtil.colorize("&cYou have downloaded the latest version but you are not using it"))) {
            this.downloadStatus = status;
        }
    }
}