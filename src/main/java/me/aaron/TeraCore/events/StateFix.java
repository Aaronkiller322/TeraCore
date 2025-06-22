package me.aaron.TeraCore.events;

import me.aaron.TeraCore.main.ConfigLoader;
import me.aaron.TeraCore.main.LanguageLoader;
import me.aaron.TeraCore.main.TeraMain;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class StateFix implements Listener {

    private final FileConfiguration config;

    private static String allPermission;
    private static String gamemodePermissionAll;
    private static String flyPermissionAll;

    private static Boolean gamemodeWorldSwitchEnabled;
    private static String gamemodeWorldSwitchPermission;

    private static Boolean gamemodeJoinEnabled;
    private static String gamemodeJoinPermission;

    private static Boolean gamemodeReloadEnabled;
    private static String gamemodeReloadPermission;

    private static Boolean flyWorldSwitchEnabled;
    private static String flyWorldSwitchPermission;

    private static Boolean flyJoinEnabled;
    private static String flyJoinPermission;

    private static Boolean flyReloadEnabled;
    private static String flyReloadPermission;

    private static final Set<UUID> gamemodeWorldSwitch = new HashSet<>();
    private static final Set<UUID> flyWorldSwitch = new HashSet<>();

    private static final Map<UUID, GameMode> gameModeHashMap = new HashMap<>();
    private static final Map<UUID, Boolean> flyHashMap_AllowFlight = new HashMap<>();
    private static final Map<UUID, Boolean> flyHashMap_isFlying = new HashMap<>();

    public StateFix() {
        String filetype = getClass().getSimpleName();
        LanguageLoader.load(LanguageLoader.LanguageFolder.events, filetype);
        File temp = new File(EventMain.datafolder, filetype + ".yml");
        if (temp.exists()) {
            config = YamlConfiguration.loadConfiguration(temp);
        } else {
            config = ConfigLoader.getConfig(filetype);
            try {
                config.save(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadConfig();
        onEnable();
    }

    private void loadConfig() {
        this.allPermission = config.getString("permissions.all");
        this.gamemodePermissionAll = config.getString("gamemode.permission_all");
        this.flyPermissionAll = config.getString("fly.permission_all");

        this.gamemodeWorldSwitchEnabled = config.getBoolean("gamemode.world_switch.enabled");
        this.gamemodeWorldSwitchPermission = config.getString("gamemode.world_switch.permission");

        this.gamemodeJoinEnabled = config.getBoolean("gamemode.join.enabled");
        this.gamemodeJoinPermission = config.getString("gamemode.join.permission");

        this.gamemodeReloadEnabled = config.getBoolean("gamemode.reload.enabled");
        this.gamemodeReloadPermission = config.getString("gamemode.reload.permission");

        this.flyWorldSwitchEnabled = config.getBoolean("fly.world_switch.enabled");
        this.flyWorldSwitchPermission = config.getString("fly.world_switch.permission");

        this.flyJoinEnabled = config.getBoolean("fly.join.enabled");
        this.flyJoinPermission = config.getString("fly.join.permission");

        this.flyReloadEnabled = config.getBoolean("fly.reload.enabled");
        this.flyReloadPermission = config.getString("fly.reload.permission");
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (hasPermission(player, StatusType.GAMEMODE, FixType.WORLD_SWITCH) &&
                !event.getFrom().getWorld().getName().equalsIgnoreCase(event.getTo().getWorld().getName())) {
            startGameMode(player, player.getGameMode());
        }

        if (hasPermission(player, StatusType.FLY, FixType.WORLD_SWITCH) &&
                !event.getFrom().getWorld().getName().equalsIgnoreCase(event.getTo().getWorld().getName())) {
            startFly(player, player.getAllowFlight(), player.isFlying());
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (gamemodeJoinEnabled && hasPermission(player, StatusType.GAMEMODE, FixType.JOIN)) {
            StateManager.of(player).setGameMode(player.getGameMode());
        }

        if (flyJoinEnabled && hasPermission(player, StatusType.FLY, FixType.JOIN)) {
            StateManager.of(player).setAllowFlight(player.getAllowFlight());
            StateManager.of(player).setFlying(player.isFlying());
        }
    }

    public static void onDisable(){
        try {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (flyReloadEnabled) {
                    if (hasPermission(online, StatusType.FLY, FixType.RELOAD)) {
                        StateManager.of(online).setFlying(online.isFlying());
                        StateManager.of(online).setAllowFlight(online.getAllowFlight());
                    }
                }
                if (gamemodeReloadEnabled) {
                    if (hasPermission(online, StatusType.GAMEMODE, FixType.RELOAD)) {
                        StateManager.of(online).setGameMode(online.getGameMode());
                    }
                }

            }
        }catch (Exception ex){}
    }

    public void onEnable(){
        for(Player online : Bukkit.getOnlinePlayers()){
            if(flyReloadEnabled){
                if(hasPermission(online, StatusType.FLY, FixType.RELOAD)){
                    startFly(online, StateManager.of(online).getAllowFlight(), StateManager.of(online).isFlying());
                }
            }
            if(gamemodeReloadEnabled){
                if(hasPermission(online, StatusType.GAMEMODE, FixType.RELOAD)){
                    startGameMode(online, StateManager.of(online).getGamemode());
                }
            }

        }
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (gamemodeJoinEnabled && hasPermission(player, StatusType.GAMEMODE, FixType.JOIN)) {
            startGameMode(player, StateManager.of(player).getGamemode());
        }

        if (flyJoinEnabled && hasPermission(player, StatusType.FLY, FixType.JOIN)) {
            startFly(player, StateManager.of(player).getAllowFlight(), StateManager.of(player).isFlying());
        }
    }

    public enum StatusType {
        GAMEMODE, FLY
    }

    public enum FixType {
        JOIN, WORLD_SWITCH, RELOAD
    }

    private static boolean hasPermission(Player player, StatusType statusType, FixType fixType) {
        if (player.hasPermission(allPermission)) return true;

        return switch (statusType) {
            case FLY -> player.hasPermission(flyPermissionAll) ||
                    switch (fixType) {
                        case WORLD_SWITCH -> player.hasPermission(flyWorldSwitchPermission);
                        case JOIN -> player.hasPermission(flyJoinPermission);
                        case RELOAD -> player.hasPermission(flyReloadPermission);
                    };
            case GAMEMODE -> player.hasPermission(gamemodePermissionAll) ||
                    switch (fixType) {
                        case WORLD_SWITCH -> player.hasPermission(gamemodeWorldSwitchPermission);
                        case JOIN -> player.hasPermission(gamemodeJoinPermission);
                        case RELOAD -> player.hasPermission(gamemodeReloadPermission);
                    };
        };
    }

    public void startGameMode(Player player, GameMode gameMode) {
        UUID uuid = player.getUniqueId();
        gameModeHashMap.put(uuid, gameMode);

        repeatUntil(
                () -> player.setGameMode(gameMode),
                p -> p.getGameMode() == gameMode,
                () -> {
                    gamemodeWorldSwitch.remove(uuid);
                    gameModeHashMap.remove(uuid);
                },
                () -> gameModeHashMap.remove(uuid),
                player
        );
    }

    public void startFly(Player player, boolean allowFlight, boolean isFlying) {
        UUID uuid = player.getUniqueId();
        flyHashMap_AllowFlight.put(uuid, allowFlight);
        flyHashMap_isFlying.put(uuid, isFlying);

        repeatUntil(
                () -> {
                    player.setAllowFlight(allowFlight);
                    player.setFlying(isFlying);
                },
                p -> p.getAllowFlight() == allowFlight && p.isFlying() == isFlying,
                () -> {
                    flyWorldSwitch.remove(uuid);
                    flyHashMap_AllowFlight.remove(uuid);
                    flyHashMap_isFlying.remove(uuid);
                },
                () -> {
                    flyHashMap_AllowFlight.remove(uuid);
                    flyHashMap_isFlying.remove(uuid);
                },
                player
        );
    }

    private void repeatUntil(Runnable task, Predicate<Player> condition, Runnable onSuccess, Runnable onFail, Player player) {
        new BukkitRunnable() {
            int attempts = 0;
            final int maxAttempts = 20;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    this.cancel();
                    return;
                }

                task.run();

                if (condition.test(player)) {
                    onSuccess.run();
                    this.cancel();
                    return;
                }

                if (++attempts >= maxAttempts) {
                    onFail.run();
                    this.cancel();
                }
            }
        }.runTaskTimer(TeraMain.getPlugin(), 10L, 5L);
    }
}
