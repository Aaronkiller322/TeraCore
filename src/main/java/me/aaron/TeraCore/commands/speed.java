package me.aaron.TeraCore.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

public class speed implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	public speed() {
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
			if (command.getName().equalsIgnoreCase("speed")) {
				if (args.length > 0) {
					String speed_str = args[0];
					if (player.hasPermission(config.getString("command.args1.permission").replace("%speed%", speed_str))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						
						try {
						int speed = Integer.valueOf(speed_str);
						if(speed > 0 && speed < 6) {
							if (args.length == 1) {
								player.setWalkSpeed((float) (0.2 * speed));
								if(speed == 1) {
									player.setFlySpeed((float) (0.1 * speed));
								}else {
								player.setFlySpeed((float) (0.2 * speed));
								}
								player.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args1.usage").replace("%speed%", speed_str)));
								return true;
							}
							if (args.length == 2) {
								if (player.hasPermission(config.getString("command.args2.permission").replace("%speed%", speed_str))
										|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
								Player trust = Bukkit.getPlayer(args[1]);
								if(trust == null) {
									player.sendMessage(PlaceHolder.replacePlaceholder(
											DefaultConfig.getConfig().getString("message.player_not_found")));
									return true;
								}
								
								trust.setWalkSpeed((float) (0.2 * speed));
								if(speed == 1) {
									trust.setFlySpeed((float) (0.1 * speed));
								}else {
									trust.setFlySpeed((float) (0.2 * speed));
								}
								trust.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args1.usage").replace("%speed%", speed_str)));
								player.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args2.usage").replace("%speed%", speed_str).replace("%trust%", trust.getDisplayName())));
								return true;
								}
							}
						}
						
						}catch (Exception e) {
							
						}
					} else {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
						return true;
					}

				}
				player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help")));
				return true;
			}
		}
		return false;
	}
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("speed")) {
			try {
			if (player.hasPermission(config.getString("command.args1.permission").replace(".%speed%", ""))
					|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
				if (args.length == 1) {

					List<String> end = new ArrayList<>();

					tab.add("1");
					tab.add("2");
					tab.add("3");
					tab.add("4");
					tab.add("5");

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

					for (Player online : Bukkit.getOnlinePlayers()) {
						tab.add(online.getDisplayName());

					}

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
