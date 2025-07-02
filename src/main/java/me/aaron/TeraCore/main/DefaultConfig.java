package me.aaron.TeraCore.main;

import java.io.File;
import java.io.IOException;

import me.aaron.TeraCore.commands.CommandMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultConfig {

	private static File file;
	private static FileConfiguration config;

	public static void LoadDefaultConfig() {
		String filetype = "default";
		LanguageLoader.load(LanguageLoader.LanguageFolder.commands, filetype);
		File temp = new File(CommandMain.datafolder, filetype + ".yml");
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
