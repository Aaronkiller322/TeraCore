package me.aaron.TeraCore.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.server.ServerListPingEvent;

import me.aaron.TeraCore.Component.TextComponent;
import me.aaron.TeraCore.util.MotdManager;

public class MotdEvent implements Listener {

@EventHandler
public void serverping(ServerListPingEvent event) {
	try {
	MotdManager motd = new MotdManager();
	event.setMotd(motd.getLine1() + "\n" + motd.getLine2());
	}catch (Exception e) {
	}
}

@EventHandler
	public void interact(PlayerInteractAtEntityEvent event){
	Entity entity = event.getRightClicked();
	
}

}
