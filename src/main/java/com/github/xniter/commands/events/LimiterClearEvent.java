package com.github.xniter.commands.events;

import com.github.xniter.LagRemoval;
import com.github.xniter.data.Blacklist;
import net.minecraft.ChatFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;

public class LimiterClearEvent {
    private static int counter = 0;

    public long l1;
    public int seconds;
    public boolean w60;
    public boolean isScanning;

    public boolean isCleaning;

    public boolean removed;

    private List<Entity> monsters = new ArrayList<>();
    private List<Entity> creatures = new ArrayList<>();
    private List<Entity> ambients = new ArrayList<>();
    private List<Entity> axolotls = new ArrayList<>();
    private List<Entity> undergroundWaterCreatures = new ArrayList<>();
    private List<Entity> waterCreatures = new ArrayList<>();
    private List<Entity> waterAmbients = new ArrayList<>();
    private List<Entity> items = new ArrayList<>();
    private List<Entity> projectiles = new ArrayList<>();

    @SubscribeEvent
    public void LimiterLagRemovalEvent(TickEvent.ServerTickEvent event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (this.l1 != System.currentTimeMillis() / 1000) {
            this.l1 = System.currentTimeMillis() / 1000;
            this.seconds++;
        }
        if (LagRemoval.LRConfig.EnableLimiterLagRemoval()) {

            if (this.seconds == LagRemoval.LRConfig.LimiterTimerInterval() * 60 && !this.w60 && !this.isScanning && !this.isCleaning) {
                this.w60 = true;
                this.isScanning = true;
                LagRemoval.sendToAll(server, ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.DARK_GREEN + "Scanning for Entity limits.");
                this.removed = false;
                counter = 0;
            }
            if (this.seconds == LagRemoval.LRConfig.LimiterTimerInterval() * 61 && this.w60 && this.isScanning && !this.isCleaning) {
                this.w60 = false;
                this.removed = false;
                counter = 0;
            }

            if (this.seconds == LagRemoval.LRConfig.LimiterTimerInterval() * 62 && !this.w60 && this.isScanning && !this.isCleaning) {


                server.getAllLevels().forEach(world -> world.getAllEntities().forEach(e -> {
                    if (LagRemoval.LRConfig.MonsterLimit() > 0 && e.getType().getCategory() == MobCategory.MONSTER && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        monsters.add(e);
                    }
                    if (LagRemoval.LRConfig.CreatureLimit() > 0 && e.getType().getCategory() == MobCategory.CREATURE && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        creatures.add(e);
                    }
                    if (LagRemoval.LRConfig.AmbientLimit() > 0 && e.getType().getCategory() == MobCategory.AMBIENT && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        ambients.add(e);
                    }
                    if (LagRemoval.LRConfig.AxolotlsLimit() > 0 && e.getType().getCategory() == MobCategory.AXOLOTLS && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        axolotls.add(e);
                    }
                    if (LagRemoval.LRConfig.UnderGroundWaterCreatureLimit() > 0 && e.getType().getCategory() == MobCategory.UNDERGROUND_WATER_CREATURE && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        undergroundWaterCreatures.add(e);
                    }
                    if (LagRemoval.LRConfig.WaterCreatureLimit() > 0 && e.getType().getCategory() == MobCategory.WATER_CREATURE && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        waterCreatures.add(e);
                    }
                    if (LagRemoval.LRConfig.WaterAmbientLimit() > 0 && e.getType().getCategory() == MobCategory.WATER_AMBIENT && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        waterAmbients.add(e);
                    }
                    if (LagRemoval.LRConfig.ItemsLimit() > 0) {
                        if (e instanceof ItemEntity && e.isOnGround()) {
                            ItemStack item = ((ItemEntity) e).getItem();
                            if (!Blacklist.isBlacklisted(item.getItem()) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                                items.add(e);
                            }
                        }
                    }
                    if (LagRemoval.LRConfig.ClearProjectiles()) {
                        if (e instanceof Projectile) {
                            if (!Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                                projectiles.add(e);
                            }
                        }
                    }
                    this.removed = false;
                    counter = 0;
                }));

                this.isScanning = false;
                this.isCleaning = true;
            }

            removed = false;
            counter = 0;

            if (!this.w60 && !this.isScanning && this.isCleaning) {
                if (monsters.size() >= LagRemoval.LRConfig.MonsterLimit()) {
                    monsters.forEach(monster -> {
                        if (monster.getType().getCategory() == MobCategory.MONSTER && !Blacklist.isBlacklisted(monster) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !monster.hasCustomName())) {
                            monster.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    monsters.clear();
                }
                if (creatures.size() >= LagRemoval.LRConfig.CreatureLimit()) {
                    creatures.forEach(creature -> {
                        if (creature.getType().getCategory() == MobCategory.CREATURE && !Blacklist.isBlacklisted(creature) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !creature.hasCustomName())) {
                            creature.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    creatures.clear();
                }
                if (ambients.size() >= LagRemoval.LRConfig.AmbientLimit()) {
                    ambients.forEach(ambient -> {
                        if (ambient.getType().getCategory() == MobCategory.AMBIENT && !Blacklist.isBlacklisted(ambient) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !ambient.hasCustomName())) {
                            ambient.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    ambients.clear();
                }
                if (axolotls.size() >= LagRemoval.LRConfig.AxolotlsLimit()) {
                    axolotls.forEach(axolot -> {
                        if (axolot.getType().getCategory() == MobCategory.AXOLOTLS && !Blacklist.isBlacklisted(axolot) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !axolot.hasCustomName())) {
                            axolot.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    axolotls.clear();
                }
                if (undergroundWaterCreatures.size() >= LagRemoval.LRConfig.UnderGroundWaterCreatureLimit()) {
                    undergroundWaterCreatures.forEach(uwc -> {
                        if (uwc.getType().getCategory() == MobCategory.UNDERGROUND_WATER_CREATURE && !Blacklist.isBlacklisted(uwc) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !uwc.hasCustomName())) {
                            uwc.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    undergroundWaterCreatures.clear();
                }
                if (waterCreatures.size() >= LagRemoval.LRConfig.WaterCreatureLimit()) {
                    waterCreatures.forEach(wc -> {
                        if (wc.getType().getCategory() == MobCategory.WATER_CREATURE && !Blacklist.isBlacklisted(wc) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !wc.hasCustomName())) {
                            wc.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    waterCreatures.clear();
                }
                if (waterAmbients.size() >= LagRemoval.LRConfig.WaterAmbientLimit()) {
                    waterAmbients.forEach(wa -> {
                        if (wa.getType().getCategory() == MobCategory.WATER_AMBIENT && !Blacklist.isBlacklisted(wa) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !wa.hasCustomName())) {
                            wa.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    waterAmbients.clear();
                }
                if (items.size() >= LagRemoval.LRConfig.ItemsLimit()) {
                    items.forEach(item -> {
                        if (!Blacklist.isBlacklisted(item) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !item.hasCustomName())) {
                            item.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    items.clear();
                }
                if (LagRemoval.LRConfig.ClearProjectiles() && projectiles.size() > 0) {
                    projectiles.forEach(pj -> {
                        if (!Blacklist.isBlacklisted(pj) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !pj.hasCustomName())) {
                            pj.remove(Entity.RemovalReason.DISCARDED);
                            removed = true;
                            counter++;
                        }
                    });
                    projectiles.clear();
                }

                if (removed) {
                    LagRemoval.sendToAll(server, ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.DARK_GREEN + "Limiter Removing " + ChatFormatting.RED + counter + ChatFormatting.DARK_GREEN + " Entities.");
                } else {
                    LagRemoval.sendToAll(server, ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.DARK_GREEN + "Limiter found nothing to remove.");
                }
                this.removed = false;
                counter = 0;
                this.isCleaning = false;
                this.seconds = 0;
            }
        }
    }
}
