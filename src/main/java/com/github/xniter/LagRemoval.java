package com.github.xniter;

import com.github.xniter.commands.CommandReloadBlacklist;
import com.github.xniter.commands.entitycommands.Blacklisting;
import com.github.xniter.commands.entitycommands.EntitiesCommands;
import com.github.xniter.commands.entitycommands.ListAllEntities;
import com.github.xniter.commands.entitycommands.RemoveEntities;
import com.github.xniter.config.Config;
import com.github.xniter.data.Blacklist;
import com.github.xniter.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LagRemoval.MODID)
public class LagRemoval {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "lagremoval";
    public static boolean isServer = false;
    public long l1;

    private static int counter = 0;

    public int seconds;

    public boolean w60;

    public boolean w30;

    public boolean w5;
    public boolean w3;
    public boolean w2;
    public boolean w1;

    public static List<ServerLevel> worldsGlobal = new ArrayList<>();

    public LagRemoval() {
        MinecraftForge.EVENT_BUS.register(this);
        EventBuses.registerModEventBus(LagRemoval.MODID, FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal(MODID)
                .requires(x -> x.hasPermission(CommandUtils.PERMISSION_LEVEL))
                .then(ListAllEntities.register())
                .then(RemoveEntities.register())
                .then(Blacklisting.register())

        );
        dispatcher.register(Commands.literal("lr")
                .requires(x -> x.hasPermission(CommandUtils.PERMISSION_LEVEL))
                .redirect(cmd)
        );

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        CommandReloadBlacklist.register(event.getServer().getCommands().getDispatcher());
        event.getServer().getAllLevels().forEach(serverLevel -> worldsGlobal.add(serverLevel));
        try {
            com.github.xniter.config.Config.loadConfig();
            Blacklist.loadList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void ServerStarted(ServerStartedEvent event) {
        isServer = true;
    }

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (this.l1 != System.currentTimeMillis() / 1000) {
            this.l1 = System.currentTimeMillis() / 1000;
            this.seconds++;
        }
        if (Config.INSTANCE.show_warning) {
            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 - 30 && !this.w30) {
                sendToAll(server, "" + ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.GREEN + "Entities will be removed in 30 seconds");
                this.w30 = true;
            }
            if (this.seconds == Config.INSTANCE.auto_clear_delay * 60 - 29) {
                this.w30 = false;
            }


            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 - 5 && !this.w5) {
                sendToAll(server, "" + ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.GREEN + "Entities will be removed in" + ChatFormatting.RED + " 5 " + ChatFormatting.GREEN + "seconds");
                this.w5 = true;
            }
            if (this.seconds == Config.INSTANCE.auto_clear_delay * 60 - 4) {
                this.w5 = false;
            }

            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 - 3 && !this.w3) {
                sendToAll(server, "" + ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.GREEN + "Entities will be removed in" + ChatFormatting.RED + " 3 " + ChatFormatting.GREEN + "seconds");
                this.w3 = true;
            }
            if (this.seconds == Config.INSTANCE.auto_clear_delay * 60 - 2.9) {
                this.w3 = false;
            }


            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 - 2 && !this.w2) {
                sendToAll(server, "" + ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.GREEN + "Entities will be removed in" + ChatFormatting.RED + " 2 " + ChatFormatting.GREEN + "seconds");
                this.w2 = true;
            }
            if (this.seconds == Config.INSTANCE.auto_clear_delay * 60 - 1.9) {
                this.w2 = false;
            }

            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 - 1 && !this.w1) {
                sendToAll(server, "" + ChatFormatting.BLUE + "[Lag Removal] " + ChatFormatting.GREEN + "Entities will be removed in" + ChatFormatting.RED + " 1 " + ChatFormatting.GREEN + "second");
                this.w1 = true;
            }
            if (this.seconds == Config.INSTANCE.auto_clear_delay * 60 - 0.9) {
                this.w1 = false;
            }

            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 && !this.w60) {
                server.getCommands().performCommand(server.createCommandSourceStack(),"lr clear all");
                this.w60 = true;
                this.seconds = 0;
            }

        }
    }

    static Random rand = new Random();

    public static void sendToAll(MinecraftServer server, String msg) {
        for (int i = 0; i < server.getPlayerList().getPlayers().size(); i++) {
            ServerPlayer serverplayerentity = server.getPlayerList().getPlayers().get(i);
            serverplayerentity.sendMessage(new TextComponent(msg), ChatType.CHAT, Util.NIL_UUID);
        }
    }
}

