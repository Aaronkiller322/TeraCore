package me.aaron.TeraCore.main;

import java.io.InputStream;
import java.io.InputStreamReader;

import net.kyori.adventure.util.Nag;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigLoader {
    private static FileConfiguration config;
    private static String filetype;

    private static void initConfig() {
        // Datei aus dem JAR laden
        try (InputStream in = TeraMain.getPlugin().getResource("me/aaron/TeraCore/configs/" + filetype + ".yml")) {
            if (in != null) {
                config = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
            } else {
                throw new IllegalArgumentException("Datei " + filetype + ".yml nicht gefunden im Ressourcenpfad.");
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
