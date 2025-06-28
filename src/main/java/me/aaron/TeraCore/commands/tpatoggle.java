package me.aaron.TeraCore.commands;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.LanguageLoader;
import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.UserData;
import me.aaron.TeraCore.util.chat.TeraHoverText;
import me.aaron.TeraCore.util.chat.TeraText;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class tpatoggle implements CommandExecutor {

	File file;
	FileConfiguration config;

	public tpatoggle() {
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
			if (command.getName().equalsIgnoreCase("tpatoggle")) {
				if (args.length == 0) {
					if (player.hasPermission(config.getString("command.args0.permission"))
							|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
						UUID uuid = player.getUniqueId();
						UserData userData;
						if(TeraMain.userDataHashMap.containsKey(uuid)){
							userData = TeraMain.userDataHashMap.get(uuid);
						}else {
							userData = new UserData(uuid);
						}

						try{
							boolean status = true;
							try {
								status = (boolean) userData.getData(UserData.FilePath.TPATOGGLE);
							}catch (Exception ex){
								status = true;
							}
							if(status){
								player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage").replace("%status%", config.getString("command.placeholder.disabled"))));
								userData.setData(UserData.FilePath.TPATOGGLE, false);
								return true;
							}
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args0.usage").replace("%status%", config.getString("command.placeholder.enabled"))));
							userData.setData(UserData.FilePath.TPATOGGLE, true);
							return true;

						}catch (Exception ex){
							userData.setData(UserData.FilePath.TPATOGGLE, true);
						}
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
