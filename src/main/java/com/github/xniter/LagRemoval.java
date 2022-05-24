package com.github.xniter;

import com.github.xniter.commands.Blacklisting;
import com.github.xniter.commands.Reload;
import com.github.xniter.commands.entitycommands.ListAllEntities;
import com.github.xniter.commands.entitycommands.RemoveEntities;
import com.github.xniter.commands.events.LimiterClearEvent;
import com.github.xniter.commands.events.StrictClearEvent;
import com.github.xniter.commands.worldcommand.ListWorlds;
import com.github.xniter.config.CommonConfig;
import com.github.xniter.data.Blacklist;
import com.github.xniter.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.architectury.platform.forge.EventBuses;
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
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
    public static final CommonConfig LRConfig = new CommonConfig();
    public static boolean isServer = false;

    public static List<ServerLevel> worldsGlobal = new ArrayList<>();

    public LagRemoval() {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new StrictClearEvent());
        MinecraftForge.EVENT_BUS.register(new LimiterClearEvent());
        EventBuses.registerModEventBus(LagRemoval.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modLoadingContext.registerConfig(ModConfig.Type.SERVER, LRConfig.getSpec(), "Lag Removal/lagremoval-server.toml");

        try {
            Blacklist.loadList();
        } catch (Exception e) {
            e.printStackTrace();
        }


        LOGGER.info("Initializing LagRemoval");
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
        LOGGER.info("LagRemoval Successfully Loaded");
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

