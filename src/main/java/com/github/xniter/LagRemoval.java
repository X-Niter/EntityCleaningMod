package com.github.xniter;

import com.github.xniter.commands.Blacklisting;
import com.github.xniter.commands.Reload;
import com.github.xniter.commands.entitycommands.ListAllEntities;
import com.github.xniter.commands.entitycommands.RemoveEntities;
import com.github.xniter.commands.events.StrictLagRemoval;
import com.github.xniter.commands.worldcommand.ListWorlds;
import com.github.xniter.config.Config;
import com.github.xniter.data.Blacklist;
import com.github.xniter.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
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

    public static final Config CONFIG = new Config();
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
        MinecraftForge.EVENT_BUS.register(StrictLagRemoval.class);
        EventBuses.registerModEventBus(LagRemoval.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Config.loadConfig();

        try {
            Blacklist.loadList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("LagRemoval Loaded, loading config files");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(LiteralArgumentBuilder.<CommandSourceStack>literal(MODID)
                .requires(x -> x.hasPermission(CommandUtils.PERMISSION_LEVEL))
                .then(ListAllEntities.register())
                .then(RemoveEntities.register())
                .then(Blacklisting.register())
                .then(Reload.register())
                .then(ListWorlds.register())

        );
        dispatcher.register(Commands.literal("lr")
                .requires(x -> x.hasPermission(CommandUtils.PERMISSION_LEVEL))
                .redirect(cmd)
        );

        LOGGER.info("Registered 5 Command Nodes");

    }

    @SubscribeEvent
    public void ServerStarted(ServerStartedEvent event) {
        event.getServer().getAllLevels().forEach(serverLevel -> worldsGlobal.add(serverLevel));
        isServer = true;
    }

    static Random rand = new Random();

    public static void sendToAll(MinecraftServer server, String msg) {
        for (int i = 0; i < server.getPlayerList().getPlayers().size(); i++) {
            ServerPlayer serverplayerentity = server.getPlayerList().getPlayers().get(i);
            serverplayerentity.sendMessage(new TextComponent(msg), ChatType.CHAT, Util.NIL_UUID);
        }
    }
}

