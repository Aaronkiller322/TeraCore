package me.aaron.TeraCore.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultConfig {

	private static File file;
	private static FileConfiguration config;

	private static String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	public static void LoadDefaultConfig() {
		String filetype = "default";
		File temp = new File(datafolder, filetype + ".yml");
		if (temp.exists()) {
			file = temp;
			config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
		} else {
			config = ConfigLoader.getConfig(filetype);
			file = temp;
			try {
				config.save(temp);
			} catch (IOException e) {
			}
		}

	}
	
	public static FileConfiguration getConfig() {
		return config;
	}
	
}
