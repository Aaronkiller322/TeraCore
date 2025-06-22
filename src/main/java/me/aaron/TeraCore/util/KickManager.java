package me.aaron.TeraCore.util;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.commands.kick;
import me.aaron.TeraCore.main.TeraMain;

public class KickManager {

	

	
	public static void KickPlayer(Player player, CommandSender sender, String reason) {
		File file = new File("plugins/" + TeraMain.getPlugin().getName(), "/lang_" + TeraMain.getLanguage()+ "/commands/kick.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		
		ArrayList<String> kick_message = (ArrayList<String>) config.getList("kick.message");
		String no_reason = config.getString("kick.no_reason");
		String name = "Console";
		if(sender instanceof Player) {
			name = ((Player) sender).getName();
		}
		
		StringBuilder result = new StringBuilder();

		for (String s : kick_message) {
		    result.append(s).append("\n");
		}

		String finalResult = result.toString();
		
		String grund;
		if(reason != null) {
			grund = reason;
		}else {
			grund = no_reason;
		}
		
		player.kickPlayer(PlaceHolder.replacePlaceholder(finalResult.replace("%sender%", name).replace("%reason%", grund)));
		
	}
	
}
