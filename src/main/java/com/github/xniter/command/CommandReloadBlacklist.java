package com.github.xniter.command;


import com.github.xniter.config.Config;
import com.github.xniter.data.Blacklist;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class CommandReloadBlacklist {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lr-reload").requires(player -> player.hasPermission(3))

                .executes(commandContext -> {
                    try {
                        Config.loadConfig();
                        Blacklist.loadList();
                        (commandContext.getSource()).sendSuccess(new TextComponent(ChatFormatting.GREEN + "[Lag Removal] Reloaded"), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 1;
                }));
    }
}

