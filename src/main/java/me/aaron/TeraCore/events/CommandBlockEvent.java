package me.aaron.TeraCore.events;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.UUID;

import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.LanguageLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.TeraMain;

public class CommandBlockEvent implements Listener {

	private static HashMap<UUID, Long> wait = new HashMap<UUID, Long>();
	private static HashMap<UUID, String> command = new HashMap<UUID, String>();

	//private static String block = "%teracore_prefix% &4🚫 &cDu bist zu schnell! Bitte warte &e%time% Sekunden!";
	
	private static String message;
	private static boolean enabled;
	private static Integer secound;
	private static String permission;
	private static boolean bypass;


	File file;
	FileConfiguration config;

	public CommandBlockEvent() {
		String filetype = "cooldown-settings";
		LanguageLoader.load(LanguageLoader.LanguageFolder.events, filetype);
		File temp = new File(EventMain.datafolder, filetype + ".yml");
		if (temp.exists()) {
			file = temp;
			config = (FileConfiguration) YamlConfiguration.loadConfiguration(file);
		} else {
			config = ConfigLoader.getConfig(filetype);
			file = temp;
			try {
				config.save(temp);
			} catch (IOException e) {
			}
		}
		loadconfig();
	}

	public void loadconfig() {
		message = config.getString("cooldown.message");
		enabled = config.getBoolean("cooldown.enabled");
		secound = config.getInt("cooldown.second");
		bypass = config.getBoolean("cooldown.bypass.enabled");
		permission = config.getString("cooldown.bypass.permission");
		
	}
	
	@EventHandler
	public void command(PlayerCommandPreprocessEvent event) {
		if(enabled) {
		Player player = event.getPlayer();
		if(bypass) {
			if(player.hasPermission(permission)) {
				return;
			}
		}
		UUID uuid = player.getUniqueId();
		String cmd = event.getMessage();

		if (wait.containsKey(uuid)) {
			if (command.containsKey(uuid)) {
				long time = wait.get(uuid);
				String sek = round(((time - System.currentTimeMillis()) / 1000)+ 1);
				if (cmd.equalsIgnoreCase(command.get(uuid))) {
					if (time > System.currentTimeMillis()) {
						player.sendMessage(PlaceHolder.replacePlaceholder(message.replace("%time%", sek)));
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		wait.put(uuid, System.currentTimeMillis() + 1000 * secound);
		command.put(uuid, cmd);
		}
	}
    public static String round(double amount) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(0); // Max 2 decimal places
        return n.format(amount);
    }
}
