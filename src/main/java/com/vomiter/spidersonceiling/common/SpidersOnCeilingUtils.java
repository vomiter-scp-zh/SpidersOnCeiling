package com.vomiter.spidersonceiling.common;

import com.vomiter.spidersonceiling.SpidersOnCeiling;
import com.vomiter.spidersonceiling.common.entity.ai.ISpiderStateDuck;
import com.vomiter.spidersonceiling.common.entity.ai.SpiderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.AABB;

public class SpidersOnCeilingUtils {

    private static final double EPS = 0.3D;

    public static boolean isCeilingMode(PathfinderMob spider){
        if (spider instanceof ISpiderStateDuck d){
            return d.soc$getState().equals(SpiderState.CEILING);
        }
        return false;
    }

    public static boolean canChangeToCeilingMode(PathfinderMob spider) {
        return canChangeToCeilingMode(spider, false);
    }

    public static boolean canChangeToCeilingMode(PathfinderMob spider, boolean log) {
        var level = spider.level();
        AABB box = spider.getBoundingBox();

        int minX = BlockPos.containing(box.minX + EPS, box.minY, box.minZ + EPS).getX();
        int maxX = BlockPos.containing(box.maxX - EPS, box.minY, box.maxZ - EPS).getX();
        int minZ = BlockPos.containing(box.minX + EPS, box.minY, box.minZ + EPS).getZ();
        int maxZ = BlockPos.containing(box.maxX - EPS, box.minY, box.maxZ - EPS).getZ();

        int ceilingY = BlockPos.containing(spider.getX(), box.maxY + EPS, spider.getZ()).getY();
        int floorY = BlockPos.containing(spider.getX(), box.minY - EPS, spider.getZ()).getY();

        boolean hasCeiling = false;
        boolean hasFloor = false;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos ceilingPos = new BlockPos(x, ceilingY, z);
                if (level.getBlockState(ceilingPos).isFaceSturdy(level, ceilingPos, Direction.DOWN)) {
                    hasCeiling = true;
                }

                BlockPos floorPos = new BlockPos(x, floorY, z);
                if (level.getBlockState(floorPos).isFaceSturdy(level, floorPos, Direction.UP)) {
                    hasFloor = true;
                }
            }
        }

        if (log && spider.tickCount % 20 == 0) {
            SpidersOnCeiling.LOGGER.info(
                    "[SOC] has ceiling = {}, has floor = {}",
                    hasCeiling,
                    hasFloor
            );
        }

        return hasCeiling && !hasFloor;
    }
}