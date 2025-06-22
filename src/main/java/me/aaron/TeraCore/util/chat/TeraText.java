package me.aaron.TeraCore.util.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeraText {

    private final List<TeraHoverText> parts = new ArrayList<>();

    public TeraText(String text) {
        addFormattedText(text);
    }

    public void addText(String text) {
        addFormattedText(text);
    }

    public void addHoverText(TeraHoverText teraHoverText) {
        parts.add(teraHoverText);
    }

    public void sendMessage(Player player) {
        StringBuilder json = new StringBuilder("[\"\"");

        for (TeraHoverText part : parts) {
            if (part == null || part.getText() == null) continue;

            json.append(",{");
            json.append("\"text\":\"").append(escape(part.getStrippedText())).append("\"");

            String color = part.getColor();
            if (color != null) {
                json.append(",\"color\":\"").append(color).append("\"");
            }

            List<String> additions = new ArrayList<>();

            if (part.getHovertext() != null) {
                String hoverJson = buildColoredHoverJson(part.getHovertext());
                additions.add("\"hoverEvent\":{\"action\":\"show_text\",\"contents\":" + hoverJson + "}");
            }

            if (part.getCommandToRun() != null) {
                additions.add("\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + escape(part.getCommandToRun()) + "\"}");
            } else if (part.getCopytext() != null) {
                additions.add("\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"" + escape(part.getCopytext()) + "\"}");
            }

            for (String s : additions) {
                json.append(",").append(s);
            }

            json.append("}");
        }

        json.append("]");

        String fullJson = json.toString();

        String command = "tellraw " + player.getName() + " " + fullJson;
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public void addFormattedText(String text) {
        if (text == null || text.isEmpty()) return;

        int i = 0;
        String currentColor = null;

        while (i < text.length()) {
            if (text.startsWith("§x", i) && i + 13 < text.length()) {
                String hexRaw = text.substring(i, i + 14);
                currentColor = "#" + hexRaw.replaceAll("§x|§", "");
                i += 14;
                continue;
            }

            if (text.charAt(i) == '§' && i + 1 < text.length()) {
                currentColor = legacyToJsonColor(text.charAt(i + 1));
                i += 2;
                continue;
            }

            int codePoint = text.codePointAt(i);
            i += Character.charCount(codePoint);
            String letter = new String(Character.toChars(codePoint));

            TeraHoverText part = new TeraHoverText(letter);
            if (currentColor != null) part.setColor(currentColor);
            addHoverText(part);
        }
    }

    private String buildColoredHoverJson(String text) {
        StringBuilder builder = new StringBuilder("[\"\"");

        int i = 0;
        String currentColor = null;

        while (i < text.length()) {
            if (text.startsWith("§x", i) && i + 13 < text.length()) {
                String hexRaw = text.substring(i, i + 14);
                currentColor = "#" + hexRaw.replaceAll("§x|§", "");
                i += 14;
                continue;
            }

            if (text.charAt(i) == '§' && i + 1 < text.length()) {
                currentColor = legacyToJsonColor(text.charAt(i + 1));
                i += 2;
                continue;
            }

            int codePoint = text.codePointAt(i);
            i += Character.charCount(codePoint);
            String letter = new String(Character.toChars(codePoint));

            builder.append(",{")
                    .append("\"text\":\"").append(escape(letter)).append("\"");

            if (currentColor != null) {
                builder.append(",\"color\":\"").append(currentColor).append("\"");
            }

            builder.append("}");
        }

        builder.append("]");
        return builder.toString();
    }

    private String escape(String text) {
        if (text == null) return "";
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private String legacyToJsonColor(char code) {
        return switch (Character.toLowerCase(code)) {
            case '0' -> "black";
            case '1' -> "dark_blue";
            case '2' -> "dark_green";
            case '3' -> "dark_aqua";
            case '4' -> "dark_red";
            case '5' -> "dark_purple";
            case '6' -> "gold";
            case '7' -> "gray";
            case '8' -> "dark_gray";
            case '9' -> "blue";
            case 'a' -> "green";
            case 'b' -> "aqua";
            case 'c' -> "red";
            case 'd' -> "light_purple";
            case 'e' -> "yellow";
            case 'f' -> "white";
            default -> null;
        };
    }
}
