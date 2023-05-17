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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.indian.ostag.OsTag;

public class UpDateUtil {

    private final OsTag plugin = OsTag.getInstance();
    private final PluginLogger logger = plugin.getLogger();
    private final Config config = plugin.getConfig();
    private final String debugPrefix = ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&8[&dAutoUpdate&8] ");
    private final String pluginsPath = Server.getInstance().getPluginPath();
    private final String latestVersion = GithubUtil.getLatestTag();
    private final String currentVersion = plugin.getDescription().getVersion();
    private final String latestUrl = "https://github.com/OpenPlugins-Minecraft/OsTag/releases/download/" + latestVersion + "/OsTag-" + latestVersion + ".jar";
    private final String latestFileName = "OsTag-" + latestVersion + ".jar";
    private final String currentFileName = "Ostag-" + currentVersion + ".jar";
    private boolean redownload = false;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void autoUpDate() {
        if (config.getBoolean("AutoUpdate")) {
            upDate(null);
        }
    }

    public void manualUpDate(CommandSender sender) {
        upDate(sender);
    }

    private void upDate(CommandSender sender) {
        executorService.execute(() -> {
            if (GithubUtil.getFastTagInfo().contains("false")) {
                final File latest = new File(pluginsPath + "/" + latestFileName);
                final File current = new File(pluginsPath + "/" + currentFileName);

                if (current.exists() && latest.exists()) {
                    if (!currentVersion.equals(latestVersion)) {
                        logger.info(ColorUtil.replaceColorCode("&cYou have downloaded the latest version but you are not using it"));
                        if (sender instanceof Player) {
                            sender.sendMessage(ColorUtil.replaceColorCode("&cYou have downloaded the latest version but you are not using it"));
                        }
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (!latest.exists()) {
                    logger.info(ColorUtil.replaceColorCode("&aDownloading latest ostag version..."));
                    downloadLatestVersion(sender);
                }
            } else {
                if (plugin.debug) {
                    logger.info(ColorUtil.replaceColorCode(debugPrefix + "&aDownloading the latest version is unnecessary or not possible"));
                }
                if (sender != null) {
                    sender.sendMessage(ColorUtil.replaceColorCode("&aDownloading the latest version is unnecessary or not possible"));
                }
                Thread.currentThread().interrupt();
            }
        });
    }

    private void downloadLatestVersion(CommandSender sender) {
        try {
            final long millisActualTime = System.currentTimeMillis();
            final URL url = new URL(latestUrl);
            final HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            final int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                final String contentType = httpConnection.getContentType();
                final int contentLength = httpConnection.getContentLength();

                if (plugin.debug) {
                    logger.info(ColorUtil.replaceColorCode(debugPrefix + "&b" + latestUrl));
                    logger.info(ColorUtil.replaceColorCode(debugPrefix + "&aVersion: &b" + latestVersion));
                    logger.info(ColorUtil.replaceColorCode(debugPrefix + "&aContent type: &b" + contentType));
                    logger.info(ColorUtil.replaceColorCode(debugPrefix + "&aContent length: &b" + contentLength));
                    logger.info(debugPrefix + ColorUtil.replaceColorCode("&eStarting downloading"));
                }
                final InputStream inputStream = httpConnection.getInputStream();
                final String saveFilePath = pluginsPath + File.separator + latestFileName;

                final FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                final byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    String progressMsg = ColorUtil.replaceColorCode("&aDownload progress: &b" + (totalBytesRead * 100) / contentLength + "%");
                    if (plugin.debug) {
                        logger.info(debugPrefix + progressMsg);
                    }
                    if (sender instanceof Player) {
                        ((Player) sender).sendActionBar(plugin.pluginPrefix + progressMsg);
                    }
                }

                outputStream.close();
                inputStream.close();

                if (totalBytesRead != contentLength) {
                    logger.warning(ColorUtil.replaceColorCode("&cDownload failed: Incomplete download"));
                    logger.info(ColorUtil.replaceColorCode("&aTrying to redownload"));
                    if (sender instanceof Player) {
                        sender.sendMessage(ColorUtil.replaceColorCode("&cDownload failed: Incomplete download"));
                        sender.sendMessage(ColorUtil.replaceColorCode("&aTrying to redownload"));
                    }
                    if(redownload) {
                        logger.warning(ColorUtil.replaceColorCode("&cRedownload failed"));
                        if (sender instanceof Player) {
                            sender.sendMessage(ColorUtil.replaceColorCode("&cRedownload failed"));
                        }
                        redownload = false;
                        return;
                    } else {
                        redownload = true;
                        upDate(sender);
                    }
                    return;
                }

                final double executionTimeInSeconds = (System.currentTimeMillis() - millisActualTime) / 1000.0;
                logger.info(ColorUtil.replaceColorCode("&aDownload completed in &b" + executionTimeInSeconds + " &aseconds"));
                if (sender instanceof Player) {
                    sender.sendMessage(ColorUtil.replaceColorCode("&aDownload completed in &b" + executionTimeInSeconds + " &aseconds"));
                }
            } else {
                logger.warning(ColorUtil.replaceColorCode("&cThe file could not be used. HTTP response code:" + responseCode));
                Thread.currentThread().interrupt();
            }
            httpConnection.disconnect();
        } catch (Exception e) {
            logger.warning(ColorUtil.replaceColorCode("&cCan't download latest ostag version!"));
            if (plugin.debug) {
                logger.error(debugPrefix + e);
            }
            Thread.currentThread().interrupt();
        }
    }
}