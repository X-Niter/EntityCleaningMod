package com.github.xniter.util;

import com.github.xniter.LagRemoval;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
    public static final int PERMISSION_LEVEL = 2;
    private static final Logger LOGGER = LogManager.getLogger();

    public static void sendNormalMessage(CommandSourceStack source, String msg, ChatFormatting color) {
        MutableComponent text = new TextComponent(msg);
        Style style = Style.EMPTY;
        text = text.setStyle(style);
        text.withStyle(color);
        source.sendSuccess(text, true);
    }

    public static void sendChunkEntityMessage(CommandSourceStack source, int count, BlockPos pos, ResourceKey<Level> type, boolean runDirectly) {
        MutableComponent text = new TextComponent("- " + pos.toString()).withStyle(ChatFormatting.GREEN);
        text.append(coloredComponent(" Count ", ChatFormatting.RED));
        text.append(coloredComponent(Integer.toString(count), ChatFormatting.GREEN));
        ServerPlayer player = null;
        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {

        }
        sendCommandMessage(source, text, "/lr tp " + (player != null ? player.getName().getString() : "Console") + " " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " " + type.location(), runDirectly);
    }

    public static List<ServerLevel> getWorlds(CommandContext<CommandSourceStack> context) {
        ServerLevel world = null;
        try {
            world = DimensionArgument.getDimension(context, "dim");
        } catch (IllegalArgumentException | CommandSyntaxException e) {
            //NO OP
        }
        List<ServerLevel> worlds = new ArrayList<>();
        if (world == null) {

            LagRemoval.worldsGlobal.forEach(worlds::add);
        } else {
            worlds.add(world);
        }
        return worlds;
    }

    public static void sendFindEMessage(CommandSourceStack source, ResourceLocation res, int count) {
        MutableComponent text = new TextComponent(String.valueOf(count)).withStyle(ChatFormatting.BLUE);
        text.append(new TextComponent("x ").withStyle(ChatFormatting.YELLOW));
        text.append(new TextComponent(res.toString()).withStyle(ChatFormatting.AQUA));
        sendCommandMessage(source, text, "/lr entities find " + res.toString(), true);

    }

    public static MutableComponent coloredComponent(String text, ChatFormatting color) {
        return new TextComponent(text).withStyle(color);
    }

    public static void sendCommandMessage(CommandSourceStack source, MutableComponent text, String command, boolean runDirectly) {

        Style style = text.getStyle();
        ClickEvent click = new ClickEvent(runDirectly ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND, command);
        style = style.applyTo(style.withClickEvent(click));

        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("Click to execute \u00A76" + command + "\u00A7r"));
        style = style.applyTo(style.withHoverEvent(hoverEvent));
        MutableComponent tex = text.setStyle(style);
        source.sendSuccess(tex, false);
        LOGGER.info(text.getString() + " " + command);
    }
}
