package me.aaron.TeraCore.color;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.aaron.TeraCore.main.TeraMain;

public class PrefixManager {

	private static File file_prefix;
	private static FileConfiguration config_prefix;

	public static String getPrefix() {
		return TeraMain.getPlugin().prefix;
	}
	public static String getName() {
		return TeraMain.getPlugin().name;
	}
	
	public static void loadPrefix() {
		
		
	    if (file_prefix == null) {
	        file_prefix = new File("plugins/" + TeraMain.getPlugin().getName(), "prefix.yml");
	        config_prefix = YamlConfiguration.loadConfiguration(file_prefix);
	    }
		if(config_prefix.get("default-prefix") == null) {
			config_prefix.set("default-prefix", TeraMain.getPlugin().getName());
			try {
				config_prefix.save(file_prefix);
			} catch (IOException e) {
			}
		}
		if(config_prefix.get("split-prefix") == null) {
			config_prefix.set("split-prefix", "§8 ►§7");
			try {
				config_prefix.save(file_prefix);
			} catch (IOException e) {
			}
		}
		if(config_prefix.get("HexColorTextGenerator-Prefix.enabled") == null) {
			config_prefix.set("HexColorTextGenerator-Prefix.enabled", true);
			try {
				config_prefix.save(file_prefix);
			} catch (IOException e) {
			}
		}
		
		if(config_prefix.get("HexColorTextGenerator-Prefix.start") == null) {
			config_prefix.set("HexColorTextGenerator-Prefix.start", "#32FFFF");
			try {
				config_prefix.save(file_prefix);
			} catch (IOException e) {
			}
		}
		
		if(config_prefix.get("HexColorTextGenerator-Prefix.end") == null) {
			config_prefix.set("HexColorTextGenerator-Prefix.end", "#1E9999");
			try {
				config_prefix.save(file_prefix);
			} catch (IOException e) {
			}
		}
		
		boolean hexcolor = config_prefix.getBoolean("HexColorTextGenerator-Prefix.enabled");
		String prefix = config_prefix.getString("default-prefix");
		
		//HexColorTextGenerator 
		TeraMain.getPlugin().name = prefix;
		if(hexcolor) {
			String start = config_prefix.getString("HexColorTextGenerator-Prefix.start");
			String end = config_prefix.getString("HexColorTextGenerator-Prefix.end");
			
			prefix = HexColorTextGenerator.converHexcolorText(prefix, start, end);
		}
		
		TeraMain.getPlugin().name = HexColorTextGenerator.convertHexColorToChatColor(prefix);
		
		TeraMain.getPlugin().prefix = TeraMain.getPlugin().name + config_prefix.getString("split-prefix");
	}

	
	
	
}
