package me.aaron.TeraCore.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.GameEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.economy.Eco_Config;
import me.aaron.TeraCore.economy.Eco_Manager;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.BanManager;

public class join_quit_event implements Listener {

	File file;
	FileConfiguration config;

	public join_quit_event() {
		String filetype = getClass().getSimpleName();
		LanguageLoader.load(LanguageLoader.LanguageFolder.events, filetype);
		File temp = new File(EventMain.datafolder, filetype + ".yml");
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

	Eco_Config eco_conf = new Eco_Config();
	@EventHandler
	public void ban(AsyncPlayerPreLoginEvent event) {
		UUID uuid = event.getUniqueId();
		if(BanManager.isBanned(uuid)) {
			event.disallow(Result.KICK_OTHER, BanManager.getBanMessage(uuid));
		}
	}
	@EventHandler
	public void onjoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Eco_Manager manager = new Eco_Manager(player.getUniqueId());
		if(!manager.hasAccount()) {
			manager.createAccount();
			manager.setMoney(1000);
		}
		if (config.getBoolean("join.enable")) {
			String message = config.getString("join.message").replace("%player%", player.getDisplayName());
			event.setJoinMessage(PlaceHolder.replacePlaceholder(message));
		}
	}

	@EventHandler
	public void onquit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (config.getBoolean("quit.enable")) {
			String message = config.getString("quit.message").replace("%player%", player.getDisplayName());
			event.setQuitMessage(PlaceHolder.replacePlaceholder(message));
		}
	}
}
