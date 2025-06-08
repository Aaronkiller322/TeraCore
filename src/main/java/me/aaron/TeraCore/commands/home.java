package me.aaron.TeraCore.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class home implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	public home() {
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
			if (command.getName().equalsIgnoreCase("home")) {

				if (args.length == 0) {
					ArrayList<String> homes = new HomeManager(player.getUniqueId()).getHomes();
					if (homes.size() < 1) {
						String empty = PlaceHolder.replacePlaceholder(config.getString("command.list.empty"));
						player.sendMessage(empty);
						return true;
					}
					String start = PlaceHolder.replacePlaceholder(config.getString("command.list.message"));
					String split = PlaceHolder.replacePlaceholder(config.getString("command.list.split"));
					StringBuilder result = new StringBuilder(start);

					for (int i = 0; i < homes.size(); i++) {
						result.append(homes.get(i));
						if (i < homes.size() - 1) {
							result.append(split);
						}
					}

					String finalResult = result.toString();
					player.sendMessage(finalResult);
					return true;
				}
				if (args.length == 1) {
					String home = args[0].toLowerCase();
					HomeManager manager = new HomeManager(player.getUniqueId());
					if (!manager.existHome(home)) {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(config.getString("command.list.failed").replace("%home%", home)));
						return true;
					}
					Location location = manager.getHome(home);

					player.teleport(location);
					player.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.list.teleport").replace("%home%", home)));
					return true;
				}
			}
			if (command.getName().equalsIgnoreCase("sethome")) {
				if (!player.hasPermission(config.getString("command.sethome.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 0) {
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.sethome.help")));
					return true;
				}
				if (args.length == 1) {
					HomeManager manager = new HomeManager(player.getUniqueId());
					if (maxHomesEnabled()) {
						if (ignoreMaxHomesEnabled()) {
							if (!hasIgnorePermission(player)) {
								int max = getMaxHomes(player);
								int exist = manager.getHomes().size();
								if (max <= exist) {
									player.sendMessage(PlaceHolder
											.replacePlaceholder(config.getString("command.sethome.maxhome.message")
													.replace("%amount%", String.valueOf(max))));
									return true;
								}
							}
						} else {
							int max = getMaxHomes(player);
							int exist = manager.getHomes().size();
							if (max <= exist) {
								player.sendMessage(PlaceHolder
										.replacePlaceholder(config.getString("command.sethome.maxhome.message")
												.replace("%amount%", String.valueOf(max))));
								return true;
							}

						}
					}
					String home = args[0].toLowerCase();

					if (manager.existHome(home)) {
						player.sendMessage(PlaceHolder.replacePlaceholder(
								config.getString("command.sethome.failed").replace("%home%", home)));
						return true;
					}
					manager.setHome(home, player.getLocation());
					player.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.sethome.message").replace("%home%", home)));
					return true;
				}
			}
			if (command.getName().equalsIgnoreCase("delhome")) {
				if (!player.hasPermission(config.getString("command.delhome.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 0) {
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.delhome.help")));
					return true;
				}
				if (args.length == 1) {
					String home = args[0].toLowerCase();
					HomeManager manager = new HomeManager(player.getUniqueId());
					if (!manager.existHome(home)) {
						player.sendMessage(PlaceHolder.replacePlaceholder(
								config.getString("command.delhome.failed").replace("%home%", home)));
						return true;
					}
					manager.delHome(home);
					player.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.delhome.message").replace("%home%", home)));
					return true;
				}
			}
			if (command.getName().equalsIgnoreCase("movehome")) {
				if (!player.hasPermission(config.getString("command.movehome.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 0) {
					player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.movehome.help")));
					return true;
				}
				if (args.length == 1) {
					String home = args[0].toLowerCase();
					HomeManager manager = new HomeManager(player.getUniqueId());
					if (!manager.existHome(home)) {
						player.sendMessage(PlaceHolder.replacePlaceholder(
								config.getString("command.movehome.failed").replace("%home%", home)));
						return true;
					}
					manager.setHome(home, player.getLocation());
					player.sendMessage(PlaceHolder
							.replacePlaceholder(config.getString("command.movehome.message").replace("%home%", home)));
					return true;
				}
			}
		}
		return false;
	}

	public boolean maxHomesEnabled() {
		boolean enabled = true;
		try {
			enabled = config.getBoolean("command.sethome.maxhome.enabled");
		} catch (Exception e) {
		}
		return enabled;
	}

	public boolean ignoreMaxHomesEnabled() {
		boolean enabled = true;
		try {
			enabled = config.getBoolean("command.sethome.maxhome.admin.ignore_limit");
		} catch (Exception e) {
		}
		return enabled;
	}

	public boolean hasIgnorePermission(Player player) {
		String perm = config.getString("command.sethome.maxhome.admin.permission");
		return player.hasPermission(perm);
	}

	public int getMaxHomes(Player player) {
		String perm = config.getString("command.sethome.maxhome.permission");
		int max = config.getInt("command.sethome.maxhome.max_home");
		for (int i = max; i > 0; i--) {
			if (player.hasPermission(perm.replace("%amount%", String.valueOf(i)))) {
				return i;
			}
		}
		return 0;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (command.getName().equalsIgnoreCase("home") || command.getName().equalsIgnoreCase("delhome")
					|| command.getName().equalsIgnoreCase("movehome")) {
				try {
					tab.addAll(getTabHomes(player, command, args, command.getName().toLowerCase()));

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return tab;
	}


	public List<String> getTabHomes(Player player,Command command, String[] args, String commands) {
		List<String> tab = new ArrayList<>();
		try {
			if (!player.hasPermission(config.getString("command." + commands + ".permission"))) {
				player.sendMessage(PlaceHolder
						.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
				return tab;
			}
		}catch (Exception ex){

		}
		if (args.length == 1) {
			List<String> end = new ArrayList<>();

			HomeManager manager = new HomeManager(player.getUniqueId());
			for (String home : manager.getHomes()) {
				tab.add(home);
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

		return tab;
	}
}