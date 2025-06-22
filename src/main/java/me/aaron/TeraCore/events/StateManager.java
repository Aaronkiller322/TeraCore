package me.aaron.TeraCore.events;

import me.aaron.TeraCore.main.TeraMain;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class StateManager {

    private final File file;
    private final FileConfiguration config;

    public static StateManager of(Player player) {
        return new StateManager(player.getUniqueId());
    }

    public static StateManager of(UUID uuid) {
        return new StateManager(uuid);
    }


    public StateManager(UUID uuid) {
        String dataFolder = "plugins" + File.separator + TeraMain.getPlugin().getName() + File.separator + "data" + File.separator + "statefix";
        this.file = new File(dataFolder, uuid.toString() + ".yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace(); // Option: Logger verwenden
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setGameMode(GameMode gameMode) {
        config.set("gamemode", gameMode.name());
        save();
    }

    public void setAllowFlight(boolean status) {
        config.set("allowflight", status);
        save();
    }

    public void setFlying(boolean status) {
        config.set("flying", status);
        save();
    }

    public GameMode getGamemode() {
        String mode = config.getString("gamemode", "SURVIVAL");
        try {
            return GameMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return GameMode.SURVIVAL;
        }
    }

    public boolean getAllowFlight() {
        return config.getBoolean("allowflight", false);
    }

    public boolean isFlying() {
        return config.getBoolean("flying", false);
    }

    private void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace(); // Option: Logger verwenden
        }
    }
}
