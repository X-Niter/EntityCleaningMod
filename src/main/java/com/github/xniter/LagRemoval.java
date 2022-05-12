package com.github.xniter;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.github.xniter.command.CommandBlacklistAdd;
import com.github.xniter.command.CommandBlacklistRemove;
import com.github.xniter.command.CommandKillAll;
import com.github.xniter.command.CommandReloadBlacklist;
import com.github.xniter.data.Blacklist;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("lagremoval")
public class LagRemoval {
    public long l1;

    public int seconds;

    public boolean w30;

    public boolean w5;

    public LagRemoval() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        CommandKillAll.register(event.getServer().getCommands().getDispatcher());
        CommandBlacklistAdd.register(event.getServer().getCommands().getDispatcher());
        CommandBlacklistRemove.register(event.getServer().getCommands().getDispatcher());
        CommandReloadBlacklist.register(event.getServer().getCommands().getDispatcher());
        try {
            com.github.xniter.config.Config.loadConfig();
            Blacklist.loadList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void serverTickEvent(TickEvent.ServerTickEvent event) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (this.l1 != System.currentTimeMillis() / 1000L) {
            this.l1 = System.currentTimeMillis() / 1000L;
            this.seconds++;
        }
        if (com.github.xniter.config.Config.INSTANCE.show_warning) {
            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 - 30 && !this.w30) {
                server.addTickable(() -> sendToAll(server, "" + ChatFormatting.GREEN + "[Lag Removal] Entities will be removed in 30 seconds"));
                this.w30 = true;
            }
            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60 - 5 && !this.w5) {
                server.addTickable(() -> sendToAll(server, "" + ChatFormatting.GREEN + "[Lag Removal] Entities will be removed in 5 seconds"));
                this.w5 = true;
            }
            if (this.seconds == com.github.xniter.config.Config.INSTANCE.auto_clear_delay * 60) {
                server.addTickable(() -> CommandKillAll.killAll(server));
                this.seconds = 0;
                this.w30 = false;
                this.w5 = false;
            }
        }
    }

    static Random rand = new Random();

    public static void sendToAll(MinecraftServer server, String msg) {
        for (int i = 0; i < server.getPlayerList().getPlayers().size(); i++) {
            ServerPlayer serverplayerentity = server.getPlayerList().getPlayers().get(i);
            serverplayerentity.sendMessage(new TextComponent(msg), ChatType.GAME_INFO, Util.NIL_UUID);
        }
    }
}

