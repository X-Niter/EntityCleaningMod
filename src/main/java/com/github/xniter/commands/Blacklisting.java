package com.github.xniter.commands;

import com.github.xniter.data.Blacklist;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Blacklisting {
    private static final SuggestionProvider<CommandSourceStack> tabCompleteEntities = (ctx, builder) -> SharedSuggestionProvider.suggestResource(ForgeRegistries.ENTITIES.getKeys().stream(), builder);
    private static final SuggestionProvider<CommandSourceStack> tabCompleteItems = (ctx, builder) -> SharedSuggestionProvider.suggestResource(ForgeRegistries.ITEMS.getKeys().stream(), builder);

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("blacklist")
                .then(Commands.literal("add")
                        .then(Commands.literal("entity")
                                .then(Commands.argument("entity", ResourceLocationArgument.id())
                                        .suggests(tabCompleteEntities)
                                        .executes(ctx -> {
                                            ResourceLocation name = ResourceLocationArgument.getId(ctx, "entity");
                                            try {
                                                Blacklist.addToBlacklist(name.toString());
                                                ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + name.getNamespace() + ChatFormatting.GOLD + " Added to blacklist"), true);
                                                return 1;
                                            } catch (Exception error) {
                                                ctx.getSource().sendFailure(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + " Entity Not Valid"));
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("item")
                                .then(Commands.argument("item", ResourceLocationArgument.id())
                                        .suggests(tabCompleteItems)
                                        .executes(ctx -> {
                                            ResourceLocation item = ResourceLocationArgument.getId(ctx, "item");
                                            try {
                                                Blacklist.addToBlacklist(item.toString());
                                                ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + item.getNamespace() + ChatFormatting.GOLD + " Added to blacklist"), true);
                                                return 1;
                                            } catch (Exception error) {
                                                ctx.getSource().sendFailure(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + " Item Not Valid"));
                                            }
                                            return 0;
                                        })
                                )
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.literal("entity")
                                .then(Commands.argument("entity", ResourceLocationArgument.id())
                                        .suggests(tabCompleteEntities)
                                        .executes(ctx -> {
                                            ResourceLocation name = ResourceLocationArgument.getId(ctx, "entity");
                                            try {
                                                Blacklist.removeFromBlacklist(String.valueOf(name));
                                                ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + name.getNamespace() + ChatFormatting.GOLD + " Removed from blacklist"), true);
                                                return 1;
                                            } catch (Exception error) {
                                                ctx.getSource().sendFailure(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + " Entity not found, nothing to remove"));
                                            }
                                            return 0;
                                        })
                                )
                        )
                        .then(Commands.literal("item")
                                .then(Commands.argument("item", ResourceLocationArgument.id())
                                        .suggests(tabCompleteItems)
                                        .executes(ctx -> {
                                            ResourceLocation item = ResourceLocationArgument.getId(ctx, "item");
                                            try {
                                                Blacklist.removeFromBlacklist(item.toString());
                                                ctx.getSource().sendSuccess(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + item.getNamespace() + ChatFormatting.GOLD + " Removed from blacklist"), true);
                                                return 1;
                                            } catch (Exception error) {
                                                ctx.getSource().sendFailure(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + ChatFormatting.RED + " Item not found, nothing to remove"));
                                            }
                                            return 0;
                                        })
                                )
                        )
                );

    }}
