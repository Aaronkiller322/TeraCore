package me.aaron.TeraCore.commands;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.LanguageLoader;
import me.aaron.TeraCore.main.TeraMain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class back implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;


	public back() {
		String filetype = getClass().getSimpleName();
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

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("back")) {
				
				if (player.hasPermission(config.getString("command.args0.permission"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					if (args.length == 0) {

						ArrayList<Location> loc = new ArrayList<>();
						if(TeraMain.back_location.containsKey(player)){
							loc = TeraMain.back_location.get(player);
						}

						if(loc.size() < 1){
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.failed")));
							return true;
						}
						Location location = loc.get(0);

						player.teleport(location);
						loc.remove(location);

						TeraMain.back_location.put(player, loc);

						player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage")));
						return true;
					}
					if (args.length == 1) {

						int num = 0;

						try{
						num = Integer.valueOf(args[0]) +-1;
						}catch (Exception ex){
							num = 1;
						}
						ArrayList<Location> loc = new ArrayList<>();
						if(TeraMain.back_location.containsKey(player)){
							loc = TeraMain.back_location.get(player);
						}

						if(loc.size() < 1){
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.failed")));
							return true;
						}

						Location location;
						try {
							location = loc.get(num);
						}catch (Exception exception){
							location = loc.get(0);
						}

						player.teleport(location);
						loc.remove(location);

						TeraMain.back_location.put(player, loc);

						player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage")));
						return true;
					}
				} else {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
			}
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("eat")) {
			try {
			if (player.hasPermission(config.getString("command.args0.permission"))
					|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
				if (args.length == 1) {
					List<String> end = new ArrayList<>();

					for (Player online : Bukkit.getOnlinePlayers()) {
						tab.add(online.getDisplayName());
					}
					for (int i = 0; i < tab.size(); i++) {
						if (args[0] != null) {
							if (tab.get(i).toLowerCase().startsWith(args[0].toLowerCase())) {
								end.add(tab.get(i));
							}
						}
					}
					return end;
				}

			}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		}
		return tab;
	}
}
