package com.vomiter.spidersonceiling.common.entity.ai;

import com.vomiter.neurolib.common.entity.generic.MutatedMeleeGoal;
import com.vomiter.spidersonceiling.SpidersOnCeiling;
import com.vomiter.spidersonceiling.common.SpidersOnCeilingUtils;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SpiderAttackGoalWrap extends MutatedMeleeGoal {
    MeleeAttackGoal basic;
    public SpiderAttackGoalWrap(MeleeAttackGoal basicGoal) {
        super(basicGoal);
        basic = basicGoal;
        this.setExtraUseCheck(goal -> !SpidersOnCeilingUtils.isCeilingMode(mob));
        this.setExtraContinueCheck(goal -> !SpidersOnCeilingUtils.isCeilingMode(mob));
    }

    @Override
    public void start(){
        basic.start();
    }

    @Override
    public void stop() {
        basic.stop();

    }

    @Override
    public void tick() {
        basic.tick();
    }



}
