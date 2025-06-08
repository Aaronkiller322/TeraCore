package me.aaron.TeraCore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.aaron.TeraCore.main.TeraMain;

public class HomeManager {

	private File file;
	private FileConfiguration config;
	
	
	public HomeManager(UUID uuid) {
		String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/data/homes/";
		file = new File(datafolder, uuid.toString() + ".yml");
		config = YamlConfiguration.loadConfiguration(file);
	}
	

	public void setHome(String name, Location location) {
		config.set("homes." + name.toLowerCase() + ".x", location.getX());
		config.set("homes." + name.toLowerCase() + ".y", location.getY());
		config.set("homes." + name.toLowerCase() + ".z", location.getZ());
		config.set("homes." + name.toLowerCase() + ".yaw", location.getYaw());
		config.set("homes." + name.toLowerCase() + ".pitch", location.getPitch());
		config.set("homes." + name.toLowerCase() + ".world", location.getWorld().getName());
		saveconfig();
	}
	
	public void delHome(String name) {
		if(existHome(name)) {
		config.set("homes." + name.toLowerCase(), null);
		saveconfig();
		}
	}
	
	public boolean existHome(String name) {
		if(config.get("homes." + name.toLowerCase()) != null) {
			return true;
		}
		return false;
	}
	public Location getHome(String name) {
		if(existHome(name)) {
			double x = config.getDouble("homes." + name.toLowerCase() + ".x");
			double y = config.getDouble("homes." + name.toLowerCase() + ".y");
			double z = config.getDouble("homes." + name.toLowerCase() + ".z");
			double yaw = config.getDouble("homes." + name.toLowerCase() + ".yaw");
			double pitch = config.getDouble("homes." + name.toLowerCase() + ".pitch");
			String world = config.getString("homes." + name.toLowerCase() + ".world");
			Location location = new Location(Bukkit.getWorld(world), x, y, z);
			location.setYaw((float) yaw);
			location.setPitch((float) pitch);
			return location;
		}
		return null;
	}
	
	public ArrayList<String> getHomes(){
		ArrayList<String> list = new ArrayList<String>();
		try {
			for(String home : config.getConfigurationSection("homes.").getKeys(false)) {
				list.add(home);
			}
		}catch (Exception e) {
		}
		return list;
	}
	
	private void saveconfig() {
		try {
		config.save(file);
		}catch (Exception e) {
		}
	}
	
}
