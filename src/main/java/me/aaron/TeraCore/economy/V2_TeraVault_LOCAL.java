package me.aaron.TeraCore.economy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;

import me.aaron.TeraCore.util.UUIDFetcher;


public class V2_TeraVault_LOCAL {

    private static UUID uuid;
    private static FileConfiguration config;

    public V2_TeraVault_LOCAL(UUID uuid) {
        this.uuid = uuid;
        this.config = MoneyConfig.getConfig(); // Assuming TeraCore is your main plugin class
    }

    private String getConfigPath() {
        return "money." + uuid.toString();
    }

    public boolean hasAccount() {
        return config.contains(getConfigPath());
    }

    public void createAccount() {
        if (!hasAccount()) {
            config.set(getConfigPath(), 0.0);
            saveConfig();
        }
    }

    public double getMoney() {
        if (!hasAccount()) {
            createAccount();
        }
        return config.getDouble(getConfigPath(), 0.0);
    }

    public void removeMoney(double amount) {
        double currentMoney = getMoney();
        double newAmount = Math.max(currentMoney - amount, 0.0);
        config.set(getConfigPath(), newAmount);
        saveConfig();
    }

    public void addMoney(double amount) {
        double currentMoney = getMoney();
        config.set(getConfigPath(), currentMoney + amount);
        saveConfig();
    }

    public void setMoney(double amount) {
        if (!hasAccount()) {
            createAccount();
        }
        config.set(getConfigPath(), amount);
        saveConfig();
    }

    private void saveConfig() {
        try {
            MoneyConfig.saveConfig(); // Save the plugin's config
        } catch (Exception e) {
        }
    }

    public static String roundMoney(double amount) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2); // Max 2 decimal places
        return n.format(amount);
    }
    public static ArrayList<String> getAllPlayers() {
    	ArrayList<String> list = new ArrayList<String>();
    	
    	
    	for(String uuids : config.getConfigurationSection("money").getKeys(false)) {
    		UUID uuid = UUID.fromString(uuids);
    		if(uuid != null) {
    			list.add(UUIDFetcher.getName(uuid));
    		}
    	}
    	return list;
    }
}
