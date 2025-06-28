package me.aaron.TeraCore.events;

import me.aaron.TeraCore.main.TeraMain;
import me.aaron.TeraCore.util.UserData;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class StateManager {

    public static StateManager of(Player player) {
        return new StateManager(player.getUniqueId());
    }

    public static StateManager of(UUID uuid) {
        return new StateManager(uuid);
    }

    private UserData userData;

    public StateManager(UUID uuid) {
        if(TeraMain.userDataHashMap.containsKey(uuid)){
            this.userData = TeraMain.userDataHashMap.get(uuid);
        }else {
            this.userData = new UserData(uuid);
        }
    }

    public void setGameMode(GameMode gamemode) {
        userData.setData(UserData.FilePath.STATEFIX_GAMEMODE, gamemode.toString());
    }

    public void setAllowFlight(boolean status) {
        userData.setData(UserData.FilePath.STATEFIX_ALLOWFLIGHT, status);
    }

    public void setFlying(boolean status) {
        userData.setData(UserData.FilePath.STATEFIX_FLYING, status);
    }

    public GameMode getGamemode() {
        String mode = String.valueOf(userData.getData(UserData.FilePath.STATEFIX_GAMEMODE));
        try {
            return GameMode.valueOf(mode.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return GameMode.SURVIVAL;
        }
    }

    public Boolean getAllowFlight() {
        Object object = userData.getData(UserData.FilePath.STATEFIX_ALLOWFLIGHT);
        if(object instanceof Boolean){
            return (boolean) userData.getData(UserData.FilePath.STATEFIX_ALLOWFLIGHT);
        }
        return false;
    }

    public Boolean isFlying() {
        Object object = userData.getData(UserData.FilePath.STATEFIX_FLYING);
        if(object instanceof Boolean){
            return (boolean) userData.getData(UserData.FilePath.STATEFIX_FLYING);
        }
        return false;
    }
}
