package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginLogger;
import cn.nukkit.utils.Config;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.indian.ostag.OsTag;

public class UpDateUtil {

    private final OsTag plugin = OsTag.getInstance();
    private final PluginLogger logger = this.plugin.getLogger();
    private final Config config = this.plugin.getConfig();
    private final String debugPrefix = TextUtil.replaceColorCode(this.plugin.publicDebugPrefix + "&8[&dAutoUpdate&8] ");
    private final String pluginsPath = Server.getInstance().getPluginPath();
    private final String latestVersion = GithubUtil.getLatestTag();
    private final String currentVersion = this.plugin.getDescription().getVersion();
    private final String currentFileName = "Ostag-" + this.currentVersion + ".jar";
    private final String latestUrl = "https://github.com/OpenPlugins-Minecraft/OsTag/releases/download/" + this.latestVersion + "/OsTag-" + this.latestVersion + ".jar";
    private final String latestFileName = "OsTag-" + this.latestVersion + ".jar";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadUtil("Ostag-UpdateThread"));
    private boolean redownload = false;

    private static double bytesToKb(final long bytes) {
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        final String kb = df.format((double) bytes / 1024);

        return Double.parseDouble(kb);
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
        this.executorService.execute(() -> {
            if (GithubUtil.getFastTagInfo().contains("false")) {
                final File latest = new File(this.pluginsPath + "/" + this.latestFileName);
                final File current = new File(this.pluginsPath + "/" + this.currentFileName);

                if (current.exists() && latest.exists()) {
                    if (!this.currentVersion.equals(this.latestVersion)) {
                        this.logger.info(TextUtil.replaceColorCode("&cYou have downloaded the latest version but you are not using it"));
                        if (sender instanceof Player) {
                            sender.sendMessage(TextUtil.replaceColorCode("&cYou have downloaded the latest version but you are not using it"));
                        }
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (!latest.exists()) {
                    this.logger.info(TextUtil.replaceColorCode("&aDownloading latest ostag version..."));
                    this.downloadLatestVersion(sender);
                }
            } else {
                if (this.plugin.debug) {
                    this.logger.info(TextUtil.replaceColorCode(this.debugPrefix + "&aDownloading the latest version is unnecessary or not possible"));
                }
                if (sender != null) {
                    sender.sendMessage(TextUtil.replaceColorCode("&aDownloading the latest version is unnecessary or not possible"));
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
                    this.logger.info(TextUtil.replaceColorCode(this.debugPrefix + "&b" + this.latestUrl));
                    this.logger.info(TextUtil.replaceColorCode(this.debugPrefix + "&aVersion: &b" + this.latestVersion));
                    this.logger.info(TextUtil.replaceColorCode(this.debugPrefix + "&aContent type: &b" + contentType));
                    this.logger.info(TextUtil.replaceColorCode(this.debugPrefix + "&aContent length: &b" + contentLength));
                    this.logger.info(TextUtil.replaceColorCode(this.debugPrefix + "&eStarting downloading"));
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
                    final String progressMsg = TextUtil.replaceColorCode("&aDownload progress: &b" + (totalBytesRead * 100) / contentLength + "%" + " &8(&b" + bytesToKb(totalBytesRead) + "kb&8)");
                    if (this.plugin.debug) {
                        this.logger.info(this.debugPrefix + progressMsg);
                    }
                    if (sender instanceof Player) {
                        ((Player) sender).sendActionBar(this.plugin.pluginPrefix + progressMsg);
                    }
                }

                outputStream.close();
                inputStream.close();

                if (totalBytesRead != contentLength) {
                    this.logger.warning(TextUtil.replaceColorCode("&cDownload failed: Incomplete download"));
                    if (sender instanceof Player) {
                        sender.sendMessage(TextUtil.replaceColorCode("&cDownload failed: Incomplete download"));
                        sender.sendMessage(TextUtil.replaceColorCode("&aTrying to redownload"));
                    }
                    this.reDownload(sender);
                    return;
                }

                final double executionTimeInSeconds = (System.currentTimeMillis() - millisActualTime) / 1000.0;
                this.logger.info(TextUtil.replaceColorCode("&aDownload completed in &b" + executionTimeInSeconds + " &aseconds"));
                if (sender instanceof Player) {
                    sender.sendMessage(TextUtil.replaceColorCode("&aDownload completed in &b" + executionTimeInSeconds + " &aseconds"));
                }
            } else {
                this.logger.warning(TextUtil.replaceColorCode("&cThe file could not be used. HTTP response code:" + responseCode));
                Thread.currentThread().interrupt();
            }
            httpConnection.disconnect();
        } catch (final Exception e) {
            this.logger.warning(TextUtil.replaceColorCode("&cCan't download latest ostag version!"));
            if (this.plugin.debug) {
                this.logger.error(this.debugPrefix + e);
            }
            this.reDownload(sender);
            Thread.currentThread().interrupt();
        }
    }

    private void reDownload(final CommandSender sender) {
        if (this.redownload) {
            this.redownload = false;
            this.logger.warning(TextUtil.replaceColorCode("&cRedownload failed"));
            if (sender instanceof Player) {
                sender.sendMessage(TextUtil.replaceColorCode("&cRedownload failed"));
            }
        } else {
            this.redownload = true;
            this.logger.info(TextUtil.replaceColorCode("&aTrying to redownload"));
            if (sender instanceof Player) {
                sender.sendMessage(TextUtil.replaceColorCode("&aTrying to redownload"));
            }
            this.upDate(sender);
        }
    }
}