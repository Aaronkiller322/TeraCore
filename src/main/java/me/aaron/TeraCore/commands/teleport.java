package me.aaron.TeraCore.commands;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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

public class teleport implements CommandExecutor, TabCompleter {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	public teleport() {
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
			if (command.getName().equalsIgnoreCase("teleport")) {
				if (args.length == 1) {
					if (player
							.hasPermission(config.getString("command.args1.permission").replace("%type%",
									config.getString("placeholder.type.player")))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						Player trust = Bukkit.getPlayer(args[0]);
						if (trust == null) {
							player.sendMessage(PlaceHolder.replacePlaceholder(
									DefaultConfig.getConfig().getString("message.player_not_found")));
							return true;
						}
						player.teleport(trust);
						player.sendMessage(PlaceHolder.replacePlaceholder(
								config.getString("command.args1.usage").replace("%location%", trust.getDisplayName())));
						return true;
					} else {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
						return true;
					}
				}
				if (args.length == 2) {
					if (player
							.hasPermission(config.getString("command.args2.permission").replace("%type%",
									config.getString("placeholder.type.player")))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						Player trust = Bukkit.getPlayer(args[0]);
						if(args[0].equalsIgnoreCase("@s")){
							trust = player;
						}
						if (trust == null) {
							player.sendMessage(PlaceHolder.replacePlaceholder(
									DefaultConfig.getConfig().getString("message.player_not_found")));
							return true;
						}
						Player trust_other = Bukkit.getPlayer(args[1]);
						if (trust_other == null) {
							player.sendMessage(PlaceHolder.replacePlaceholder(
									DefaultConfig.getConfig().getString("message.player_not_found")));
							return true;
						}
						trust.teleport(trust_other);
						trust.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
								.replace("%location%", trust_other.getDisplayName())));
						player.sendMessage(PlaceHolder.replacePlaceholder(
								config.getString("command.args2.usage").replace("%location%", player.getDisplayName())
										.replace("%player%", trust.getDisplayName())));
						return true;
					} else {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
						return true;
					}
				}
				if (args.length == 3) {
					if (player
							.hasPermission(config.getString("command.args1.permission").replace("%type%",
									config.getString("placeholder.type.player")))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						try {
							double x = Double.valueOf(args[0].replace(",", "."));
							double y = Double.valueOf(args[1].replace(",", "."));
							double z = Double.valueOf(args[2].replace(",", "."));
							//tp 29999999 100 29999999
							if(y < -64) {
								y = -64;
							}
							if(y > 128) {
								y = 128;
							}
							
							if(x > 29999999) {
								x = 29999999;
							}
							if(z > 29999999) {
								z = 29999999;
							}
							
							if(x < -29999999) {
								x = -29999999;
							}
							if(z < -29999999) {
								z = -29999999;
							}
							
							Location loc = new Location(player.getWorld(), x, y, z);
							loc.setDirection(player.getLocation().getDirection());
							loc.setPitch(player.getLocation().getPitch());
							player.teleport(loc);
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
									.replace("%location%", getformat(x) + ", " + getformat(y) + ", " + getformat(z))));
							return true;
							
						} catch (Exception e) {
						}
						
					} else {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
						return true;

					}
				}
				if (args.length == 4) {
					if (player
							.hasPermission(config.getString("command.args1.permission").replace("%type%",
									config.getString("placeholder.type.player")))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						try {
							double x = Double.valueOf(args[1].replace(",", "."));
							double y = Double.valueOf(args[2].replace(",", "."));
							double z = Double.valueOf(args[3].replace(",", "."));

							if(y < -64) {
								y = -64;
							}
							if(y > 128) {
								y = 128;
							}
							
							if(x > 29999999) {
								x = 29999999;
							}
							if(z > 29999999) {
								z = 29999999;
							}
							
							if(x < -29999999) {
								x = -29999999;
							}
							if(z < -29999999) {
								z = -29999999;
							}
							
							Location loc = new Location(player.getWorld(), x, y, z);
							Player trust = Bukkit.getPlayer(args[0]);
							if(args[0].equalsIgnoreCase("@s")){
								trust = player;
							}
							if (trust == null) {
								player.sendMessage(PlaceHolder.replacePlaceholder(
										DefaultConfig.getConfig().getString("message.player_not_found")));
								return true;
							}
							
							loc.setDirection(trust.getLocation().getDirection());
							loc.setPitch(trust.getLocation().getPitch());
							
							trust.teleport(loc);
							trust.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage")
									.replace("%location%", getformat(x) + ", " + getformat(y) + ", " + getformat(z))));
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args2.usage")
									.replace("%location%", getformat(x) + ", " + getformat(y) + ", " + getformat(z))
									.replace("%player%", trust.getDisplayName())));
							return true;
						} catch (Exception e) {
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
		if (command.getName().equalsIgnoreCase("teleport")) {
			try {
			if (player.hasPermission(config.getString("command.args1.permission").replace(".%type%", ""))
					|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
				if (args.length == 1) {
					List<String> end = new ArrayList<>();
					
					
					tab.add(getformat(player.getLocation().getX()));
					for(Player online : Bukkit.getOnlinePlayers()) {
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
				if (args.length == 2) {
					List<String> end = new ArrayList<>();
					if(isNummer(args[0])) {
					tab.add(getformat(player.getLocation().getY()));
					}else {
						tab.add(getformat(player.getLocation().getX()));	
					}
					for(Player online : Bukkit.getOnlinePlayers()) {
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
				if (args.length == 3) {
					List<String> end = new ArrayList<>();
					
					if(isNummer(args[0]) && isNummer(args[1])) {
					tab.add(getformat(player.getLocation().getZ()));
					}else if (!isNummer(args[0]) && isNummer(args[1])) {
						tab.add(getformat(player.getLocation().getY()));	
					}
					
					for(Player online : Bukkit.getOnlinePlayers()) {
						tab.add(online.getDisplayName());
					}
					for (int i = 0; i < tab.size(); i++) {
						if (args[2] != null) {
							if (tab.get(i).toLowerCase().startsWith(args[2].toLowerCase())) {
								end.add(tab.get(i));
							}
						}
					}
					return end;
				}
				if (args.length == 4) {
					List<String> end = new ArrayList<>();
					
					if (!isNummer(args[0]) && isNummer(args[1]) && isNummer(args[2])) {
						tab.add(getformat(player.getLocation().getZ()));	
					}
					
					for (int i = 0; i < tab.size(); i++) {
						if (args[3] != null) {
							if (tab.get(i).toLowerCase().startsWith(args[3].toLowerCase())) {
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
	
	private String getformat(double wert) {
        DecimalFormat df = new DecimalFormat("##.###");
        return df.format(wert);
	}
	
	private boolean isNummer(String string) {
		
		try {
			double nummer = Double.valueOf(string.replace(",", "."));
		}catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
	
}
