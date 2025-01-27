package me.aaron.TeraCore.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

public class unban implements CommandExecutor {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	public unban() {
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
		String name = "Console";
		String permission = config.getString("command.permission");
		if (command.getName().equalsIgnoreCase("unban")) {
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

			if (args.length == 0) {
				sender.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help")));
				return true;
			}
			if (args.length == 1) {

				UUID uuid = UUIDFetcher.getUUID(args[0]);

				if (uuid == null) {
					sender.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
					return true;
				}
				String playername = UUIDFetcher.getName(uuid);
				if(!BanManager.isBanned(uuid)) {
					sender.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.failed").replace("%player%", playername)));
					return true;
				}
				for (Player online : Bukkit.getOnlinePlayers()) {
					if (online.hasPermission(permission)) {
						online.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.usage")
								.replace("%sender%", name).replace("%player%", playername)));
					}
				}

				Bukkit.getConsoleSender().sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.usage")
						.replace("%sender%", name).replace("%player%", playername)));
				BanManager.unban(uuid);
				return true;

			}
		}
		return false;
	}
}
