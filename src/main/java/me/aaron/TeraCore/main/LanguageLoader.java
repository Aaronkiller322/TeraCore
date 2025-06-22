package me.aaron.TeraCore.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LanguageLoader {

    public static enum LanguageFolder{
        commands, events, gobal
    }


    public static void load(LanguageFolder languageFolder, String filename) {

        List<String> language = List.of("de", "en");

        for (String lang : language) {
            File file;
            FileConfiguration config;
            if (languageFolder.equals(LanguageFolder.events)) {
                String datafolder_event = "plugins/" + TeraMain.getPlugin().getName() + "/lang_" + lang + "/events";
                File temp_event = new File(datafolder_event, filename + ".yml");

                if (!temp_event.exists()) {
                    config = getConfig(filename, lang);
                    file = temp_event;
                    try {
                        config.save(temp_event);
                    } catch (IOException e) {
                    }
                }
            }

            if (languageFolder.equals(LanguageFolder.commands)) {
                String datafolder_command = "plugins/" + TeraMain.getPlugin().getName() + "/lang_" + lang + "/commands";
                File temp_command = new File(datafolder_command, filename + ".yml");

                if (!temp_command.exists()) {
                    config = getConfig(filename, lang);
                    file = temp_command;
                    try {
                        config.save(temp_command);
                    } catch (IOException e) {
                    }
                }
            }
            if (languageFolder.equals(LanguageFolder.gobal)) {
                String datafolder_command = "plugins/" + TeraMain.getPlugin().getName() + "/lang_" + lang;
                File temp_command = new File(datafolder_command, filename + ".yml");

                if (!temp_command.exists()) {
                    config = getConfig(filename, lang);
                    file = temp_command;
                    try {
                        config.save(temp_command);
                    } catch (IOException e) {
                    }
                }
            }

        }
    }

    private static FileConfiguration config_default;
    private static String filetype_default;

    private static void initConfig(String lang) {
        // Datei aus dem JAR laden
        try (InputStream in = TeraMain.getPlugin().getResource("me/aaron/TeraCore/configs_" + lang+"/" + filetype_default + ".yml")) {
            if (in != null) {
                config_default = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
            } else {
                throw new IllegalArgumentException("File " + filetype_default + ".yml not found in the resource path." + "me/aaron/TeraCore/configs_" + lang+"/" + filetype_default + ".yml");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getConfig(String type, String lang) {
        filetype_default = type;
        initConfig(lang);
        return config_default;
    }

}
