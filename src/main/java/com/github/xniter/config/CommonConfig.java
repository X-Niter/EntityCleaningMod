package com.github.xniter.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CommonConfig {

        public ForgeConfigSpec.BooleanValue enable_strict_lagremoval;
        public ForgeConfigSpec.IntValue strict_timer_interval;
        public ForgeConfigSpec.BooleanValue prevent_named_entity_removal;

        public CommonConfig(final ForgeConfigSpec.Builder builder) {

            builder.push("general");
            builder.comment("Some General Overall Settings For LagRemoval");
            prevent_named_entity_removal = buildBoolean(builder, "PreventNamedEntityRemoval","all",true, "If true, Prevents Named Entites from getting removed by LagRemoval");
            builder.pop();

            builder.push("Strict LagRemoval");
            builder.comment("Strict LagRemoval will run on an interval that you set and will clear EVERYTHING on the server (Excluding the blacklist)");
            enable_strict_lagremoval = buildBoolean(builder,"EnableStrictLagRemoval","Strict LagRemoval",false,"If true, Enables Strict LagRemoval [default:false]");
            strict_timer_interval = buildInt(builder,"IntervalInMinutes","Strict LagRemoval",30,1,1440,"Interval (In Minutes) that Strict LagRemoval will run [default:30]");

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
