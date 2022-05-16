package com.github.xniter.config;

import com.github.xniter.util.FileUtils;

import java.io.File;

public class Config {
    public static Config INSTANCE = new Config();

    public int auto_clear_delay = 10;

    public boolean show_warning = true;

    public boolean enable_entity_limit_check = true;
    public int entity_limit_check_delay = 10;
    public int server_entity_limit = 300;

    public boolean prevent_named_entity_removal = true;

    public static void loadConfig() {
        File file = new File("config/Lag Removal/config.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            INSTANCE = new Config();
            saveConfig();
            return;
        }
        INSTANCE = (Config) FileUtils.readObjectFromFile(Config.class, file);
        if (INSTANCE == null) {
            INSTANCE = new Config();
            saveConfig();
        }
    }

    public static void saveConfig() {
        FileUtils.writeObjectToFile(INSTANCE, "config/Lag Removal/config.json");
    }
}
