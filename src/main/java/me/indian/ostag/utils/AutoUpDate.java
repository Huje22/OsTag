package me.indian.ostag.utils;

import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import me.indian.ostag.OsTag;

public class AutoUpDate {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config config = plugin.getConfig();
    private static final String pluginsPath = Server.getInstance().getPluginPath();
    private static final String latestVersion = GithubUtil.getLatestTag();
    private static final String currentVersion = plugin.getDescription().getVersion();
    private static final String latestUrl = "https://github.com/OpenPlugins-Minecraft/OsTag/releases/download/" + latestVersion + "/OsTag-" + latestVersion + ".jar";
    private static final String latestFileName = "OsTag-" + latestVersion + ".jar";
    private static final String currentFileName = "Ostag-" + currentVersion + ".jar";

    public static void start() {
        if (config.getBoolean("AutoUpdate")) {
            if (GithubUtil.getFastTagInfo().contains("false")) {
                File latest = new File(pluginsPath + "/" + latestFileName);
                File current = new File(pluginsPath + "/" + currentFileName);

                if (current.exists() && latest.exists()) {
                    if (!currentVersion.equals(latestVersion)) {
                        plugin.getLogger().info(ColorUtil.replaceColorCode("&cYou have downloaded the latest version but you are not using it"));
                        return;
                    }
                }
                if (!latest.exists()) {
                    plugin.getLogger().info(ColorUtil.replaceColorCode("&aDownloading latest ostag version..."));
                    if (plugin.debug) {
                        plugin.getLogger().info(ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&b" + latestUrl));
                    }
                    downloadLatestVersion();
                }
            } else {
                if (plugin.debug) {
                    plugin.getLogger().info(ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&aDownloading the latest version is unnecessary or not possible"));
                }
            }
        }
    }

    private static void downloadLatestVersion() {
        try {
            URL url = new URL(latestUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            int responseCode = httpConnection.getResponseCode();

            // sprawdzanie kodu odpowiedzi HTTP
            if (responseCode == HttpURLConnection.HTTP_OK) {

                String contentType = httpConnection.getContentType();
                int contentLength = httpConnection.getContentLength();

                if (plugin.debug) {
                    plugin.getLogger().info(ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&aFile download: &b" + latestFileName));
                    plugin.getLogger().info(ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&aContent type: &b" + contentType));
                    plugin.getLogger().info(ColorUtil.replaceColorCode(plugin.publicDebugPrefix + "&aContent length: &b" + contentLength));
                }
                // otwieranie strumienia wejściowego z połączenia HTTP
                InputStream inputStream = httpConnection.getInputStream();
                String saveFilePath = pluginsPath + File.separator + latestFileName;

                // otwieranie strumienia wyjściowego do zapisu pliku
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                // zapisywanie danych z wejściowego strumienia do wyjściowego strumienia
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                plugin.getLogger().info(ColorUtil.replaceColorCode("&aDownload completed."));

                // zwalnianie zasobów
                outputStream.close();
                inputStream.close();
            } else {
                plugin.getLogger().warning(ColorUtil.replaceColorCode("&cThe file could not be used. HTTP response code:" + responseCode));
            }
            httpConnection.disconnect();
        } catch (Exception e) {
            plugin.getLogger().warning(ColorUtil.replaceColorCode("&cCan't download latest ostag version!"));
        }
    }
}