package com.vomiter.spidersonceiling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.neurolib.common.entity.generic.GoalMutateUtils;
import com.vomiter.spidersonceiling.SpidersOnCeiling;
import com.vomiter.spidersonceiling.common.SpidersOnCeilingUtils;
import com.vomiter.spidersonceiling.common.entity.ai.SpiderAttackGoalWrap;
import com.vomiter.spidersonceiling.common.entity.ai.SpiderAttackOnCeilingGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(Spider.class)
public abstract class SpiderMixin extends Monster {
    protected SpiderMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Spider;setClimbing(Z)V"))
    private void soc$modifyClimbing(Spider instance, boolean b, Operation<Void> original){
        if(!SpidersOnCeilingUtils.isCeilingMode(this)){
            original.call(instance, b);
        }
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void soc$modifyGoals(CallbackInfo ci) {
        GoalMutateUtils.replaceAllMeleeWithMutated(
                this.goalSelector,
                SpiderAttackGoalWrap::new,
                new ArrayList<>()
        );

        goalSelector.addGoal(
                0,
                new SpiderAttackOnCeilingGoal((Spider)(Object)this, 1.0D)
        );
    }


    @Inject(method = "tick", at = @At("TAIL"))
    private void soc$debugLog(CallbackInfo ci){
        //SpidersOnCeiling.LOGGER.info("[SOC] Ceiling Mode = {}", SpidersOnCeilingUtils.isCeilingMode((Spider)(Object)this));
    }

    @Inject(
            method = "getPassengersRidingOffset",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sd$positionCeilingSkeletonRider(
            CallbackInfoReturnable<Double> cir
    ) {
        if(!SpidersOnCeilingUtils.isCeilingMode(this)) return;
        Entity entity = getFirstPassenger();
        if(entity != null) cir.setReturnValue(entity.getBbHeight() * -0.5);
    }
}
