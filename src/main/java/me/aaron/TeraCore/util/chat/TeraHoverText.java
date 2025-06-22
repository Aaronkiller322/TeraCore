package me.aaron.TeraCore.util.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeraHoverText {

    private String text = "§cEmpty";
    private String hovertext = null;
    private String copytext = null;
    private String commandToRun = null;
    private String color = null;

    public TeraHoverText(String text) {
        this.text = text;
        this.color = extractColorFromText(text);
    }

    public String getText() {
        return text;
    }

    public void setHoverText(String hovertext) {
        this.hovertext = hovertext;
    }

    public String getHovertext() {
        return hovertext;
    }

    public void setCopyText(String copytext) {
        this.copytext = copytext;
    }

    public String getCopytext() {
        return copytext;
    }

    public void setCommandToRun(String commandToRun) {
        this.commandToRun = commandToRun;
    }

    public String getCommandToRun() {
        return commandToRun;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getStrippedText() {
        return text
                .replaceAll("§x(§[0-9a-fA-F]){6}", "")
                .replaceAll("§[0-9a-fA-Fk-orK-OR]", "");
    }

    private String extractColorFromText(String text) {
        if (text == null) return null;

        Matcher hex = Pattern.compile("§x(§[0-9a-fA-F]){6}").matcher(text);
        if (hex.find()) {
            String raw = hex.group();
            StringBuilder hexColor = new StringBuilder("#");
            for (int i = 2; i < raw.length(); i += 2) {
                hexColor.append(raw.charAt(i + 1));
            }
            return hexColor.toString();
        }

        Matcher legacy = Pattern.compile("§[0-9a-fA-F]").matcher(text);
        String last = null;
        while (legacy.find()) last = legacy.group();
        if (last != null) {
            return legacyToJsonColor(last.charAt(1));
        }

        return null;
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
