package com.github.xniter.commands.entitycommands;

import com.github.xniter.data.EntityData;
import com.github.xniter.util.CommandUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class ListAllEntities implements Command<CommandSourceStack> {
    private static final ListAllEntities cmd = new ListAllEntities();

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("list")
                .then(Commands.argument("dim", DimensionArgument.dimension())
                        .executes(cmd))
                .executes(cmd);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        EntityData list = new EntityData();
        List<ServerLevel> worlds = CommandUtils.getWorlds(context);
        list.createLists(worlds);
        list.reply(null, context.getSource());
        return Command.SINGLE_SUCCESS;
    }
}
