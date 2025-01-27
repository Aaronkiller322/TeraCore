package me.aaron.TeraCore.events;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;

import me.aaron.TeraCore.main.TeraMain;

public class EventMain {

	public static void loadEvents() {
		final PluginManager mp = Bukkit.getServer().getPluginManager();
		mp.registerEvents(new join_quit_event(), TeraMain.getPlugin());
		mp.registerEvents(new CommandBlockEvent(), TeraMain.getPlugin());
		mp.registerEvents(new MotdEvent(), TeraMain.getPlugin());
		mp.registerEvents(new TeleportManager(), TeraMain.getPlugin());
	}
	
}
