package me.aaron.TeraCore.economy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;


public class V2_TeraVault_MYSQL {

private UUID uuid;
	
	public V2_TeraVault_MYSQL (UUID uuid) {
		this.uuid = uuid;
	}

    public boolean hasAccount() {
        return MySQLDatabase.exists(uuid);
    }

    public void createAccount() {
        if (!hasAccount()) {
        	MySQLDatabase.setMoney(uuid, 0);
        }
    }

    public double getMoney() {
        if (!hasAccount()) {
            createAccount();
        }
        return MySQLDatabase.getMoney(uuid);
    }

    public void removeMoney(double amount) {
        MySQLDatabase.removeMoney(uuid, amount);
    }

    public void addMoney(double amount) {
    	MySQLDatabase.addMoney(uuid, amount);
    }

    public void setMoney(double amount) {
        if (!hasAccount()) {
            createAccount();
        }
        MySQLDatabase.setMoney(uuid, amount);
    }

    public static String roundMoney(double amount) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2); // Max 2 decimal places
        return n.format(amount);
    }
    
    public static ArrayList<String> getAllPlayers() {
    	return MySQLDatabase.getAllPlayers();
    }
}
