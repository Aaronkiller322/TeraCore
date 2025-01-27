package me.aaron.TeraCore.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import me.aaron.TeraCore.util.MotdManager;

public class motd implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	public motd() {
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
			if (command.getName().equalsIgnoreCase("motd")) {
				
				if (player.hasPermission(config.getString("command.args1.permission"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					if (args.length == 2) {
						try {
						int nummer = Integer.valueOf(args[0]);
						String text = args[1];
						if(nummer == 1 || nummer == 2) {
							player.sendMessage("nummer: " + nummer);
							player.sendMessage("text: " + text);
						if(nummer == 1) {
							MotdManager.setLine1(text);
						}
						if(nummer == 2) {
							MotdManager.setLine2(text);
						}
						player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage").replace("%line%", args[0])));

						return true;
						}
						}catch (Exception e) {
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

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("motd")) {
			try {
			if (player.hasPermission(config.getString("command.args1.permission"))
					|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
				if (args.length == 1) {
					List<String> end = new ArrayList<>();

					tab.add("1");
					tab.add("2");
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

					tab.add("<Text>");
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
