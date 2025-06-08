package me.aaron.TeraCore.commands;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.util.ArrayList;
import java.util.List;

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

public class weather implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	public weather() {
		String filetype = getClass().getSimpleName();
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("sun")) {

				String weather = "clear";
				if (player.hasPermission(config.getString("command.args1.permission").replace("%weather%", weather))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					World world = player.getWorld();
					world.setStorm(false);
					world.setThundering(false);
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
							.replace("%weather%", config.getString("placeholder." + weather))));
					return true;
				} else {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}

			}
			if (command.getName().equalsIgnoreCase("rain")) {

				String weather = "rain";
				if (player.hasPermission(config.getString("command.args1.permission").replace("%weather%", weather))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					World world = player.getWorld();
					world.setStorm(false);
					world.setThundering(false);
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
							.replace("%weather%", config.getString("placeholder." + weather))));
					return true;
				} else {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}

			}
			if (command.getName().equalsIgnoreCase("thunder")) {

				String weather = "thunder";
				if (player.hasPermission(config.getString("command.args1.permission").replace("%weather%", weather))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					World world = player.getWorld();
					world.setStorm(false);
					world.setThundering(false);
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
							.replace("%weather%", config.getString("placeholder." + weather))));
					return true;
				} else {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}

			}
			if (command.getName().equalsIgnoreCase("weather")) {
				if (args.length == 1) {
					String weather = args[0].toLowerCase();
					if (weather.equalsIgnoreCase("clear") || weather.equalsIgnoreCase("rain")
							|| weather.equalsIgnoreCase("thunder")) {
						if (player.hasPermission(
								config.getString("command.args1.permission").replace("%weather%", weather))
								|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
							World world = player.getWorld();
							if (weather.equalsIgnoreCase("clear")) {
								world.setStorm(false);
								world.setThundering(false);
							}
							if (weather.equalsIgnoreCase("rain")) {
								world.setStorm(true);
								world.setThundering(false);
							}
							if (weather.equalsIgnoreCase("thunder")) {
								world.setStorm(true);
								world.setThundering(true);
							}
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
									.replace("%weather%", config.getString("placeholder." + weather))));
							return true;
						} else {
							player.sendMessage(PlaceHolder
									.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
							return true;
						}
					}
				}
				if (hasWeatherPermission(player)
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

	public boolean hasWeatherPermission(Player player) {
		if (player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
			return true;
		}
		if (player.hasPermission(config.getString("command.args1.permission").replace("%weather%", "clear"))) {
			return true;
		}
		if (player.hasPermission(config.getString("command.args1.permission").replace("%weather%", "rain"))) {
			return true;
		}
		if (player.hasPermission(config.getString("command.args1.permission").replace("%weather%", "thunder"))) {
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("weather")) {
				try {
					if (player.hasPermission(config.getString("command.args1.permission").replace(".%weather%", ""))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						if (args.length == 1) {

							List<String> end = new ArrayList<>();

							tab.add("clear");
							tab.add("rain");
							tab.add("thunder");

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
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return tab;
	}
}
