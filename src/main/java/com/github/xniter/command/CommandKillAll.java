package com.github.xniter.command;

import com.github.xniter.LagRemoval;
import com.github.xniter.config.Config;
import com.github.xniter.data.Blacklist;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandKillAll {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("killall").requires(player -> player.hasPermission(3))

                .executes(commandContext -> {
                    int i = killAll((commandContext.getSource()).getServer());
                    return 1;
                }));
    }

    public static int killAll(MinecraftServer server) {
        AtomicInteger ordinal = new AtomicInteger(0);
        for (ServerLevel world : server.getAllLevels()) {
            world.getAllEntities().forEach(e -> {
                if (!(e instanceof net.minecraft.server.level.ServerPlayer))
                    if (e instanceof ItemEntity) {
                        ItemStack item = ((ItemEntity) e).getItem();
                        boolean kill = (item == null || (item != null && item.getItem() == null) || (item != null && item.isEmpty()));
                        if ((kill || item == null || !Blacklist.isBlacklisted(item.getItem())) && (!Config.INSTANCE.prevent_named_entity_removal || !e.hasCustomName())) {
                            e.kill();
                            System.out.println("LagRemoval Kill Event Fired!");
                            ordinal.getAndIncrement();
                        }
                    } else if (!Blacklist.isBlacklisted(e) && (!Config.INSTANCE.prevent_named_entity_removal || !e.hasCustomName())) {
                        e.kill();
                        ordinal.getAndIncrement();
                    }
            });
        }
        LagRemoval.sendToAll(server, ChatFormatting.GREEN + "[Lag Removal] Removed " + ChatFormatting.GREEN + " entities");
        return ordinal.get();
    }
}