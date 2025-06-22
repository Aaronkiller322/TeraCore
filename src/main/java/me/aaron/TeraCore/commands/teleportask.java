package me.aaron.TeraCore.commands;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.chat.TeraHoverText;
import me.aaron.TeraCore.util.chat.TeraText;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class teleportask implements CommandExecutor {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang/commands";

	private static HashMap<Player, ArrayList<Player>> tpa = new HashMap<>();
	private static HashMap<Player, ArrayList<Player>> tpahere = new HashMap<>();

	public teleportask() {
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
			if (command.getName().equalsIgnoreCase("tpa") ||
					command.getName().equalsIgnoreCase("tpahere") ||
					command.getName().equalsIgnoreCase("tpaccept") ||
					command.getName().equalsIgnoreCase("tpadeny")) {
				if (args.length == 1) {
					if (player
							.hasPermission(config.getString("command.args1.permission." + command.getName().toLowerCase()))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						Player trust = Bukkit.getPlayer(args[0]);
						if (trust == null) {
							player.sendMessage(PlaceHolder.replacePlaceholder(
									DefaultConfig.getConfig().getString("message.player_not_found")));
							return true;
						}

						if(command.getName().equalsIgnoreCase("tpa") || command.getName().equalsIgnoreCase("tpahere")){
							ArrayList<Player> list = new ArrayList<>();


							if(command.getName().equalsIgnoreCase("tpahere")) {
								if (tpahere.containsKey(trust)) {
									list = tpahere.get(trust);
								}
							}
							if(command.getName().equalsIgnoreCase("tpa")) {
								if (tpa.containsKey(trust)) {
									list = tpa.get(trust);
								}
							}
							if(!list.contains(player)){
								list.add(player);
								if(command.getName().equalsIgnoreCase("tpa")) {
									tpa.put(trust, list);
								}
								if(command.getName().equalsIgnoreCase("tpahere")) {
									tpahere.put(trust, list);
								}
								player.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args1.quest").replace("%command%", command.getName().toLowerCase())));

								ArrayList<String> text = (ArrayList<String>) config.getList("command.args1.trust.text");

								for(String string : text) {
									string = string.replace("%command%", command.getName().toLowerCase());
									string = string.replace("%trust_name%", player.getName());

									String[] split_accept = string.split("%accept%");

									TeraText teraText = new TeraText(PlaceHolder.replacePlaceholder(split_accept[0]));
									if(split_accept.length > 1){
										String[] split_deny = split_accept[1].split("%deny%");
										if(split_deny.length > 1){

											TeraHoverText hoverText_accept = new TeraHoverText(PlaceHolder.replacePlaceholder(
													config.getString("command.args1.trust.hover.accept.text")));
											hoverText_accept.setHoverText(PlaceHolder.replacePlaceholder(
													config.getString("command.args1.trust.hover.accept.hover")));
											hoverText_accept.setCommandToRun(
													config.getString("command.args1.trust.hover.accept.click").replace("%trust_name%", player.getName()));
											teraText.addHoverText(hoverText_accept);

											teraText.addText(PlaceHolder.replacePlaceholder(split_deny[0]));

											TeraHoverText hoverText_deny = new TeraHoverText(PlaceHolder.replacePlaceholder(
													config.getString("command.args1.trust.hover.deny.text")));
											hoverText_deny.setHoverText(PlaceHolder.replacePlaceholder(
													config.getString("command.args1.trust.hover.deny.hover")));
											hoverText_deny.setCommandToRun(
													config.getString("command.args1.trust.hover.deny.click").replace("%trust_name%", player.getName()));
											teraText.addHoverText(hoverText_deny);

											teraText.addText(PlaceHolder.replacePlaceholder(split_deny[1]));
										}
									}

									teraText.sendMessage(trust);
								}
							}else{
								player.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args1.failed")));
							}
						}
						if(command.getName().equalsIgnoreCase("tpaccept")){
							if(tpa.containsKey(player)){
								if(tpa.get(player).contains(trust)) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.trust")));
									trust.teleport(player);
									ArrayList<Player> array = tpa.get(player);
									array.remove(trust);
									tpa.put(player, array);
									return true;
								}
							}
							if(tpahere.containsKey(player)){
								if(tpahere.get(player).contains(trust)) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.trust")));
									player.teleport(trust);
									ArrayList<Player> array = tpahere.get(player);
									array.remove(trust);
									tpahere.put(player, array);
									return true;
								}
							}
							player.sendMessage(PlaceHolder.replacePlaceholder(
									config.getString("command.args1.empty").replace("%trust_name%", trust.getName())));
							return true;
						}

						if(command.getName().equalsIgnoreCase("tpadeny")){
							if(tpa.containsKey(player)){
								if(tpa.get(player).contains(trust)) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.trust")));
									ArrayList<Player> array = tpa.get(player);
									array.remove(trust);
									tpa.put(player, array);
									return true;
								}
							}
							if(tpahere.containsKey(player)){
								if(tpahere.get(player).contains(trust)) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.trust")));
									ArrayList<Player> array = tpahere.get(player);
									array.remove(trust);
									tpahere.put(player, array);
									return true;
								}
							}
							player.sendMessage(PlaceHolder.replacePlaceholder(
									config.getString("command.args1.empty").replace("%trust_name%", trust.getName())));
							return true;
						}

						return true;
					} else {
						player.sendMessage(PlaceHolder
								.replacePlaceholder(DefaultConfig.getConfig().getString("message.no_permission")));
						return true;
					}
				}


				player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help").replace("%command%", command.getName().toLowerCase())));
				return true;
			}
		}
		return false;
	}


}
