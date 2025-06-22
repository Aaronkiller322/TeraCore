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
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getConsoleSender().sendMessage("§cTeraCore deaktiviert: Vault nicht gefunden.");
            Bukkit.getPluginManager().disablePlugin(TeraMain.getPlugin());
            return;
        }

        EconomyImplementer implementer = new EconomyImplementer();
        Bukkit.getServicesManager().register(Economy.class, implementer, TeraMain.getPlugin(), ServicePriority.Normal);
        econ = implementer;

        Bukkit.getConsoleSender().sendMessage("§aTeraCore Economy erfolgreich als Vault-Provider registriert.");
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
