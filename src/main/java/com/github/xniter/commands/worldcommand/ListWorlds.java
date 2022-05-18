package com.github.xniter.commands.worldcommand;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ListWorlds {

        public static ArgumentBuilder<CommandSourceStack, ?> register() {
            return Commands.literal("worlds")
                    .executes(commandContext -> {
                        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                        commandContext.getSource().sendSuccess(new TextComponent(ChatFormatting.BLUE + "[Lag Removal]" + server.forgeGetWorldMap().keySet().stream().toList()), true);
                        return 1;
                    });
        }
}
