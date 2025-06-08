package me.aaron.TeraCore.color;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceHolder {

	public static String replacePlaceholder(String message) {
		String color = HexColorTextGenerator.convertHexColorToChatColor(message);
        return PlaceholderAPI.setPlaceholders(null, color);
	}
	public static String replacePlaceholder_Player(Player player, String message) {
		String color = HexColorTextGenerator.convertHexColorToChatColor(message);
        return PlaceholderAPI.setPlaceholders(player, color);
	}
	
}
