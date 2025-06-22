package me.aaron.TeraCore.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
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
import me.aaron.TeraCore.util.HomeManager;
import me.aaron.TeraCore.util.WarpManager;

public class warp implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	public warp() {
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
			if (command.getName().equalsIgnoreCase("warp")) {

				if (args.length == 0) {
					ArrayList<String> warps = new WarpManager().getWarps();
					if (warps.size() < 1) {
						String empty = PlaceHolder.replacePlaceholder(config.getString("command.list.empty"));
						player.sendMessage(empty);
						return true;
					}
					String start = PlaceHolder.replacePlaceholder(config.getString("command.list.message"));
					String split = PlaceHolder.replacePlaceholder(config.getString("command.list.split"));
					StringBuilder result = new StringBuilder(start);

					for (int i = 0; i < warps.size(); i++) {
						result.append(warps.get(i));
						if (i < warps.size() - 1) {
							result.append(split);
						}
					}

					String finalResult = result.toString();
					player.sendMessage(finalResult);
					return true;
				}
				if (args.length == 1) {
					String warp = args[0].toLowerCase();
					WarpManager manager = new WarpManager();
					if (!manager.existWarp(warp)) {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(config.getString("command.list.failed").replace("%warp%", warp)));
						return true;
					}
					Location location = manager.getWarp(warp);

					player.teleport(location);
					player.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.list.teleport").replace("%warp%", warp)));
					return true;
				}
			}
			if (command.getName().equalsIgnoreCase("setwarp")) {
				if (!player.hasPermission(config.getString("command.setwarp.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 0) {
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.setwarp.help")));
					return true;
				}
				if (args.length == 1) {
					String warp = args[0].toLowerCase();
					WarpManager manager = new WarpManager();
					if (manager.existWarp(warp)) {
						player.sendMessage(PlaceHolder.replacePlaceholder(
								config.getString("command.setwarp.failed").replace("%warp%", warp)));
						return true;
					}
					manager.setWarp(warp, player.getLocation());
					player.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.setwarp.message").replace("%warp%", warp)));
					return true;
				}
			}
			if (command.getName().equalsIgnoreCase("delwarp")) {
				if (!player.hasPermission(config.getString("command.delwarp.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 0) {
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.delwarp.help")));
					return true;
				}
				if (args.length == 1) {
					String warp = args[0].toLowerCase();
					WarpManager manager = new WarpManager();
					if (!manager.existWarp(warp)) {
						player.sendMessage(PlaceHolder.replacePlaceholder(
								config.getString("command.delwarp.failed").replace("%warp%", warp)));
						return true;
					}
					manager.delWarp(warp);
					player.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.delwarp.message").replace("%warp%", warp)));
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
		if (command.getName().equalsIgnoreCase("warp") || command.getName().equalsIgnoreCase("delwarp")) {
			try {
				if (command.getName().equalsIgnoreCase("delwarp")) {
					if (!player.hasPermission(config.getString("command.delwarp.permission"))) {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
						return tab;
					}
				}
				if (args.length == 1) {
					List<String> end = new ArrayList<>();

					WarpManager manager = new WarpManager();
					for (String warp : manager.getWarps()) {
						tab.add(warp);
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
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		}
		return tab;
	}
}
