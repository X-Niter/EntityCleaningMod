package com.github.xniter.config;

import net.minecraft.locale.Language;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CommonConfig {

    private final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    private final ForgeConfigSpec.BooleanValue enable_strict_lagremoval;
    private final ForgeConfigSpec.BooleanValue prevent_named_entity_removal;
    private final ForgeConfigSpec.BooleanValue clear_projectiles;
    private final ForgeConfigSpec.BooleanValue enable_limiter_lagremoval;
    private final ForgeConfigSpec.IntValue strict_timer_interval;
    private final ForgeConfigSpec.IntValue limiter_timer_interval;
    private final ForgeConfigSpec.IntValue MONSTER;
    private final ForgeConfigSpec.IntValue CREATURE;
    private final ForgeConfigSpec.IntValue AMBIENT;
    private final ForgeConfigSpec.IntValue AXOLOTLS;
    private final ForgeConfigSpec.IntValue UNDERGROUND_WATER_CREATURE;
    private final ForgeConfigSpec.IntValue WATER_CREATURE;
    private final ForgeConfigSpec.IntValue WATER_AMBIENT;
    private final ForgeConfigSpec.IntValue ITEMS;


    public CommonConfig() {

        builder.push("general");
        builder.comment("Some General Overall Settings For LagRemoval");
        prevent_named_entity_removal = buildBoolean(builder, "PreventNamedEntityRemoval", "all", true, "If true, Prevents Named Entites from getting removed by LagRemoval [Default:true]");
        clear_projectiles = buildBoolean(builder, "ClearProjectiles", "all", true, "If true, clears projectiles whenever LagRemoval runs ANY Lag Clearing Events, (This is something that runs passively sense Minecraft is very bad on removing these and then you find them floating in the world in random places)");
        builder.pop();

        builder.push("Strict LagRemoval");
        builder.comment("Strict LagRemoval will run on an interval that you set and will clear EVERYTHING on the server (Excluding the blacklist)");
        enable_strict_lagremoval = buildBoolean(builder, "EnableStrictLagRemoval", "Strict LagRemoval", false, "If true, Enables Strict LagRemoval [Default:false]");
        strict_timer_interval = buildInt(builder, "IntervalInMinutes", "Strict LagRemoval", 30, 1, 1440, "Interval (In Minutes) that Strict LagRemoval will run [Default:30 | Min:1 | Max:1440 (Is One day)]");
        builder.pop();

        builder.push("Limiter LagRemoval");
        builder.comment("Limiter LagRemoval will run on an interval that you set and will only remove/clear the item/entity if the limit is reached or is past the limit");
        enable_limiter_lagremoval = buildBoolean(builder, "EnableLimiterLagRemoval", "Limiter LagRemoval", true, "If true, Enables Limiter LagRemoval [Default:true]");
        limiter_timer_interval = buildInt(builder, "IntervalInMinutes", "Limiter LagRemoval", 30, 1, 1440, "Interval (In Minutes) that Limiter LagRemoval will run it's check [Default:30 | Min:1 | Max:1440 (Is One day)]");
        builder.push("Limiter Types");
        builder.comment("Below you will see you can define limits per types.");
        MONSTER = buildInt(builder, "MonsterLimit", "Limiter Types", 400, 0, 2000, "The Server Limit for this entity type [Default:400 | Disabled:0 | Max:2000]");
        CREATURE = buildInt(builder, "CreatureLimit", "Limiter Types", 400, 0, 2000, "The Server Limit for this entity type [Default:400 | Disabled:0 | Max:2000]");
        AMBIENT = buildInt(builder, "AmbientLimit", "Limiter Types", 400, 0, 2000, "The Server Limit for this entity type [Default:400 | Disabled:0 | Max:2000]");
        AXOLOTLS = buildInt(builder, "AxolotlsLimit", "Limiter Types", 150, 0, 2000, "The Server Limit for this entity type [Default:150 | Disabled:0 | Max:2000]");
        UNDERGROUND_WATER_CREATURE = buildInt(builder, "UndergroundWaterCreatureLimit", "Limiter Types", 500, 0, 2000, "The Server Limit for this entity type [Default:500 | Disabled:0 | Max:2000]");
        WATER_CREATURE = buildInt(builder, "WaterCreatureLimit", "Limiter Types", 250, 0, 2000, "The Server Limit for this entity type [Default:250 | Disabled:0 | Max:2000]");
        WATER_AMBIENT = buildInt(builder, "WaterAmbientLimit", "Limiter Types", 250, 0, 2000, "The Server Limit for this entity type [Default:250 | Disabled:0 | Max:2000]");
        ITEMS = buildInt(builder, "ItemLimit", "Limiter Types", 800, 0, 2000, "The Server Limit for this entity type [Default:250 | Disabled:0 | Max:2000]");
        builder.pop();


    }

    public ForgeConfigSpec getSpec() {

        return builder.build();
    }
    public boolean EnableStrictLagremoval() {
        return enable_strict_lagremoval.get();
    }
    public int StrictTimerInterval() {
        return strict_timer_interval.get();
    }
    public boolean PreventNamedEntityRemoval() {
        return  prevent_named_entity_removal.get();
    }
    public boolean EnableLimiterLagRemoval() {
        return enable_limiter_lagremoval.get();
    }
    public int LimiterTimerInterval() {
        return limiter_timer_interval.get();
    }
    public int MonsterLimit() {
        return MONSTER.get();
    }
    public int CreatureLimit() {
        return CREATURE.get();
    }
    public int AmbientLimit() {
        return AMBIENT.get();
    }
    public int AxolotlsLimit() {
        return AXOLOTLS.get();
    }
    public int UnderGroundWaterCreatureLimit() {
        return UNDERGROUND_WATER_CREATURE.get();
    }
    public int WaterCreatureLimit() {
        return  WATER_CREATURE.get();
    }
    public int WaterAmbientLimit() {
        return WATER_AMBIENT.get();
    }
    public int ItemsLimit() {
        return ITEMS.get();
    }
    public boolean ClearProjectiles() {
        return clear_projectiles.get();
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
