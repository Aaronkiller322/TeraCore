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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.economy.Eco_Manager;
import me.aaron.TeraCore.economy.MySQLDatabase;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.UUIDFetcher;

public class money implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	public money() {
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

			if (command.getName().equalsIgnoreCase("money")) {
				if (args.length == 0) {
					Eco_Manager vault = new Eco_Manager(player.getUniqueId());
					double money = vault.getMoney();
					String text = PlaceHolder.replacePlaceholder(config.getString("command.money.usage"));
					player.sendMessage(text.replace("%money%", vault.roundMoney(money)));
					return true;
				}
				if (args.length == 1) {
					Eco_Manager vault = new Eco_Manager(UUIDFetcher.getUUID(args[0].toLowerCase()));
					if (!vault.hasAccount()) {
						sender.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
						return true;
					}
					
					
					double money = vault.getMoney();
					String text = PlaceHolder.replacePlaceholder(config.getString("command.money.other"));
					player.sendMessage(text.replace("%money%", vault.roundMoney(money)).replace("%trust%", UUIDFetcher.getName(UUIDFetcher.getUUID(args[0]))));
					return true;
				}
				String text = PlaceHolder.replacePlaceholder(config.getString("command.money.help"));
				player.sendMessage(text);
				return true;
			}
			if (command.getName().equalsIgnoreCase("setmoney")) {
				if (!player.hasPermission(config.getString("command.setmoney.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 2) {
					Eco_Manager vault = new Eco_Manager(UUIDFetcher.getUUID(args[0].toLowerCase()));
					if (!vault.hasAccount()) {
						sender.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
						return true;
					}
					try {
						double amaout = Double.valueOf(args[1]);
						String username;
						Player trust = Bukkit.getPlayer(args[0]);
						if (trust != null) {
							username = trust.getName();
						} else {
							username = UUIDFetcher.getName(UUIDFetcher.getUUID(args[0]));
						}
						vault.setMoney(amaout);
						String text = PlaceHolder.replacePlaceholder(config.getString("command.setmoney.usage"));
						String trust_text = PlaceHolder.replacePlaceholder(config.getString("command.setmoney.trust"));
						player.sendMessage(
								text.replace("%trust%", username).replace("%money%", vault.roundMoney(amaout)));
						if (trust != null) {
							trust.sendMessage(trust_text.replace("%trust%", player.getName())
									.replace("%money%", vault.roundMoney(amaout)));
						}
						return true;
					} catch (Exception e) {
					}
				}
				String text = PlaceHolder.replacePlaceholder(config.getString("command.setmoney.help"));
				player.sendMessage(text);
				return true;

			}
			if (command.getName().equalsIgnoreCase("addmoney")) {
				if (!player.hasPermission(config.getString("command.addmoney.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 2) {
					Eco_Manager vault = new Eco_Manager(UUIDFetcher.getUUID(args[0].toLowerCase()));
					if (!vault.hasAccount()) {
						sender.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
						return true;
					}
					try {
						double amaout = Double.valueOf(args[1]);
						String username;
						Player trust = Bukkit.getPlayer(args[0]);
						if (trust != null) {
							username = trust.getName();
						} else {
							username = UUIDFetcher.getName(UUIDFetcher.getUUID(args[0]));
						}
						vault.addMoney(amaout);
						String text = PlaceHolder.replacePlaceholder(config.getString("command.addmoney.usage"));
						String trust_text = PlaceHolder.replacePlaceholder(config.getString("command.addmoney.trust"));
						player.sendMessage(
								text.replace("%trust%", username).replace("%money%", vault.roundMoney(amaout)));
						if (trust != null) {
							trust.sendMessage(trust_text.replace("%trust%", player.getName())
									.replace("%money%", vault.roundMoney(amaout)));
						}
						return true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				String text = PlaceHolder.replacePlaceholder(config.getString("command.addmoney.help"));
				player.sendMessage(text);
				return true;
			}
			if (command.getName().equalsIgnoreCase("removemoney")) {
				if (!player.hasPermission(config.getString("command.removemoney.permission"))) {
					player.sendMessage(PlaceHolder
							.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
					return true;
				}
				if (args.length == 2) {
					Eco_Manager vault = new Eco_Manager(UUIDFetcher.getUUID(args[0].toLowerCase()));

					if (!vault.hasAccount()) {
						sender.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
						return true;
					}
					try {
						double amaout = Double.valueOf(args[1]);
						String username;
						Player trust = Bukkit.getPlayer(args[0]);
						if (trust != null) {
							username = trust.getName();
						} else {
							username = UUIDFetcher.getName(UUIDFetcher.getUUID(args[0]));
						}
						vault.addMoney(amaout);
						String text = PlaceHolder.replacePlaceholder(config.getString("command.removemoney.usage"));
						String trust_text = PlaceHolder
								.replacePlaceholder(config.getString("command.removemoney.trust"));
						player.sendMessage(
								text.replace("%trust%", username).replace("%money%", vault.roundMoney(amaout)));
						if (trust != null) {
							trust.sendMessage(trust_text.replace("%trust%", player.getName())
									.replace("%money%", vault.roundMoney(amaout)));
						}
						return true;
					} catch (Exception e) {
					}
				}
				String text = PlaceHolder.replacePlaceholder(config.getString("command.removemoney.help"));
				player.sendMessage(text);
				return true;
			}
			if (command.getName().equalsIgnoreCase("pay")) {
				if (args.length == 2) {
					Eco_Manager trust_vault = new Eco_Manager(UUIDFetcher.getUUID(args[0].toLowerCase()));
					Eco_Manager user_vault = new Eco_Manager(player.getUniqueId());

					if (!trust_vault.hasAccount()) {
						sender.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.player_not_found")));
						return true;
					}
					try {
						double amaout = Double.valueOf(args[1]);
						String username;
						Player trust = Bukkit.getPlayer(args[0]);
						if (trust != null) {
							username = trust.getName();
						} else {
							username = UUIDFetcher.getName(UUIDFetcher.getUUID(args[0]));
						}
						if (user_vault.getMoney() < amaout) {
							String text = PlaceHolder.replacePlaceholder(config.getString("default.empty"));
							player.sendMessage(text);
							return true;
						}

						trust_vault.addMoney(amaout);
						user_vault.removeMoney(amaout);

						String text = PlaceHolder.replacePlaceholder(config.getString("command.pay.usage"));
						String trust_text = PlaceHolder.replacePlaceholder(config.getString("command.pay.trust"));
						player.sendMessage(
								text.replace("%trust%", username).replace("%money%", user_vault.roundMoney(amaout)));
						if (trust != null) {
							trust.sendMessage(trust_text.replace("%trust%", player.getName())
									.replace("%money%", user_vault.roundMoney(amaout)));
						}
						return true;
					} catch (Exception e) {
					}
				}
				String text = PlaceHolder.replacePlaceholder(config.getString("command.pay.help"));
				player.sendMessage(text);
				return true;
			}
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> tab = new ArrayList<>();
		if (sender instanceof Player) {
			Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("money") || command.getName().equalsIgnoreCase("setmoney")
				|| command.getName().equalsIgnoreCase("addmoney") || command.getName().equalsIgnoreCase("removemoney")
				|| command.getName().equalsIgnoreCase("pay")) {
			try {
				if (args.length == 1) {
					List<String> end = new ArrayList<>();
					tab = Eco_Manager.getAllPlayers();
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
					if (command.getName().equalsIgnoreCase("setmoney") || command.getName().equalsIgnoreCase("addmoney")
							|| command.getName().equalsIgnoreCase("removemoney")
							|| command.getName().equalsIgnoreCase("pay")) {
						List<String> end = new ArrayList<>();
						end.add("Betrag");
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
