package com.github.xniter.commands;


import com.github.xniter.data.Blacklist;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class Reload {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("reload")
                .executes(commandContext -> {
                    try {
                        Blacklist.loadList();
                        (commandContext.getSource()).sendSuccess(new TextComponent(ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.GREEN + "Reloaded"), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 1;
                });
    }
}

