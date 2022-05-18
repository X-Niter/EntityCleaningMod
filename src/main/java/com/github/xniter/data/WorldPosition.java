package com.github.xniter.data;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;

import java.util.UUID;

public class WorldPosition {
    public BlockPos pos;
    public ResourceKey<Level> type;
    public UUID id;

    public WorldPosition(BlockPos pos, ResourceKey<Level> type, UUID id) {
        this.pos = pos;
        this.type = type;
        this.id = id;
    }

    public static WorldPosition getPosFromEntity(Entity entity) {
        return new WorldPosition(new BlockPos(entity.position()), entity.getCommandSenderWorld().dimension(), entity.getUUID());
    }

    public static WorldPosition getPosFromTileEntity(TickingBlockEntity entity, Level level) {
        return new WorldPosition(entity.getPos(), level.dimension(), UUID.randomUUID());
    }

    public UUID getID() {
        return id;
    }
}
