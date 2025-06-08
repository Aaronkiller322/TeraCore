package me.aaron.TeraCore.economy;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.aaron.TeraCore.main.TeraMain;


public class MoneyConfig {

	private static File file = new File("plugins/" + TeraMain.getPlugin().getName() + "/data/money.yml");
	private static FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	
	public static FileConfiguration getConfig() {
		return config;
	}
	public static void saveConfig() {
		try {
		config.save(file);
		}catch (Exception e) {
		}
	}
	
}
