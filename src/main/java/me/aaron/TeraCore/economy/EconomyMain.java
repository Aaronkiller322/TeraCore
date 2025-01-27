package me.aaron.TeraCore.economy;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.TeraMain;
import net.milkbowl.vault.economy.Economy;

public class EconomyMain {
    private static Economy econ = null;

    public static void enable() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        if (setupEconomy()) {
            Bukkit.getServicesManager().register(Economy.class, new EconomyImplementer(), TeraMain.getPlugin(), ServicePriority.Normal);
            Bukkit.getConsoleSender().sendMessage("TeraCore Economy erfolgreich initialisiert.");
        } else {
            Bukkit.getConsoleSender().sendMessage("§cTeraCore Deaktiviert: Vault nicht gefunden oder nicht richtig konfiguriert.");
            pm.disablePlugin(TeraMain.getPlugin());
        }
    }

    private static boolean setupEconomy() {
        if (TeraMain.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = TeraMain.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
