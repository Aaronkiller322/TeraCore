package me.aaron.TeraCore.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.aaron.TeraCore.main.LanguageLoader;
import me.aaron.TeraCore.util.chat.TeraHoverText;
import me.aaron.TeraCore.util.chat.TeraText;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

public class seed implements CommandExecutor {

	File file;
	FileConfiguration config;

	public seed() {
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
			if (command.getName().equalsIgnoreCase("seed")) {
				
				if (player.hasPermission(config.getString("command.permission"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					if (args.length == 0) {
						String seedString = String.valueOf(player.getWorld().getSeed());
						String configString = config.getString("command.usage.message");
						String[] split = configString.split("%seed%", -1);
						TeraText teraText = new TeraText(PlaceHolder.replacePlaceholder(split[0]));

						TeraHoverText seed = new TeraHoverText(PlaceHolder.replacePlaceholder(config.getString("command.usage.seed_color"))
								+ seedString);
						seed.setHoverText(PlaceHolder.replacePlaceholder(config.getString("command.usage.hovertext")));
						seed.setCopyText(seedString);
						teraText.addHoverText(seed);

						if (split.length > 1) {
							teraText.addText(PlaceHolder.replacePlaceholder(split[1]));
						}

						teraText.sendMessage(player);
						return true;
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


}
