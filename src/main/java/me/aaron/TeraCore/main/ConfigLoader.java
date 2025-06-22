package me.aaron.TeraCore.main;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigLoader {
    private static FileConfiguration config;
    private static String filetype;

    private static void initConfig() {
        // Datei aus dem JAR laden
        try (InputStream in = TeraMain.getPlugin().getResource("me/aaron/TeraCore/configs_" + TeraMain.getLanguage()+"/" + filetype + ".yml")) {
            if (in != null) {
                config = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
            } else {
                throw new IllegalArgumentException("File " + filetype + ".yml not found in the resource path." + "me/aaron/TeraCore/configs_" + TeraMain.getLanguage()+"/" + filetype + ".yml");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig(String type) {
        filetype = type;
        initConfig();
        return config;
    }
}
