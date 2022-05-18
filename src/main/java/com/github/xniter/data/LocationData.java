package com.github.xniter.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LocationData {
    Map<ChunkPos, List<WorldPosition>> chunkMap = new HashMap<>();
    Map<ChunkPos, WorldPosition> tpPos = new HashMap<>();
    Map<ResourceLocation, List<WorldPosition>> map = new HashMap<>();
    int total = 0;
    String lastFill = "";

    public Map<ResourceLocation, List<WorldPosition>> getMap() {
        return map;
    }

    public void fillChunkMaps(String rl) {
        if (lastFill.equals(rl)) return;
        lastFill = rl;
        ResourceLocation resourceLocation = new ResourceLocation(rl);
        chunkMap.clear();
        tpPos.clear();
        List<WorldPosition> positions = new ArrayList<>();
        if (rl.isEmpty()) {
            map.values().forEach(positions::addAll);
        } else {
            positions.addAll(map.get(resourceLocation));
        }
        for (WorldPosition pos : positions) {
            ChunkPos chunkPos = new ChunkPos(pos.pos);
            if (!chunkMap.containsKey(chunkPos)) {
                chunkMap.put(chunkPos, new ArrayList<>());
            }
            chunkMap.get(chunkPos).add(pos);
            tpPos.put(chunkPos, pos);
        }
    }

    public int getCountForChunk(ChunkPos chunkPos, String filter) {
        if (chunkMap == null) return 0;
        fillChunkMaps(filter);
        if (chunkMap.get(chunkPos) == null) return 0;
        return chunkMap.get(chunkPos).size();
    }
}
