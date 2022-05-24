package com.github.xniter.data;

import com.github.xniter.util.FileUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Blacklist {
    private static List<String> BLACKLIST = new ArrayList<>();

    public static void loadList() throws Exception {
        BLACKLIST.clear();
        File file = new File("world/serverconfig/Lag Removal/blacklist.json");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists()) {
            BLACKLIST.add(EntityType.ARMOR_STAND.getRegistryName().toString());
            BLACKLIST.add(EntityType.PAINTING.getRegistryName().toString());
            BLACKLIST.add(EntityType.BOAT.getRegistryName().toString());
            BLACKLIST.add(EntityType.VILLAGER.getRegistryName().toString());
            BLACKLIST.add(EntityType.IRON_GOLEM.getRegistryName().toString());
            BLACKLIST.add(EntityType.SNOW_GOLEM.getRegistryName().toString());
            BLACKLIST.add(EntityType.COMMAND_BLOCK_MINECART.getRegistryName().toString());
            BLACKLIST.add(EntityType.MINECART.getRegistryName().toString());
            BLACKLIST.add(EntityType.FURNACE_MINECART.getRegistryName().toString());
            BLACKLIST.add(EntityType.HOPPER_MINECART.getRegistryName().toString());
            BLACKLIST.add(EntityType.SPAWNER_MINECART.getRegistryName().toString());
            BLACKLIST.add(EntityType.ITEM_FRAME.getRegistryName().toString());
            saveBlacklist();
            return;
        }
        BLACKLIST = (List<String>) FileUtils.readObjectFromFile(List.class, file);
    }

    public static void saveBlacklist() {
        FileUtils.writeObjectToFile(BLACKLIST, "world/serverconfig/Lag Removal/blacklist.json");
    }

    public static void addToBlacklist(Entity e) {
        if (!BLACKLIST.contains(getKey(e))) {
            BLACKLIST.add(getKey(e));
            saveBlacklist();
        }
    }

    public static void removeFromBlacklist(Entity e) {
        if (BLACKLIST.contains(getKey(e))) {
            BLACKLIST.remove(getKey(e));
            saveBlacklist();
        }
    }

    public static void addToBlacklist(String e) {
        if (!BLACKLIST.contains(e)) {
            BLACKLIST.add(e);
            saveBlacklist();
        }
    }

    public static void removeFromBlacklist(String e) {
        if (BLACKLIST.contains(e)) {
            BLACKLIST.remove(e);
            saveBlacklist();
        }
    }

    public static boolean isBlacklisted(Entity e) {
        return BLACKLIST.contains(getKey(e));
    }

    public static boolean isBlacklisted(Item i) {
        return BLACKLIST.contains(getKey(i));
    }

    private static String getKey(Item i) {
        return Objects.requireNonNull(i.getRegistryName()).toString();
    }

    private static String getKey(Entity e) {
        return Objects.requireNonNull(e.getType().getRegistryName()).toString();
    }
}
