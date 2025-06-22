package me.aaron.TeraCore.economy;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.aaron.TeraCore.util.UUIDFetcher;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyImplementer implements Economy {

    private static final String SUCCESS_MESSAGE = "Funktion ausgeführt";
    
    @Override
    public EconomyResponse bankBalance(String bankName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse bankDeposit(String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse bankHas(String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse bankWithdraw(String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse createBank(String bankName, String username) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse createBank(String bankName, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public boolean createPlayerAccount(String username) {
		Eco_Manager manager = new Eco_Manager(UUIDFetcher.getUUID(username));
		if(!manager.hasAccount()) {
			manager.createAccount();
		}
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		Eco_Manager manager = new Eco_Manager(offlinePlayer.getUniqueId());
		if(!manager.hasAccount()) {
			manager.createAccount();
		}
        return true;
    }

    @Override
    public boolean createPlayerAccount(String username, String bankName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String bankName) {
        return false;
    }

    @Override
    public String currencyNamePlural() {
        return "$";
    }

    @Override
    public String currencyNameSingular() {
        return "Teri";
    }

    @Override
    public EconomyResponse deleteBank(String bankName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse depositPlayer(String username, double amount) {
		Eco_Manager manager = new Eco_Manager(UUIDFetcher.getUUID(username));
			manager.addMoney(amount);
        return new EconomyResponse(amount, manager.getMoney(), EconomyResponse.ResponseType.SUCCESS, SUCCESS_MESSAGE);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
    	Eco_Manager manager = new Eco_Manager(offlinePlayer.getUniqueId());
    	manager.addMoney(amount);
        return new EconomyResponse(amount, manager.getMoney(), EconomyResponse.ResponseType.SUCCESS, SUCCESS_MESSAGE);
    }

    @Override
    public EconomyResponse depositPlayer(String username, String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public String format(double amount) {
        return Eco_Manager.roundMoney(amount);
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public double getBalance(String username) {
    	Eco_Manager manager = new Eco_Manager(UUIDFetcher.getUUID(username));
        return manager.getMoney();
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
    	Eco_Manager manager = new Eco_Manager(offlinePlayer.getUniqueId());
        return manager.getMoney();
    }

    @Override
    public double getBalance(String username, String bankName) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String bankName) {
        return 0;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public String getName() {
        return "TeraCore";
    }

    @Override
    public boolean has(String username, double amount) {
    	Eco_Manager manager = new Eco_Manager(UUIDFetcher.getUUID(username));
        return manager.getMoney() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
    	Eco_Manager manager = new Eco_Manager(offlinePlayer.getUniqueId());
        return manager.getMoney() >= amount;
    }

    @Override
    public boolean has(String username, String bankName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String bankName, double amount) {
        return false;
    }

    @Override
    public boolean hasAccount(String username) {
    	Eco_Manager manager = new Eco_Manager(UUIDFetcher.getUUID(username));
        return manager.hasAccount();
    	
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
    	Eco_Manager manager = new Eco_Manager(offlinePlayer.getUniqueId());
        return manager.hasAccount();
    }

    @Override
    public boolean hasAccount(String username, String bankName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String bankName) {
        return false;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse isBankMember(String bankName, String username) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse isBankMember(String bankName, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, String username) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String bankName, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public EconomyResponse withdrawPlayer(String username, double amount) {
    	Eco_Manager manager = new Eco_Manager(UUIDFetcher.getUUID(username));
        if (manager.getMoney() < amount) {
            return new EconomyResponse(0, manager.getMoney(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
        manager.removeMoney(amount);
        return new EconomyResponse(amount, manager.getMoney(), EconomyResponse.ResponseType.SUCCESS, SUCCESS_MESSAGE);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
    	Eco_Manager manager = new Eco_Manager(offlinePlayer.getUniqueId());
        if (manager.getMoney() < amount) {
            return new EconomyResponse(0, manager.getMoney(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }
        manager.removeMoney(amount);
        return new EconomyResponse(amount, manager.getMoney(), EconomyResponse.ResponseType.SUCCESS, SUCCESS_MESSAGE);
    }

    @Override
    public EconomyResponse withdrawPlayer(String username, String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String bankName, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Banking is not supported");
    }
}
