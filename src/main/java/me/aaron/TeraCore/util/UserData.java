package me.aaron.TeraCore.util;

import me.aaron.TeraCore.main.TeraMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UserData {

    private UUID uuid;

    private File file;
    private FileConfiguration config;

    public enum FilePath{
        TPATOGGLE("tpa.allow"),
        STATEFIX_GAMEMODE("statefix.gamemode"),
        STATEFIX_ALLOWFLIGHT("statefix.allowflight"),
        STATEFIX_FLYING("statefix.flying");

        private final String path;

        FilePath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }


    public UserData(UUID uuid) {
        String dataFolder = "plugins" + File.separator + TeraMain.getPlugin().getName() + File.separator + "data" + File.separator + "userdata";
        this.file = new File(dataFolder, uuid.toString() + ".yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void setData(FilePath filePath, Object object){
        config.set(filePath.getPath(), object);
        saveData();
    }

    public Object getData(FilePath filePath){
        if(config.get(filePath.getPath()) == null){
            return null;
        }
        return config.get(filePath.getPath());
    }

    private void saveData() {
        try {
            config.save(file);
        } catch (IOException e) {
        }
    }

}
