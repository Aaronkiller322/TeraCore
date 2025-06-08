package me.aaron.TeraCore.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class UUIDFetcher {

    public static final long FEBRUARY_2015 = 1422748800000L;

    private static Gson gson = new GsonBuilder().create();

    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profile/%s";

    private static Map<String, UUID> uuidCache = new HashMap<>();
    private static Map<UUID, String> nameCache = new HashMap<>();

    private static ExecutorService pool = Executors.newCachedThreadPool();

    private String name;
    private UUID id;

    public static void getUUID(String name, Consumer<UUID> action) {
        pool.execute(() -> action.accept(getUUID(name)));
    }

    public static UUID getUUID(String name) {
        return getUUIDAt(name, System.currentTimeMillis());
    }

    public static void getUUIDAt(String name, long timestamp, Consumer<UUID> action) {
        pool.execute(() -> action.accept(getUUIDAt(name, timestamp)));
    }

    public static UUID getUUIDAt(String name, long timestamp) {
        name = name.toLowerCase();
        if (uuidCache.containsKey(name)) {
            return uuidCache.get(name);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(UUID_URL, name, timestamp / 1000)).openConnection();
            connection.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);

            String id = jsonObject.get("id").getAsString();
            String formattedId = formatUUID(id);

            UUIDFetcher data = new UUIDFetcher();
            data.id = UUID.fromString(formattedId);
            data.name = jsonObject.get("name").getAsString();

            uuidCache.put(name, data.id);
            nameCache.put(data.id, data.name);

            return data.id;
        } catch (Exception e) {
        }

        return null;
    }

    public static void getName(UUID uuid, Consumer<String> action) {
        pool.execute(() -> action.accept(getName(uuid)));
    }

    public static String getName(UUID uuid) {
        if (nameCache.containsKey(uuid)) {
            return nameCache.get(uuid);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(NAME_URL, formatUUID(uuid.toString()))).openConnection();
            connection.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);

            UUIDFetcher currentNameData = new UUIDFetcher();
            currentNameData.id = UUID.fromString(formatUUID(jsonObject.get("id").getAsString()));
            currentNameData.name = jsonObject.get("name").getAsString();

            uuidCache.put(currentNameData.name.toLowerCase(), uuid);
            nameCache.put(uuid, currentNameData.name);

            return currentNameData.name;
        } catch (Exception e) {
        }

        return null;
    }

    private static String formatUUID(String id) {
        return id.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5"
        );
    }
}



