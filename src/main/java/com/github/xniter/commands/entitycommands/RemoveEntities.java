package com.github.xniter.commands.entitycommands;

import com.github.xniter.LagRemoval;
import com.github.xniter.data.Blacklist;
import com.github.xniter.util.CommandUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class RemoveEntities {
    private static final SuggestionProvider<CommandSourceStack> tabCompleteEntities = (ctx, builder) -> SharedSuggestionProvider.suggestResource(ForgeRegistries.ENTITIES.getKeys().stream(), builder);
    private static final SuggestionProvider<CommandSourceStack> tabCompleteItems = (ctx, builder) -> SharedSuggestionProvider.suggestResource(ForgeRegistries.ITEMS.getKeys().stream(), builder);
    private static int counter = 0;
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("clear")
                .then(Commands.literal("all")
                        .executes(ctx -> killAll(ctx, null))
                )
                .then(Commands.literal("byType")
                                .then(Commands.literal("entities")
                                        .then(Commands.literal("AllHostiles")
                                                .executes(RemoveEntities::removeHostile)
                                        )
                                        .then(Commands.literal("AllAmbient")
                                                .executes(RemoveEntities::removeAmbient)
                                        )
                                        .then(Commands.literal("AllCreatures")
                                                .executes(RemoveEntities::removeCreature)
                                        )
                                        .then(Commands.argument("entity", ResourceLocationArgument.id())
                                                        .suggests(tabCompleteEntities)
                                                        .executes(ctx -> removeEntities(ctx, ResourceLocationArgument.getId(ctx, "entity")))
                                        )
                                )
                                .then(Commands.literal("items")
                                        .executes(ctx -> removeItems(ctx, null))
                                        .then(Commands.argument("itemID", StringArgumentType.word())
                                                .executes(ctx -> removeItems(ctx, StringArgumentType.getString(ctx, "itemID")))
                                        )
                                )
                );
    }

    public static int killAll(CommandContext<CommandSourceStack> context, ResourceLocation type) {

        counter = 0;
        List<Entity> allP = new ArrayList<>();
        List<Entity> allI = new ArrayList<>();
        List<Entity> allE = new ArrayList<>();
        //List<Entity> 3.1415 = new ArrayList<>();
        List<ServerLevel> worlds = CommandUtils.getWorlds(context);
        worlds.forEach(world -> world.getEntities().getAll().forEach(e -> {
            if (type == null) {
                if (!(e instanceof net.minecraft.server.level.ServerPlayer))
                    if (e instanceof ItemEntity && e.isOnGround()) {
                        ItemStack item = ((ItemEntity) e).getItem();
                        if (!Blacklist.isBlacklisted(item.getItem()) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                            allI.add(e);

                        }
                    }
                if (e instanceof LivingEntity) {
                    if (!Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        allE.add(e);
                    }
                }
                if (e instanceof Projectile) {
                    if (LagRemoval.LRConfig.ClearProjectiles() && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        allE.add(e);
                    }
                }
            }
            else {
                if (e.getType().getRegistryName() != null && e.getType().getRegistryName().equals(type) && !e.hasCustomName()) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }));

        if (allI.size() > 0) {
            allI.forEach(e -> {
                ItemStack item = ((ItemEntity) e).getItem();
                if (!Blacklist.isBlacklisted(item.getItem()) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {

                    e.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
            allI.clear();
        }

        if (allE.size() > 0) {
            allE.forEach(e -> {
                if (!Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
            allE.clear();
        }

        LagRemoval.sendToAll(context.getSource().getServer(), ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.GREEN + "Removed " + ChatFormatting.RED + counter + ChatFormatting.GREEN + " entities");
        return 1;
    }


    private static int removeEntities(CommandContext<CommandSourceStack> context, ResourceLocation type) {
        counter = 0;
        List<Entity> allE = new ArrayList<>();
        List<Entity> allType = new ArrayList<>();
        List<ServerLevel> worlds = CommandUtils.getWorlds(context);
        worlds.forEach(world -> world.getEntities().getAll().forEach(e -> {
            if (type == null) {
                if (!Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                    allE.add(e);
                }
            }
            if (e.getType().getRegistryName() != null && e.getType().getRegistryName().equals(type) && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                allType.add(e);
            }
        }));
        if (allType.size() > 0) {
            allType.forEach(e -> {
                if (e.getType().getRegistryName() != null && e.getType().getRegistryName().equals(type) && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
        }
        if (allE.size() > 0) {
            allE.forEach(e -> {
                if (!Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
        }

        respond(context);
        return 1;
    }

    private static int removeItems(CommandContext<CommandSourceStack> context, String type) {
        counter = 0;
        List<Entity> itemL = new ArrayList<>();
        List<Entity> itemLType = new ArrayList<>();
        List<ServerLevel> worlds = CommandUtils.getWorlds(context);
        worlds.forEach(world -> world.getEntities().getAll().forEach(e -> {
            if (e instanceof ItemEntity) {
                if (type == null) {
                    ItemStack item = ((ItemEntity) e).getItem();
                    if (!Blacklist.isBlacklisted(item.getItem()) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        itemL.add(e);
                    }
                }
                else if (e.getName().getString().contains(type)) {
                    ItemStack item = ((ItemEntity) e).getItem();
                    if (!Blacklist.isBlacklisted(item.getItem()) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                        itemLType.add(e);
                    }
                }
            }
        }));

        if (itemLType.size() > 0) {
            itemLType.forEach(e -> {
                if (e.getType().getRegistryName() != null && e.getType().getRegistryName().equals(type) && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
        }
        if (itemL.size() > 0) {
            itemL.forEach(e -> {
                if (!Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
        }

        respond(context);
        return 1;
    }

    private static int removeHostile(CommandContext<CommandSourceStack> context) {
        counter = 0;

        List<Entity> hostiles = new ArrayList<>();
        List<ServerLevel> worlds = CommandUtils.getWorlds(context);
        worlds.forEach(world -> world.getEntities().getAll().forEach(e -> {
            if (e.getType().getCategory() == MobCategory.MONSTER && !Blacklist.isBlacklisted(e) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName())) {
                hostiles.add(e);
            }
        }));

        if (hostiles.size() > 0) {
            hostiles.forEach(hostile -> {
                if (hostile.getType().getCategory() == MobCategory.MONSTER && !Blacklist.isBlacklisted(hostile) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !hostile.hasCustomName())) {

                    hostile.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
            hostiles.clear();
        }
        respond(context);
        return 1;
    }

    private static int removeAmbient(CommandContext<CommandSourceStack> context) {
        counter = 0;
        List<Entity> Ambient = new ArrayList<>();
        List<ServerLevel> worlds = CommandUtils.getWorlds(context);
        worlds.forEach(world -> world.getEntities().getAll().forEach(e -> {
            if (e.getType().getCategory() == MobCategory.AMBIENT && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !e.hasCustomName()))
                Ambient.add(e);
        }));

        if (Ambient.size() > 0) {
            Ambient.forEach(a -> {
                if (a.getType().getCategory() == MobCategory.MONSTER && !Blacklist.isBlacklisted(a) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !a.hasCustomName())) {
                    a.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
            Ambient.clear();
        }

        respond(context);
        return 1;
    }

    private static int removeCreature(CommandContext<CommandSourceStack> context) {
        counter = 0;
        List<Entity> Creature = new ArrayList<>();
        List<ServerLevel> worlds = CommandUtils.getWorlds(context);
        worlds.forEach(world -> world.getEntities().getAll().forEach(e -> {
            if (e.getType().getCategory() == MobCategory.CREATURE && !e.hasCustomName())
                Creature.add(e);
        }));

        if (Creature.size() > 0) {
            Creature.forEach(c -> {
                if (c.getType().getCategory() == MobCategory.CREATURE && !Blacklist.isBlacklisted(c) && (LagRemoval.LRConfig.PreventNamedEntityRemoval() || !c.hasCustomName())) {
                    c.remove(Entity.RemovalReason.DISCARDED);
                    counter++;
                }
            });
            Creature.clear();
        }
        respond(context);
        return 1;
    }

    private static void respond(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(new TextComponent(ChatFormatting.BLUE + "[]" + counter + " Entities"), true);
    }
}