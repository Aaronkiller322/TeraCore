package me.aaron.TeraCore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.aaron.TeraCore.color.PlaceHolder;
import me.aaron.TeraCore.main.TeraMain;

public class BanManager {

    private static File banFile = new File("plugins/" + TeraMain.getPlugin().getName() + "/data", "bans.yml");
    private static FileConfiguration banConfig = YamlConfiguration.loadConfiguration(banFile);

    public static boolean isBanned(UUID uuid) {
    	try {
        long time = banConfig.getLong("bans." + uuid.toString() + ".time");
        if (time == -1 || time > System.currentTimeMillis()) {
            return true;
        }
    	}catch (Exception e) {
		}
        return false;
    }

    public static void unban(UUID uuid) {
        if (isBanned(uuid)) {
            banConfig.set("bans." + uuid.toString(), null);
            saveConfig();
        }
    }

    public static String getBanMessage(UUID uuid) {
        if (isBanned(uuid)) {
            File file = new File("plugins/" + TeraMain.getPlugin().getName(), "/lang/commands/ban.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            String reason = banConfig.getString("bans." + uuid.toString() + ".grund", config.getString("ban.no_reason"));
            String sender = banConfig.getString("bans." + uuid.toString() + ".sender", "Console");
            long time = banConfig.getLong("bans." + uuid.toString() + ".time", -1);
            String timeString = (time == -1) ? config.getString("ban.permaban") : getTime(time - System.currentTimeMillis());

            String finalResult = buildBanMessage((ArrayList<String>) config.getStringList("ban.message"), sender, reason, timeString);
            return PlaceHolder.replacePlaceholder(finalResult);
        }
        return null;
    }

    public static void banPlayer(UUID uuid, CommandSender sender, String reason) {
        banPlayer(uuid, sender, reason, -1); // Permaban mit -1 Zeit
    }

    public static void banPlayer(UUID uuid, CommandSender sender, String reason, long time) {
        File file = new File("plugins/" + TeraMain.getPlugin().getName(), "/lang/commands/ban.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String name = (sender instanceof Player) ? ((Player) sender).getName() : "Console";
        String banReason = (reason != null) ? reason : config.getString("ban.no_reason");

        banConfig.set("bans." + uuid.toString() + ".grund", banReason);
        banConfig.set("bans." + uuid.toString() + ".sender", name);
        banConfig.set("bans." + uuid.toString() + ".time", (time == -1) ? -1 : time + System.currentTimeMillis());
        saveConfig();

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            String timeString = (time == -1) ? config.getString("ban.permaban") : getTime(time);
            String finalResult = buildBanMessage((ArrayList<String>) config.getStringList("ban.message"), name, banReason, timeString);
            player.kickPlayer(PlaceHolder.replacePlaceholder(finalResult));
        }
    }

    private static String buildBanMessage(ArrayList<String> banMessage, String sender, String reason, String timeString) {
        StringBuilder result = new StringBuilder();
        for (String line : banMessage) {
            result.append(line.replace("%sender%", sender).replace("%reason%", reason).replace("%time%", timeString)).append("\n");
        }
        return result.toString().trim();
    }

    private static String getTime(long time) {
        time = time / 1000; // Millisekunden in Sekunden umrechnen

        File file = new File("plugins/" + TeraMain.getPlugin().getName(), "/lang/commands/tempban.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String sDay = config.getString("tempban.time.days", "Tage");
        String sHours = config.getString("tempban.time.hours", "Stunden");
        String sMinutes = config.getString("tempban.time.minutes", "Minuten");
        String sSeconds = config.getString("tempban.time.seconds", "Sekunden");

        long days = time / (24 * 3600);
        long remainingSeconds = time % (24 * 3600);

        long hours = remainingSeconds / 3600;
        remainingSeconds %= 3600;

        long minutes = remainingSeconds / 60;
        long seconds = remainingSeconds % 60;

        StringBuilder result = new StringBuilder();

        if (days > 0) result.append("&2").append(days).append("&b ").append(sDay);
        if (hours > 0) result.append(" &2").append(hours).append("&b ").append(sHours);
        if (minutes > 0) result.append(" &2").append(minutes).append("&b ").append(sMinutes);
        if (seconds > 0) result.append(" &2").append(seconds).append("&b ").append(sSeconds);

        return result.toString().trim();
    }

    private static void saveConfig() {
        try {
            banConfig.save(banFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
