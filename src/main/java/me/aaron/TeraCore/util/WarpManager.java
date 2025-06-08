package me.aaron.TeraCore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.aaron.TeraCore.main.TeraMain;

public class WarpManager {

	private File file;
	private FileConfiguration config;
	public WarpManager() {
		String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/data";
		file = new File(datafolder, "warps.yml");
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	
	public void setWarp(String name, Location location) {
		config.set("warps." + name.toLowerCase() + ".x", location.getX());
		config.set("warps." + name.toLowerCase() + ".y", location.getY());
		config.set("warps." + name.toLowerCase() + ".z", location.getZ());
		config.set("warps." + name.toLowerCase() + ".yaw", location.getYaw());
		config.set("warps." + name.toLowerCase() + ".pitch", location.getPitch());
		config.set("warps." + name.toLowerCase() + ".world", location.getWorld().getName());
		saveconfig();
	}
	
	public void delWarp(String name) {
		if(existWarp(name)) {
		config.set("warps." + name.toLowerCase(), null);
		saveconfig();
		}
	}
	
	public boolean existWarp(String name) {
		if(config.get("warps." + name.toLowerCase()) != null) {
			return true;
		}
		return false;
	}
	public Location getWarp(String name) {
		if(existWarp(name)) {
			double x = config.getDouble("warps." + name.toLowerCase() + ".x");
			double y = config.getDouble("warps." + name.toLowerCase() + ".y");
			double z = config.getDouble("warps." + name.toLowerCase() + ".z");
			double yaw = config.getDouble("warps." + name.toLowerCase() + ".yaw");
			double pitch = config.getDouble("warps." + name.toLowerCase() + ".pitch");
			String world = config.getString("warps." + name.toLowerCase() + ".world");
			Location location = new Location(Bukkit.getWorld(world), x, y, z);
			location.setYaw((float) yaw);
			location.setPitch((float) pitch);
			return location;
		}
		return null;
	}
	
	public ArrayList<String> getWarps(){
		ArrayList<String> list = new ArrayList<String>();
		try {
			for(String warps : config.getConfigurationSection("warps.").getKeys(false)) {
				list.add(warps);
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
