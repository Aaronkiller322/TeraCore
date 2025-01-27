package me.aaron.TeraCore.economy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

public class Eco_Manager {
	private static Eco_Config eco_conf = new Eco_Config();
	private UUID uuid;
	public Eco_Manager(UUID uuid) {
		this.uuid = uuid;
	}
	
	public static boolean isMysql() {
		if (eco_conf.config.getString("economy.storage-method").equalsIgnoreCase("MYSQL")) {
			return true;
		}
		return false;
	}
	
	public void setMoney(double money){
		if(isMysql()) {
			V2_TeraVault_MYSQL vault = new V2_TeraVault_MYSQL(uuid);
			vault.setMoney(money);
		}else {
			V2_TeraVault_LOCAL vault = new V2_TeraVault_LOCAL(uuid);
			vault.setMoney(money);
		}
	}
	
	public void addMoney(double money){
		if(isMysql()) {
			V2_TeraVault_MYSQL vault = new V2_TeraVault_MYSQL(uuid);
			vault.addMoney(money);
		}else {
			V2_TeraVault_LOCAL vault = new V2_TeraVault_LOCAL(uuid);
			vault.addMoney(money);
		}
	}
	
	public void removeMoney(double money){
		if(isMysql()) {
			V2_TeraVault_MYSQL vault = new V2_TeraVault_MYSQL(uuid);
			vault.removeMoney(money);
		}else {
			V2_TeraVault_LOCAL vault = new V2_TeraVault_LOCAL(uuid);
			vault.removeMoney(money);
		}
	}
	
	public double getMoney(){
		if(isMysql()) {
			V2_TeraVault_MYSQL vault = new V2_TeraVault_MYSQL(uuid);
			return vault.getMoney();
		}else {
			V2_TeraVault_LOCAL vault = new V2_TeraVault_LOCAL(uuid);
			return vault.getMoney();
		}
	}
    public boolean hasAccount() {
    	if(isMysql()) {
    		V2_TeraVault_MYSQL vault = new V2_TeraVault_MYSQL(uuid);
    		return vault.hasAccount();
    	}else {
    		V2_TeraVault_LOCAL vault = new V2_TeraVault_LOCAL(uuid);
    		return vault.hasAccount();
    	}
    }

    public void createAccount() {
        if (!hasAccount()) {
        	setMoney(0);
        }
    }
    
    public static String roundMoney(double amount) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2); // Max 2 decimal places
        return n.format(amount);
    }
    public static ArrayList<String> getAllPlayers() {
    	if(isMysql()) {
    		return V2_TeraVault_MYSQL.getAllPlayers();
    	}else {
    	return V2_TeraVault_LOCAL.getAllPlayers();
    	}
    }
}
