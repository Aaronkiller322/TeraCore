package me.aaron.TeraCore.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class SkinFetcher {
    public static ItemStack getSkull(String ownerName) {
        OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerName);
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        if (skullMeta != null) {
            GameProfile profile = new GameProfile(owner.getUniqueId(), ownerName);

            // Verwende Mojang API, um die Texturen zu laden
            String[] skinData = getSkinFromMojangAPI(ownerName);
            if (skinData != null) {
                profile.getProperties().put("textures", new Property("textures", skinData[0], skinData[1]));
            } else {
            }

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            skull.setItemMeta(skullMeta);
        }
        return skull;
    }

    private static String[] getSkinFromMojangAPI(String playerName) {
        try {
            // Hole die UUID des Spielers
            String uuid = UUIDFetcher.getUUID(playerName).toString();
            if (uuid == null) {
                return null;
            }

            // Hole die Textur-Daten von der Mojang API
            URL skinURL = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            HttpURLConnection skinConnection = (HttpURLConnection) skinURL.openConnection();
            skinConnection.setRequestMethod("GET");
            skinConnection.setConnectTimeout(5000);
            skinConnection.setReadTimeout(5000);

            if (skinConnection.getResponseCode() != 200) {
                return null;
            }

            BufferedReader skinReader = new BufferedReader(new InputStreamReader(skinConnection.getInputStream()));
            String skinResponse = readResponse(skinReader);

            // Extrahiere die Texturen- und Signatur-Daten
            String base64Value = extractBase64Value(skinResponse);
            String signature = extractSignature(skinResponse);

            if (base64Value != null && signature != null) {
                return new String[]{base64Value, signature};
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static String readResponse(BufferedReader reader) throws Exception {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

    private static String extractBase64Value(String response) {
        // Suche nach der Texturen-Base64-Daten
        String marker = "\"value\" : \"";
        int startIndex = response.indexOf(marker);
        if (startIndex != -1) {
            startIndex += marker.length();
            int endIndex = response.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return response.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    private static String extractSignature(String response) {
        // Suche nach der Signatur
        String marker = "\"signature\" : \"";
        int startIndex = response.indexOf(marker);
        if (startIndex != -1) {
            startIndex += marker.length();
            int endIndex = response.indexOf("\"", startIndex);
            if (endIndex != -1) {
                return response.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    private static String decodeBase64(String base64) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        return new String(decodedBytes);
    }
}
