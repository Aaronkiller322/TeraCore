package me.aaron.TeraCore.economy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.TeraMain;

public class Eco_Config {
    public File file;
    public FileConfiguration config;

	String datafolder = "plugins/" + TeraMain.getPlugin().getName();

	public Eco_Config() {
		String filetype = "economy";
		File temp = new File(datafolder, filetype + ".yml");
		if (temp.exists()) {
			file = temp;
			config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
		} else {
			config = getConfig(filetype);
			file = temp;
			try {
				config.save(temp);
			} catch (IOException e) {
			}
		}

	}
    public boolean enabled(){
        return config.getBoolean("economy.enabled");
    }

    private static FileConfiguration config_temp;
    private static String filetype;

    private static void initConfig() {
        // Datei aus dem JAR laden
        try (InputStream in = TeraMain.getPlugin().getResource("me/aaron/TeraCore/economy/" + filetype + ".yml")) {
            if (in != null) {
            	config_temp = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
            } else {
                throw new IllegalArgumentException("Datei " + filetype + ".yml nicht gefunden im Ressourcenpfad.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FileConfiguration getConfig(String type) {
        filetype = type;
        initConfig();
        return config_temp;
    }
	
}
