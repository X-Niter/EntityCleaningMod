package com.github.xniter.config;

import com.github.xniter.util.FileUtils;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class Config {

    public static ForgeConfigSpec.Builder INSTANCE = new ForgeConfigSpec.Builder();
    private ForgeConfigSpec.BooleanValue enable_strict_lagremoval;
    private ForgeConfigSpec.IntValue strict_timer;
    private ForgeConfigSpec.BooleanValue prevent_named_entity_removal;

    public Config() {

        INSTANCE.push("LagRemoval");
        INSTANCE.comment("Some General Overall Settings For LagRemoval");
        prevent_named_entity_removal = INSTANCE.comment("If true, Prevents Named Entites from getting removed by LagRemoval").define("Ignore Named Entites", true);

        INSTANCE.push("Strict LagRemoval");
        INSTANCE.comment("Strict LagRemoval will run on an interval that you set and will clear EVERYTHING on the server (Excluding the blacklist)");
        enable_strict_lagremoval = INSTANCE.comment("If true, Enables Strict LagRemoval [default:false]").define("enabled", false);
        strict_timer = INSTANCE.comment("Interval (In Minutes) that Strict LagRemoval will run [default:30]").defineInRange("timer", 30, 1, 1440);
    }

    public ForgeConfigSpec getConfig() {

        return INSTANCE.build();
    }
    public boolean enable_strict_lagremoval() {
        return enable_strict_lagremoval.get();
    }
    public int strict_timer() {
        return strict_timer.get();
    }
    public boolean prevent_named_entity_removal() {
        return prevent_named_entity_removal.get();
    }

    public static void loadConfig() {
        File file = new File("config/Lag Removal/config.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            INSTANCE = new ForgeConfigSpec.Builder();
            saveConfig();
            return;
        }
        INSTANCE = (ForgeConfigSpec.Builder) FileUtils.readObjectFromFile(ForgeConfigSpec.Builder.class, file);
        if (INSTANCE == null) {
            INSTANCE = new ForgeConfigSpec.Builder();
            saveConfig();
        }
    }

    public static void saveConfig() {
        FileUtils.writeObjectToFile(INSTANCE, "config/Lag Removal/config.json");
    }
}
