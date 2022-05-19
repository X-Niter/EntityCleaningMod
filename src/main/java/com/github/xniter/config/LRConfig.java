package com.github.xniter.config;

import com.github.xniter.LagRemoval;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class LRConfig {
    public static boolean enable_strict_lagremoval = false;
    public static int strict_timer_interval = 10;
    public static boolean prevent_named_entity_removal = true;

    public static void bake(ModConfig config) {
        try {
            enable_strict_lagremoval = ConfigHolder.COMMON.enable_strict_lagremoval.get();
            strict_timer_interval = ConfigHolder.COMMON.strict_timer_interval.get();
            prevent_named_entity_removal = ConfigHolder.COMMON.prevent_named_entity_removal.get();
        } catch (Exception e) {
            LagRemoval.LOGGER.warn("An exception was caused trying to load the config for Lag Removal.");
            e.printStackTrace();
        }
    }
}
