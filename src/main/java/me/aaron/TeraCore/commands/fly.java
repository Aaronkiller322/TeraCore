package me.aaron.TeraCore.commands;

import java.io.File;
import java.io.IOException;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.TeraMain;

public class fly implements CommandExecutor {

	File file;
	FileConfiguration config;


	public fly() {
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
			if (command.getName().equalsIgnoreCase("fly")) {
				if (player.hasPermission(config.getString("command.args0.permission"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					if (args.length == 0) {
						if (!player.getAllowFlight()) {
							player.setAllowFlight(true);
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage")
									.replace("%status%", config.getString("placeholder.enabled"))));
							return true;

						} else {
							player.setAllowFlight(false);
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage")
									.replace("%status%", config.getString("placeholder.disabled"))));
							return true;

						}
					}
					if (args.length == 1) {
						try {
							if (player.hasPermission(config.getString("command.args1.permission"))
									|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
								Player trust = Bukkit.getPlayer(args[0]);
								if (trust != null) {
									if (!trust.getAllowFlight()) {
										trust.setAllowFlight(true);
										player.sendMessage(
												PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
														.replace("%trust%", trust.getDisplayName())
														.replace("%status%", config.getString("placeholder.enabled"))));
										trust.sendMessage(
												PlaceHolder.replacePlaceholder(config.getString("command.args0.usage")
														.replace("%status%", config.getString("placeholder.enabled"))));
										return true;
									} else {
										trust.setAllowFlight(false);
										player.sendMessage(
												PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
														.replace("%trust%", trust.getDisplayName()).replace("%status%",
																config.getString("placeholder.disabled"))));
										trust.sendMessage(PlaceHolder.replacePlaceholder(
												config.getString("command.args0.usage").replace("%status%",
														config.getString("placeholder.disabled"))));
										return true;
									}
								} else {
									player.sendMessage(PlaceHolder.replacePlaceholder(
											DefaultConfig.getConfig().getString("message.player_not_found")));
									return true;
								}
							} else {
								player.sendMessage(PlaceHolder.replacePlaceholder(
										DefaultConfig.getConfig().getString("message.no_permission")));
								return true;
							}
						} catch (Exception e) {

						}
						player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help")));
						return true;
					}

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

}
