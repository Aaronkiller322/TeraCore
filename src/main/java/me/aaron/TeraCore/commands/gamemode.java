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

public class gamemode implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;


	public gamemode() {
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
			if (command.getName().equalsIgnoreCase("gamemode")) {
				if (player.hasPermission(config.getString("command.args0.permission"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					if (args.length == 1) {
						try {
							GameMode gamemode = getGamemode(args[0]);
							String permission = config.getString("command.args1.permission").replace("%gamemode%", gamemode.name().toLowerCase());
							if (player.hasPermission(permission)
									|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
								player.setGameMode(gamemode);
								player.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args1.usage").replace("%gamemode%",
												config.getString("placeholder." + gamemode.name().toLowerCase()))));
								return true;
							} else {
								player.sendMessage(PlaceHolder.replacePlaceholder(
										DefaultConfig.getConfig().getString("message.no_permission")));
								return true;
							}
						} catch (Exception e) {
						}
					}
					if (args.length == 2) {
						try {
							GameMode gamemode = getGamemode(args[0]);
							String permission = config.getString("command.args1.permission").replace("%gamemode%", gamemode.name().toLowerCase());
							if (player.hasPermission(permission)
									|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
								Player trust = Bukkit.getPlayer(args[1]);
								if (trust != null) {
									trust.setGameMode(gamemode);
									player.sendMessage(PlaceHolder.replacePlaceholder(config
											.getString("command.args2.usage")
											.replace("%gamemode%",
													config.getString("placeholder." + gamemode.name().toLowerCase()))
											.replace("%trust%", trust.getDisplayName())));
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.usage").replace("%gamemode%",
													config.getString("placeholder." + gamemode.name().toLowerCase()))));
									return true;
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

	private GameMode getGamemode(String gamemode) {
		if (gamemode.equalsIgnoreCase("0") || gamemode.equalsIgnoreCase("SURVIVAL") || gamemode.equalsIgnoreCase("S")) {
			return GameMode.SURVIVAL;
		}
		if (gamemode.equalsIgnoreCase("1") || gamemode.equalsIgnoreCase("CREATIVE") || gamemode.equalsIgnoreCase("C")) {
			return GameMode.CREATIVE;
		}
		if (gamemode.equalsIgnoreCase("2") || gamemode.equalsIgnoreCase("ADVENTURE")
				|| gamemode.equalsIgnoreCase("A")) {
			return GameMode.ADVENTURE;
		}
		if (gamemode.equalsIgnoreCase("3") || gamemode.equalsIgnoreCase("SPECTATOR")
				|| gamemode.equalsIgnoreCase("SP")) {
			return GameMode.SPECTATOR;
		}
		return null;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("gamemode")) {
				try {
					if (player.hasPermission(config.getString("command.args0.permission"))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						if (args.length == 1) {

							List<String> end = new ArrayList<>();

							tab.add(GameMode.SURVIVAL.name().toLowerCase());
							tab.add(GameMode.CREATIVE.name().toLowerCase());
							tab.add(GameMode.ADVENTURE.name().toLowerCase());
							tab.add(GameMode.SPECTATOR.name().toLowerCase());

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
				} catch (Exception e) {
				}
			}
		}
		return tab;
	}
}
