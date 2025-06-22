package me.aaron.TeraCore.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.TeraMain;

public class MotdManager {

	private static File file_motd = new File("plugins/" + TeraMain.getPlugin().getName() , "motd.yml");
	private static FileConfiguration config_motd = YamlConfiguration.loadConfiguration(file_motd);
	
	private static String line1 = "&e► 🏗 %teracore_<gradient:32FFFF:1E9999>TeraCore</gradient>% &8▪ &e🌆 %teracore_<gradient:D1FFED:93FF72>Plugin Version 2.5.3</gradient>%";
	private static String line2 = "&e🚧 %teracore_<gradient:FF2B1C:FF6A00>By Aaronkiller322</gradient>% &e🚧";

	public MotdManager() {
		loadConfig();
	}
	
	public static String getLine1() {
		return PlaceHolder.replacePlaceholder(line1);
	}
	public static String getLine2() {
		return PlaceHolder.replacePlaceholder(line2);
	}
	
	public static void setLine1(String text) {
			config_motd.set("motd.line1", text);
			try {
				config_motd.save(file_motd);
			} catch (IOException e) {
			
		}
	}
	public static void setLine2(String text) {
			config_motd.set("motd.line2", text);
			try {
				config_motd.save(file_motd);
			} catch (IOException e) {
			}
		
	}
	public static void loadConfig() {
		
		
		if(config_motd.get("motd.line1") == null) {
			config_motd.set("motd.line1", "&e► 🏗 %teracore_<gradient:32FFFF:1E9999>TeraCore</gradient>% &8▪ &e🌆 %teracore_<gradient:D1FFED:93FF72>Plugin Version 2.5.3</gradient>%");
			try {
				config_motd.save(file_motd);
			} catch (IOException e) {
			}
		}
		if(config_motd.get("motd.line2") == null) {
			config_motd.set("motd.line2", "&e🚧 %teracore_<gradient:FF2B1C:FF6A00>By Aaronkiller322</gradient>% &e🚧");
			try {
				config_motd.save(file_motd);
			} catch (IOException e) {
			}
		}
		
		line1 = config_motd.getString("motd.line1");
		line2 = config_motd.getString("motd.line2");
	}

	
	
	
}
