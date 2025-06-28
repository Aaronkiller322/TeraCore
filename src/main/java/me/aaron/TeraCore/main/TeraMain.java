package me.aaron.TeraCore.main;

import me.aaron.TeraCore.events.*;
import me.aaron.TeraCore.util.UserData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.aaron.TeraCore.color.PrefixManager;
import me.aaron.TeraCore.color.TeraHolder;
import me.aaron.TeraCore.commands.CommandMain;
import me.aaron.TeraCore.economy.Eco_Config;
import me.aaron.TeraCore.economy.Eco_Manager;
import me.aaron.TeraCore.economy.EconomyImplementer;
import me.aaron.TeraCore.economy.EconomyMain;
import me.aaron.TeraCore.economy.MySQLDatabase;
import me.aaron.TeraCore.util.KickManager;
import me.aaron.TeraCore.util.MotdManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import me.aaron.TeraCore.economy.EconomyImplementer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


public class TeraMain extends JavaPlugin implements Listener {

	public String prefix;
	public String name;
	// String setmoney = "";

	public static HashMap<UUID, ArrayList<Location>> back_location = new HashMap<>();

	public static HashMap<UUID, UserData> userDataHashMap = new HashMap<>();

	@Override
	public void onEnable() {
		saveDefaultConfig();
		try {
			singleton = this;
			for(Player online : Bukkit.getOnlinePlayers()){
				userDataHashMap.put(online.getUniqueId(), new UserData(online.getUniqueId()));
			}
			// Initialisiere wichtige Ressourcen
			PrefixManager.loadPrefix();
			MotdManager.loadConfig();

			// Datenbankverbindung herstellen (falls MySQL aktiviert ist)
			if (Eco_Manager.isMysql()) {
				MySQLDatabase.connect();
			}

			if (new Eco_Config().enabled()) {
				Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), this, ServicePriority.Normal);
				EconomyMain.enable();
			}
			DefaultConfig.LoadDefaultConfig();
			CommandMain.loadCommands();
			EventMain.loadEvents();

			// PlaceholderAPI-Unterstützung registrieren


			Bukkit.getScheduler().runTaskLater(this, () -> {
				if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
					new TeraHolder().register();
					Bukkit.getConsoleSender().sendMessage("§aTeraCore Placeholder registriert.");
				} else {
					Bukkit.getConsoleSender().sendMessage("§cPlaceholderAPI nicht gefunden.");

				}
			}, 20L); // 1 Sekunde Verzögerung (20 Ticks)
		}catch (Exception ex){}

	}

	public static String getLanguage(){
		return TeraMain.getPlugin().getConfig().getString("language");
	}

	public void onDisable() {
		Eco_Config eco_conf = new Eco_Config();
		if(eco_conf.config.getString("economy.storage-method").equalsIgnoreCase("MYSQL")) {
		MySQLDatabase.disconnect();
		}

		StateFix.onDisable();

	}
	private static TeraMain singleton;

	public static TeraMain getPlugin() {
		return singleton;
	}
}
