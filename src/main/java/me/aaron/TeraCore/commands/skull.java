package me.aaron.TeraCore.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.SkinFetcher;
import me.aaron.TeraCore.util.UUIDFetcher;

public class skull implements CommandExecutor {

	File file;
	FileConfiguration config;

	public skull() {
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
			if (command.getName().equalsIgnoreCase("skull")) {
				if (player.hasPermission(config.getString("command.args1.permission"))
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					if (args.length == 1) {
						try {
							player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.args1.usage").replace("%trust%", args[0])));
							
							player.getInventory().addItem(SkinFetcher.getSkull(args[0]));
							
							return true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("command.help")));
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
