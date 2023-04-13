package me.indian.ostag.util;

import me.indian.ostag.OsTag;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GithubUtil {

    private static final String current = OsTag.getInstance().getDescription().getVersion();
    private static final String latest = getLatestTag();
    private static final String errorMessage = "&cCan't get latest tag";

    public static String getFastTagInfo() {
        if (latest.equals(errorMessage)) {
            return ColorUtil.replaceColorCode(errorMessage);
        }
        if (current.equals(latest)) {
            return ColorUtil.replaceColorCode("&etrue");
        } else {
            return ColorUtil.replaceColorCode("&4false");
        }
    }

    public static String checkTagCompatibility() {
        String tag = "&aYou are running latest version";
        if (latest.equals(errorMessage)) {
            return ColorUtil.replaceColorCode(errorMessage);
        }
        if (!current.equals(latest)) {
            tag = "&aNew update available, your version &b" + current + "&a latest version &b" + latest;
        }
        return ColorUtil.replaceColorCode(tag);
    }

    public static String getLatestTag() {
        try {
            URL url = new URL("https://api.github.com/repos/OpenPlugins-Minecraft/OsTag/tags");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            return parseLatestTagFromJson(response.toString());
        } catch (IOException exception) {
            return errorMessage;
        }

    }

    private static String parseLatestTagFromJson(String json) {
        String[] tags = json.replaceAll("[\\[\\]{}\"]", "").split(",");
        return tags[0].split(":")[1];
    }
}