package me.aaron.TeraCore.events;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.DefaultConfig;
import me.aaron.TeraCore.main.TeraMain;

public class TeleportManager implements Listener {

	File file;
	FileConfiguration config;

	private String datafolder = "plugins/" + TeraMain.getPlugin().getName() + "/lang";

	private HashMap<Player, Location> wait = new HashMap<Player, Location>();
	private HashMap<Player, Integer> time = new HashMap<Player, Integer>();
	private HashMap<Player, Location> move = new HashMap<Player, Location>();
	private HashMap<Player, Long> cooldown = new HashMap<Player, Long>();
	private ArrayList<Player> allowed = new ArrayList<Player>();

	public TeleportManager() {
		String filetype = getClass().getSimpleName();
		File temp = new File(datafolder, filetype + ".yml");
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

	}

	@EventHandler
	public void move(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (move.containsKey(player)) {
			Location loc = move.get(player);
			if (loc.getX() != player.getLocation().getX() || loc.getY() != player.getLocation().getY()
					|| loc.getZ() != player.getLocation().getZ()
					|| !loc.getWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
				resetPlayerState(player);
				player.sendMessage(PlaceHolder.replacePlaceholder(config.getString("teleport.delay.chat.failed")));
			}
		}

	}

	private void resetPlayerState(Player player) {
		if (time.containsKey(player)) {
			time.remove(player);
		}
		if (wait.containsKey(player)) {
			wait.remove(player);
		}
		if (allowed.contains(player)) {
			allowed.remove(player);
		}
		if (move.containsKey(player)) {
			move.remove(player);
		}
	}

	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		double secound = config.getDouble("teleport.delay.join-cooldown-second");
		long time = (long) (System.currentTimeMillis() + (secound * 1000));
		cooldown.put(player, time);

	}

	public boolean isBypass(Player player) {
		boolean bypass = config.getBoolean("teleport.delay.bypass.enabled");
		if (bypass) {
			String perm = config.getString("teleport.delay.bypass.permission");
			if (player.hasPermission(perm)
					|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
				return true;
			}
		}

		return false;
	}

	public boolean blacklist() {
		return config.getBoolean("teleport.black-list.enabled");
	}

	public boolean blacklist_effect(Player player, String worldname) {
		if (blacklist()) {
			boolean gamemode = config.getBoolean("teleport.black-list.gamemode.enabled");
			if (gamemode) {
				ArrayList<String> list = (ArrayList<String>) config.getList("teleport.black-list.gamemode.list");

				if (list.contains(player.getGameMode().name())) {
					return true;
				}
			}
			boolean world = config.getBoolean("teleport.black-list.world.enabled");
			if (world) {
				ArrayList<String> list = (ArrayList<String>) config.getList("teleport.black-list.world.list");

				if (list.contains(worldname)) {
					return true;
				}
			}
			boolean permission = config.getBoolean("teleport.black-list.permission.enabled");
			if (permission) {
				String perm = config.getString("teleport.black-list.permission.permission");
				if (player.hasPermission(perm)
						|| player.hasPermission(DefaultConfig.getConfig().getString("admin_permission"))) {
					return true;
				}
			}

		}
		return false;
	}

	@EventHandler
	public void teleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();

		if(!event.getCause().equals(PlayerTeleportEvent.TeleportCause.COMMAND) &&(!event.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN))){
			return;
		}

		ArrayList<Location> loc = new ArrayList<>();
		if(TeraMain.back_location.containsKey(player)){
			loc = TeraMain.back_location.get(player);
		}
		loc.add(event.getFrom());
		TeraMain.back_location.put(player, loc);

		if(blacklist_effect(player, event.getTo().getWorld().getName())) return;




		if (isBypass(player)) {
			
			showParticleCircle(event.getTo(), player);
			
			return;
		}
		if (!event.isCancelled()) {
			if (cooldown.containsKey(player)) {
				if (cooldown.get(player) > System.currentTimeMillis()) {
					return;
				}
			}
			boolean delay = config.getBoolean("teleport.delay.enabled");
			if (delay) {
				if (!allowed.contains(player)) {
					Integer secound = config.getInt("teleport.delay.seconds");
					if (time.containsKey(player)) {
						resetPlayerState(player);
						player.sendMessage(
								PlaceHolder.replacePlaceholder(config.getString("teleport.delay.chat.failed")));
						event.setCancelled(true);
						return;
					}
					move.put(player, player.getLocation());
					time.put(player, secound);
					wait.put(player, event.getTo());
					event.setCancelled(true);
					new BukkitRunnable() {
						int counter = 0;
						final int maxRuns = secound;
						int amount = secound;

						@Override
						public void run() {
							if (!move.containsKey(player)) {
								this.cancel();
								return;
							}

							boolean chat = config.getBoolean("teleport.delay.chat.enabled");
							int sek = amount - counter;
							if (chat) {
								if (sek > 0) {
									player.sendMessage(PlaceHolder
											.replacePlaceholder(config.getString("teleport.delay.chat.message")
													.replace("%seconds%", String.valueOf(sek))));
								}
							}

							playSound(player, player.getLocation(), "teleport.delay.sound");
							boolean particle = config.getBoolean("teleport.delay.particle.enabled");
							if (particle) {
								showParticleCircleDelay(player.getLocation(), player, maxRuns, counter, sek);
							}
							counter++;
							if (counter > maxRuns) {
								allowed.add(player);
								event.setCancelled(false);
								player.teleport(wait.get(player));
								player.sendMessage(PlaceHolder
										.replacePlaceholder(config.getString("teleport.delay.chat.successful")));
								this.cancel();
								return;
							}
						}
					}.runTaskTimer(TeraMain.getPlugin(), 0, 20);
					event.setCancelled(true);
					return;
				}
			}
			resetPlayerState(player);
			playSound(player, event.getTo(), "teleport.sound");
			showParticleCircle(event.getTo(), player);

		}
	}

	private void playSound(Player player, Location location, String configPrefix) {
		try {
			if (config.getBoolean(configPrefix + ".enabled")) {
				Sound sound = Sound.valueOf(config.getString(configPrefix + ".type").toUpperCase());
				float pitch = (float) config.getDouble(configPrefix + ".pitch");
				float volume = (float) config.getDouble(configPrefix + ".volume");
				
				//player.getWorld().playSound(location, sound, pitch, volume);
				
				if(config.getBoolean("teleport.see-and-hear.online_player")) {
					for(Player online : Bukkit.getOnlinePlayers()) {
						if(!online.getName().equalsIgnoreCase(player.getName())) {
						online.playSound(location, sound, volume, pitch);
						}
					}
				
				}
				if(config.getBoolean("teleport.see-and-hear.sender")) {
					player.playSound(location, sound, volume, pitch);
				}
				
				
			}
		} catch (Exception e) {
		}
	}

	public Color getColorFromString(String colorName) {
		try {
			for (Field field : Color.class.getDeclaredFields()) {
				if (field.getType() == Color.class && java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					if (field.getName().equalsIgnoreCase(colorName)) {
						return (Color) field.get(null);
					}
				}
			}
		} catch (Exception e) {
		}
		return Color.WHITE;
	}

	public void showParticleCircle(Location location, Player player) {
		if (!config.getBoolean("teleport.particle.enabled"))
			return;

		final double radius = config.getDouble("teleport.particle.radius");
		final String colorName = config.getString("teleport.particle.color");
		final double size = config.getDouble("teleport.particle.size");
		final DustOptions dustOptions = new DustOptions(getColorFromString(colorName), (float) size);
		final double height = config.getDouble("teleport.particle.height");
		final double spread = config.getDouble("teleport.particle.spread");

		new BukkitRunnable() {
			double angle = 0;

			@Override
			public void run() {
				for (double y = 0; y < height; y += spread) {
					for (int i = 0; i < 32; i++) {
						double x = radius * Math.cos(angle);
						double z = radius * Math.sin(angle);
						Location particleLocation = location.clone().add(x, y, z);
						
						
						if(config.getBoolean("teleport.see-and-hear.online_player")) {
							for(Player online : Bukkit.getOnlinePlayers()) {
								if(!online.getName().equalsIgnoreCase(player.getName())) {
									online.spawnParticle(Particle.REDSTONE, particleLocation, 1, dustOptions);
								}
							}
						
						}
						if(config.getBoolean("teleport.see-and-hear.sender")) {
							player.spawnParticle(Particle.REDSTONE, particleLocation, 1, dustOptions);
						}
						angle += Math.PI / 16;
					}
				}
				this.cancel();
			}
		}.runTask(TeraMain.getPlugin());
	}

	public void showParticleCircleDelay(Location location, Player player, int seconds, int count, int run) {
		if (!config.getBoolean("teleport.delay.particle.enabled"))
			return;

		new BukkitRunnable() {
			double angle = 0;
			final double radius = config.getDouble("teleport.delay.particle.radius");
			final String color = config.getString("teleport.delay.particle.color");
			final double size = config.getDouble("teleport.delay.particle.size");
			final DustOptions dustOptions = new DustOptions(getColorFromString(color), (float) size);
			final double height = config.getDouble("teleport.delay.particle.height");
			final double yIncrement = height / seconds * count;
			int ticksPassed = 0;

			@Override
			public void run() {
				if (ticksPassed >= run) {
					this.cancel();
					return;
				}

				for (double y = 0; y <= yIncrement; y += height / seconds) {
					for (int i = 0; i < 32; i++) {
						double x = radius * Math.cos(angle);
						double z = radius * Math.sin(angle);
						Location particleLocation = location.clone().add(x, y, z);

						
						if(config.getBoolean("teleport.see-and-hear.online_player")) {
							for(Player online : Bukkit.getOnlinePlayers()) {
								if(!online.getName().equalsIgnoreCase(player.getName())) {
									online.spawnParticle(Particle.REDSTONE, particleLocation, 1, dustOptions);
								}
							}
						
						}
						if(config.getBoolean("teleport.see-and-hear.sender")) {
							player.spawnParticle(Particle.REDSTONE, particleLocation, 1, dustOptions);
						}

						angle += Math.PI / 16;
					}
				}

				ticksPassed += 20;
			}
		}.runTaskTimer(TeraMain.getPlugin(), 0, 20);
	}

}