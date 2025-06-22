package me.aaron.TeraCore.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import me.aaron.TeraCore.util.BanManager;
import me.aaron.TeraCore.util.KickManager;
import me.aaron.TeraCore.util.UUIDFetcher;

public class tempban implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	public tempban() {
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
		String name = "Console";
		String permission = config.getString("command.permission");
		if (command.getName().equalsIgnoreCase("tempban")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				name = player.getName();
				if (!(player.hasPermission(permission)
						&& player.hasPermission(DefaultConfig.getConfig().getString("admin_permission")))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
			}

			if (args.length <= 1) {
				sender.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help")));
				sender.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help2")));
				sender.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help3")));
				return true;
			}
			if (args.length == 2) {

				UUID uuid = UUIDFetcher.getUUID(args[0]);

				if (uuid == null) {
					sender.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
					return true;
				}

				String playername = UUIDFetcher.getName(uuid);
				if(BanManager.isBanned(uuid)) {
					sender.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.failed").replace("%player%", playername)));
					return true;
				}
				
				
				String time = args[1];
				if(time.endsWith("d") || time.endsWith("h") || time.endsWith("m") || time.endsWith("s")) {
				
					int secound = 1000;
					int minutes = secound * 60;
					int hours = minutes * 60;
					int day = hours * 24;
					
					
					long temp = 0;
					
					if(time.endsWith("s")) {
						temp = secound * Integer.valueOf(time.replace("s", ""));
					}
					if(time.endsWith("m")) {
						temp = minutes * Integer.valueOf(time.replace("m", ""));
					}
					if(time.endsWith("h")) {
						temp = hours * Integer.valueOf(time.replace("h", ""));
					}
					if(time.endsWith("d")) {
						temp = day * Integer.valueOf(time.replace("d", ""));
					}
					
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (online.hasPermission(permission)) {
						online.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.usage")
								.replace("%sender%", name).replace("%player%", playername)));
					}
				}

				Bukkit.getConsoleSender().sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.usage")
						.replace("%sender%", name).replace("%player%", playername)));
				BanManager.banPlayer(uuid, sender, null, temp);
				return true;
				}
			}
			if (args.length >= 3) {
				StringBuilder reason = new StringBuilder();

				for (int i = 2; i < args.length; i++) {
					reason.append(args[i]).append(" ");
				}

				String finalReason = reason.toString().trim();
				UUID uuid = UUIDFetcher.getUUID(args[0]);

				if (uuid == null) {
					sender.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
					return true;
				}

				String playername = UUIDFetcher.getName(uuid);
				if(BanManager.isBanned(uuid)) {
					sender.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.failed").replace("%player%", playername)));
					return true;
				}
				String time = args[1];
				if(time.endsWith("d") || time.endsWith("h") || time.endsWith("m") || time.endsWith("s")) {
				
					int secound = 1000;
					int minutes = secound * 60;
					int hours = minutes * 60;
					int day = hours * 24;
					
					
					long temp = 0;
					
					if(time.endsWith("s")) {
						temp = secound * Integer.valueOf(time.replace("s", ""));
					}
					if(time.endsWith("m")) {
						temp = minutes * Integer.valueOf(time.replace("m", ""));
					}
					if(time.endsWith("h")) {
						temp = hours * Integer.valueOf(time.replace("h", ""));
					}
					if(time.endsWith("d")) {
						temp = day * Integer.valueOf(time.replace("d", ""));
					}
				
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (online.hasPermission(permission)) {
						online.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.usage")
								.replace("%sender%", name).replace("%player%", playername)));
					}
				}

				Bukkit.getConsoleSender().sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.usage")
						.replace("%sender%", name).replace("%player%", playername)));
				BanManager.banPlayer(uuid, sender, finalReason, temp);
				return true;
				}
			}
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();

		if (command.getName().equalsIgnoreCase("temban")) {
			try {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (!(player.hasPermission(config.getString("command.permission"))
							&& player.hasPermission(DefaultConfig.getConfig().getString("admin_permission")))) {
						return tab;
					}
				}
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

			} catch (Exception e) {
				// TODO: handle exception

			}
		}
		return tab;

	}
}
