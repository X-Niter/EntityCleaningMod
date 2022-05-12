package com.github.xniter.command;

import com.github.xniter.data.Blacklist;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class CommandBlacklistAdd {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lr-blacklist-add").requires(player -> player.hasPermission(3))

                .then(Commands.argument("entity-name", EntitySummonArgument.id()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(commandContext -> {
                    ResourceLocation name = EntitySummonArgument.getSummonableEntity(commandContext, "entity-name");
                    if (name != null) {
                        (commandContext.getSource()).sendSuccess(new TextComponent(ChatFormatting.GREEN + "[Lag Removal] Added entity to blacklist"), true);
                        Blacklist.addToBlacklist(name.toString());
                        return 1;
                    }
                    (commandContext.getSource()).sendFailure(new TextComponent(ChatFormatting.RED + "[Lag Removal] Entity not found"));
                    return 0;
                }))
                .then(Commands.argument("item-name", ItemArgument.item()).executes(commandContext -> {
            ItemInput item = ItemArgument.getItem(commandContext, "item-name");
            if (item != null) {
                (commandContext.getSource()).sendSuccess(new TextComponent(ChatFormatting.GREEN + "[Lag Removal] Added item to blacklist"), true);
                Blacklist.addToBlacklist(item.getItem().getRegistryName().toString());
                return 1;
            }
            (commandContext.getSource()).sendFailure(new TextComponent(ChatFormatting.RED + "[Lag Removal] Item not found"));
            return 0;
        })));
    }
}
