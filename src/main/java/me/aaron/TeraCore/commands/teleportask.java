package me.aaron.TeraCore.commands;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.events.TeleportManager;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.LanguageLoader;
import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.UserData;
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
import java.util.UUID;

public class teleportask implements CommandExecutor {

	File file;
	FileConfiguration config;

	private static HashMap<UUID, ArrayList<UUID>> tpa = new HashMap<>();
	private static HashMap<UUID, ArrayList<UUID>> tpahere = new HashMap<>();

	public teleportask() {
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
			if (command.getName().equalsIgnoreCase("tpa") ||
					command.getName().equalsIgnoreCase("tpahere") ||
					command.getName().equalsIgnoreCase("tpaccept") ||
					command.getName().equalsIgnoreCase("tpacancel") ||
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
						if(trust.getUniqueId().equals(player.getUniqueId())){
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.cancel.self")));
							return true;
						}
						if(command.getName().equalsIgnoreCase("tpacancel")){
							if(checkAndRemove(player.getUniqueId(), trust.getUniqueId())){
								trust.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.cancel.trust").replace("%player%", player.getName())));
								player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.cancel.player")));
								return true;
							}else{
								player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.cancel.failed")));
								return true;
							}
						}
						if(command.getName().equalsIgnoreCase("tpa") || command.getName().equalsIgnoreCase("tpahere")){
							ArrayList<UUID> list = new ArrayList<>();

							UUID uuid = trust.getUniqueId();
							UserData userData;
							if(TeraMain.userDataHashMap.containsKey(uuid)){
								userData = TeraMain.userDataHashMap.get(uuid);
							}else {
								userData = new UserData(uuid);
							}

							try{
								boolean status = (boolean) userData.getData(UserData.FilePath.TPATOGGLE);
								if(!status){
									player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.blocked")));
									return true;
								}

							}catch (Exception ex){
								userData.setData(UserData.FilePath.TPATOGGLE, true);
							}

							if(command.getName().equalsIgnoreCase("tpahere")) {
								if (tpahere.containsKey(trust.getUniqueId())) {
									list = tpahere.get(trust.getUniqueId());
								}
							}
							if(command.getName().equalsIgnoreCase("tpa")) {
								if (tpa.containsKey(trust.getUniqueId())) {
									list = tpa.get(trust.getUniqueId());
								}
							}
							if(!list.contains(player.getUniqueId())){
								list.add(player.getUniqueId());

								TeleportManager.getInstance().playSound(trust.getLocation(), trust, "command.args1.setting.sound", false, config);
								TeleportManager.getInstance().showParticleCircle(trust.getLocation(), trust, "command.args1.setting.particle", false, config);

								if(command.getName().equalsIgnoreCase("tpa")) {
									tpa.put(trust.getUniqueId(), list);
								}
								if(command.getName().equalsIgnoreCase("tpahere")) {
									tpahere.put(trust.getUniqueId(), list);
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
								if(config.getBoolean("command.args1.timer.enabled")){
									int second = config.getInt("command.args1.timer.second");
									String message_trust = config.getString("command.args1.timer.message.trust");
									String message_player = config.getString("command.args1.timer.message.player");

									player.sendMessage(PlaceHolder.replacePlaceholder(message_trust.replace("%time%", String.valueOf(second))));
									trust.sendMessage(PlaceHolder.replacePlaceholder(message_player.replace("%time%", String.valueOf(second))));


									Bukkit.getScheduler().runTaskLater(TeraMain.getPlugin(), new Runnable() {

										UUID trustuuid = trust.getUniqueId();
										UUID plaeruuid = player.getUniqueId();

										@Override
										public void run() {
											if(checkAndRemove(plaeruuid, trustuuid)){
												if(player != null){
													player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.timer.message.finish")));
												}
												if(trust != null){
													trust.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.timer.message.finish")));
												}
											}
										}
									},20 * second);
								}
								return true;

							}else{
								player.sendMessage(PlaceHolder.replacePlaceholder(
										config.getString("command.args1.failed")));
							}
						}
						if(command.getName().equalsIgnoreCase("tpaccept")){
							if(tpa.containsKey(player.getUniqueId())){
								if(tpa.get(player.getUniqueId()).contains(trust.getUniqueId())) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.trust")));
									trust.teleport(player);
									ArrayList<UUID> array = tpa.get(player.getUniqueId());
									array.remove(trust.getUniqueId());
									tpa.put(player.getUniqueId(), array);
									return true;
								}
							}
							if(tpahere.containsKey(player.getUniqueId())){
								if(tpahere.get(player.getUniqueId()).contains(trust.getUniqueId())) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.accept.trust")));
									player.teleport(trust);
									ArrayList<UUID> array = tpahere.get(player.getUniqueId());
									array.remove(trust.getUniqueId());
									tpahere.put(player.getUniqueId(), array);
									return true;
								}
							}
							player.sendMessage(PlaceHolder.replacePlaceholder(
									config.getString("command.args1.empty").replace("%trust_name%", trust.getName())));
							return true;
						}

						if(command.getName().equalsIgnoreCase("tpadeny")){
							if(tpa.containsKey(player.getUniqueId())){
								if(tpa.get(player.getUniqueId()).contains(trust.getUniqueId())) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.trust")));
									ArrayList<UUID> array = tpa.get(player.getUniqueId());
									array.remove(trust.getUniqueId());
									tpa.put(player.getUniqueId(), array);
									return true;
								}
							}
							if(tpahere.containsKey(player.getUniqueId())){
								if(tpahere.get(player.getUniqueId()).contains(trust.getUniqueId())) {
									trust.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.player")));
									player.sendMessage(PlaceHolder.replacePlaceholder(
											config.getString("command.args1.trust.hover.deny.trust")));
									ArrayList<UUID> array = tpahere.get(player.getUniqueId());
									array.remove(trust.getUniqueId());
									tpahere.put(player.getUniqueId(), array);
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

	public boolean checkAndRemove(UUID playerUUID, UUID trustUUUID){
		if(tpa.containsKey(trustUUUID)){
			ArrayList<UUID> array = tpa.get(trustUUUID);
			if(array.contains(playerUUID)){
				array.remove(playerUUID);
				tpa.put(trustUUUID, array);
				return true;
			}
		}
		if(tpahere.containsKey(trustUUUID)){
			ArrayList<UUID> array = tpahere.get(trustUUUID);
			if(array.contains(playerUUID)){
				array.remove(playerUUID);
				tpahere.put(trustUUUID, array);
				return true;
			}
		}
		return false;
	}

}
