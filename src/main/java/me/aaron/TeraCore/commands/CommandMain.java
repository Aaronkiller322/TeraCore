package me.aaron.TeraCore.commands;

import me.aaron.TeraCore.economy.Eco_Config;
import org.bukkit.command.CommandExecutor;

import me.aaron.TeraCore.main.TeraMain;

public class CommandMain {

	public static String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang_" + TeraMain.getLanguage() +"/commands";

	public static void loadCommands() {
		TeraMain.getPlugin().getCommand("gamemode").setExecutor((CommandExecutor) new gamemode());
		TeraMain.getPlugin().getCommand("gamemode").setTabCompleter(new gamemode());
		
		TeraMain.getPlugin().getCommand("fly").setExecutor((CommandExecutor) new fly());
		
		TeraMain.getPlugin().getCommand("time").setExecutor((CommandExecutor) new time());
		TeraMain.getPlugin().getCommand("time").setTabCompleter(new time());
		
		TeraMain.getPlugin().getCommand("day").setExecutor((CommandExecutor) new time());
		TeraMain.getPlugin().getCommand("night").setExecutor((CommandExecutor) new time());
		
		TeraMain.getPlugin().getCommand("weather").setExecutor((CommandExecutor) new weather());
		TeraMain.getPlugin().getCommand("weather").setTabCompleter(new weather());
		
		TeraMain.getPlugin().getCommand("sun").setExecutor((CommandExecutor) new weather());
		TeraMain.getPlugin().getCommand("rain").setExecutor((CommandExecutor) new weather());
		TeraMain.getPlugin().getCommand("thunder").setExecutor((CommandExecutor) new weather());
		
		TeraMain.getPlugin().getCommand("teleport").setExecutor((CommandExecutor) new teleport());
		TeraMain.getPlugin().getCommand("teleport").setTabCompleter(new teleport());
		
		TeraMain.getPlugin().getCommand("tphere").setExecutor((CommandExecutor)new tphere());
		TeraMain.getPlugin().getCommand("tphere").setTabCompleter(new tphere());
		
		TeraMain.getPlugin().getCommand("speed").setExecutor((CommandExecutor) new speed());
		TeraMain.getPlugin().getCommand("speed").setTabCompleter(new speed());
		
		TeraMain.getPlugin().getCommand("eat").setExecutor((CommandExecutor) new eat());
		TeraMain.getPlugin().getCommand("eat").setTabCompleter(new eat());
		
		TeraMain.getPlugin().getCommand("heal").setExecutor((CommandExecutor) new heal());
		TeraMain.getPlugin().getCommand("heal").setTabCompleter(new heal());
		
		TeraMain.getPlugin().getCommand("money").setExecutor((CommandExecutor) new money());
		TeraMain.getPlugin().getCommand("money").setTabCompleter(new money());
		if(new Eco_Config().enabled()) {
			TeraMain.getPlugin().getCommand("setmoney").setExecutor((CommandExecutor) new money());
			TeraMain.getPlugin().getCommand("setmoney").setTabCompleter(new money());

			TeraMain.getPlugin().getCommand("addmoney").setExecutor((CommandExecutor) new money());
			TeraMain.getPlugin().getCommand("addmoney").setTabCompleter(new money());

			TeraMain.getPlugin().getCommand("removemoney").setExecutor((CommandExecutor) new money());
			TeraMain.getPlugin().getCommand("removemoney").setTabCompleter(new money());

			TeraMain.getPlugin().getCommand("pay").setExecutor((CommandExecutor) new money());
			TeraMain.getPlugin().getCommand("pay").setTabCompleter(new money());
		}
		TeraMain.getPlugin().getCommand("skull").setExecutor((CommandExecutor) new skull());
		
		
		TeraMain.getPlugin().getCommand("motd").setExecutor((CommandExecutor) new motd());
		TeraMain.getPlugin().getCommand("motd").setTabCompleter(new motd());
		
		TeraMain.getPlugin().getCommand("home").setExecutor((CommandExecutor) new home());
		TeraMain.getPlugin().getCommand("home").setTabCompleter(new home());
		
		TeraMain.getPlugin().getCommand("delhome").setExecutor((CommandExecutor) new home());
		TeraMain.getPlugin().getCommand("delhome").setTabCompleter(new home());
		
		TeraMain.getPlugin().getCommand("sethome").setExecutor((CommandExecutor) new home());
		TeraMain.getPlugin().getCommand("sethome").setTabCompleter(new home());

		TeraMain.getPlugin().getCommand("movehome").setExecutor((CommandExecutor) new home());
		TeraMain.getPlugin().getCommand("movehome").setTabCompleter(new home());
		
		
		TeraMain.getPlugin().getCommand("warp").setExecutor((CommandExecutor) new warp());
		TeraMain.getPlugin().getCommand("warp").setTabCompleter(new warp());
		
		TeraMain.getPlugin().getCommand("delwarp").setExecutor((CommandExecutor) new warp());
		TeraMain.getPlugin().getCommand("delwarp").setTabCompleter(new warp());
		
		TeraMain.getPlugin().getCommand("setwarp").setExecutor((CommandExecutor) new warp());
		TeraMain.getPlugin().getCommand("setwarp").setTabCompleter(new warp());
		
		TeraMain.getPlugin().getCommand("kick").setExecutor((CommandExecutor) new kick());
		TeraMain.getPlugin().getCommand("kick").setTabCompleter(new kick());
		
		TeraMain.getPlugin().getCommand("ban").setExecutor((CommandExecutor) new ban());
		TeraMain.getPlugin().getCommand("ban").setTabCompleter(new ban());
		
		TeraMain.getPlugin().getCommand("unban").setExecutor((CommandExecutor) new unban());
		
		TeraMain.getPlugin().getCommand("tempban").setExecutor((CommandExecutor) new tempban());
		TeraMain.getPlugin().getCommand("tempban").setTabCompleter(new tempban());

		TeraMain.getPlugin().getCommand("seed").setExecutor((CommandExecutor) new seed());

		TeraMain.getPlugin().getCommand("back").setExecutor((CommandExecutor) new back());

		TeraMain.getPlugin().getCommand("tpa").setExecutor((CommandExecutor) new teleportask());
		TeraMain.getPlugin().getCommand("tpahere").setExecutor((CommandExecutor) new teleportask());
		TeraMain.getPlugin().getCommand("tpaccept").setExecutor((CommandExecutor) new teleportask());
		TeraMain.getPlugin().getCommand("tpadeny").setExecutor((CommandExecutor) new teleportask());
		
	}
	
}
