package com.vomiter.spidersonceiling.common.entity.ai;

import com.vomiter.spidersonceiling.SpidersOnCeiling;
import com.vomiter.spidersonceiling.common.SpidersOnCeilingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class SpiderAttackOnCeilingGoal extends Goal {

    private final Spider spider;
    private final double speed;
    private int repathCooldown;
    private int failureCount;
    private final int failureCountMax = 40;

    public SpiderAttackOnCeilingGoal(Spider spider, double speed) {
        this.spider = spider;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = spider.getTarget();
        return target != null
                && target.isAlive()
                && SpidersOnCeilingUtils.isCeilingMode(spider);
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = spider.getTarget();
        return target != null
                && target.isAlive()
                && SpidersOnCeilingUtils.isCeilingMode(spider);
    }

    @Override
    public void start() {
        failureCount = 0;
        repathCooldown = 5;
    }

    @Override
    public void stop() {
        failureCount = 0;
        repathCooldown = 5;
    }


    @Override
    public void tick() {
        LivingEntity target = spider.getTarget();
        if (target == null) {
            return;
        }

        spider.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (--repathCooldown <= 0) {
            repathCooldown = 5;

            BlockPos targetBase = target.blockPosition().above();
            BlockPos ceiling = findCeilingAbove(spider.level(), targetBase, 8);
            //SpidersOnCeiling.LOGGER.info("[SOC] ceiling target = {}", ceiling);


            if (ceiling != null) {
                spider.getNavigation().moveTo(
                        ceiling.getX() + 0.5D,
                        ceiling.getY() - spider.getBbHeight() + 0.05D,
                        ceiling.getZ() + 0.5D,
                        speed
                );
            } else {
                ++ failureCount;
            }
        }

        double horizontalDistSqr =
                spider.distanceToSqr(target.getX(), spider.getY(), target.getZ());

        if (horizontalDistSqr < 2.5D || failureCount > failureCountMax) {
            stop();
        }
    }

    public static BlockPos findCeilingAbove(Level level, BlockPos origin, int maxUp) {
        BlockPos.MutableBlockPos mutable = origin.mutable();

        for (int i = 0; i <= maxUp; i++) {
            BlockPos pos = mutable.set(origin.getX(), origin.getY() + i, origin.getZ());
            BlockState state = level.getBlockState(pos);

            if (state.isFaceSturdy(level, pos, Direction.DOWN)) {
                return pos.immutable();
            }
        }

        return null;
    }
}