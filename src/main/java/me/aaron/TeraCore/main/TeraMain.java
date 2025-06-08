package me.aaron.TeraCore.main;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
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
import me.aaron.TeraCore.events.CommandBlockEvent;
import me.aaron.TeraCore.events.EventMain;
import me.aaron.TeraCore.util.KickManager;
import me.aaron.TeraCore.util.MotdManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import me.aaron.TeraCore.economy.EconomyImplementer;



public class TeraMain extends JavaPlugin implements Listener {

	public String prefix;
	public String name;
	// String setmoney = "";


	@Override
	public void onEnable() {
	    try {
	        singleton = this;
	        // Initialisiere wichtige Ressourcen
	        PrefixManager.loadPrefix();
	        CommandBlockEvent.loadconfig();
	        MotdManager.loadConfig();

	        // Datenbankverbindung herstellen (falls MySQL aktiviert ist)
	        if (Eco_Manager.isMysql()) {
	            MySQLDatabase.connect();
	        }

	        // Economy registrieren
	        Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), this, ServicePriority.Normal);

	        // Weitere Initialisierungen
	        EconomyMain.enable();
	        DefaultConfig.LoadDefaultConfig();
	        CommandMain.loadCommands();
	        EventMain.loadEvents();

	        // PlaceholderAPI-Unterstützung registrieren
	        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
	            new TeraHolder().register();
	        }

	        getLogger().info("TeraCore has been successfully enabled!");
	    } catch (Exception e) {
	        getLogger().severe("Failed to enable TeraCore!");
	        e.printStackTrace();
	        getServer().getPluginManager().disablePlugin(this);
	    }
	}
	public void onDisable() {
		Eco_Config eco_conf = new Eco_Config();
		if(eco_conf.config.getString("economy.storage-method").equalsIgnoreCase("MYSQL")) {
		MySQLDatabase.disconnect();
		}
	}
	private static TeraMain singleton;

	public static TeraMain getPlugin() {
		return singleton;
	}
}
