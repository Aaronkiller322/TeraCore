package me.aaron.TeraCore.commands;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import java.util.List;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.TeraMain;

public class time implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	public time() {
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
			if (command.getName().equalsIgnoreCase("day")) {
				if (player.hasPermission(config.getString("command.args2.permission").replace("%time%", "day"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					World world = player.getWorld();
					world.setTime(Integer.valueOf(config.getString("time.day")));
					
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args2.usage")
							.replace("%time%", config.getString("placeholder.day"))));
					return true;
				} else {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
			}
			if (command.getName().equalsIgnoreCase("night")) {
				if (player.hasPermission(config.getString("command.args2.permission").replace("%time%", "night"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					World world = player.getWorld();
					world.setTime(Integer.valueOf(config.getString("time.night")));
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args2.usage")
							.replace("%time%", config.getString("placeholder.night"))));
					return true;
				} else {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
			}
			if (command.getName().equalsIgnoreCase("time")) {
				if (args.length == 2) {
					String time = args[1];
					boolean error = false;
					if (!time.equalsIgnoreCase("day") && !time.equalsIgnoreCase("night")) {
						try {
							int i = Integer.valueOf(time);
							time = "nummer";
						} catch (Exception e) {
							error = true;
						}
					}
					if (!error) {
						if (player.hasPermission(
								config.getString("command.args2.permission").replace("%time%", time.toLowerCase()))
								|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
							World world = player.getWorld();
							if (time.equalsIgnoreCase("day")) {
								world.setTime(Integer.valueOf(config.getString("time.day")));
								player.sendMessage(
										PlaceHolder.replacePlaceholder(config.getString("command.args2.usage")
												.replace("%time%", config.getString("placeholder.day"))));
								return true;
							}
							if (time.equalsIgnoreCase("night")) {
								world.setTime(Integer.valueOf(config.getString("time.night")));
								player.sendMessage(
										PlaceHolder.replacePlaceholder(config.getString("command.args2.usage")
												.replace("%time%", config.getString("placeholder.night"))));
								return true;
							}
							if (time.equalsIgnoreCase("nummer")) {
								int i = Integer.valueOf(args[1]);
								world.setTime(i);
								player.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args2.usage").replace("%time%", String.valueOf(i))));
								return true;
							}
						}
					}
				}
				if (player.hasPermission(config.getString("command.args2.permission").replace(".%time%", ""))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {

					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help")));

					return true;
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
		if (command.getName().equalsIgnoreCase("time")) {
			try {
			if (player.hasPermission(config.getString("command.args2.permission").replace(".%time%", ""))
					|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
				if (args.length == 1) {

					List<String> end = new ArrayList<>();

					tab.add("set");

					for (int i = 0; i < tab.size(); i++) {

						if (args[0] != null) {

							if (tab.get(i).toLowerCase().startsWith(args[0].toLowerCase())) {
								end.add(tab.get(i));
							}

						}

					}

					return end;
				}
				if (args.length == 2) {

					List<String> end = new ArrayList<>();

					tab.add("day");
					tab.add("night");

					for (int i = 0; i < tab.size(); i++) {

						if (args[1] != null) {

							if (tab.get(i).toLowerCase().startsWith(args[1].toLowerCase())) {
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
