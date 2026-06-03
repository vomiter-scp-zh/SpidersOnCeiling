package com.vomiter.spidersonceiling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.neurolib.common.entity.generic.GoalMutateUtils;
import com.vomiter.spidersonceiling.common.SpidersOnCeilingUtils;
import com.vomiter.spidersonceiling.common.entity.ai.ISpiderStateDuck;
import com.vomiter.spidersonceiling.common.entity.ai.SpiderAttackGoalWrap;
import com.vomiter.spidersonceiling.common.entity.ai.SpiderAttackOnCeilingGoal;
import com.vomiter.spidersonceiling.common.entity.ai.SpiderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

import static com.vomiter.spidersonceiling.SpidersOnCeiling.SPIDER_ON_CEILING_GRAVITY_MODIFIER;

@Mixin(Spider.class)
public abstract class SpiderMixin extends Monster implements ISpiderStateDuck {
    protected SpiderMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Unique
    private SpiderState state = SpiderState.VANILLA;
    public SpiderState soc$getState(){
        return state;
    };
    public void soc$setState(SpiderState state){
        this.state = state;
    };

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Spider;setClimbing(Z)V"))
    private void soc$modifyClimbing(Spider instance, boolean b, Operation<Void> original){
        if(soc$getState().equals(SpiderState.VANILLA)){
            original.call(instance, b);
            return;
        }
        original.call(instance, false);
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
    private void soc$changeState(CallbackInfo ci){
        if (soc$getState().equals(SpiderState.VANILLA) && SpidersOnCeilingUtils.canChangeToCeilingMode(this)){
            soc$setState(SpiderState.CEILING);
        }
        if (soc$getState().equals(SpiderState.FALL) && this.onGround()){
            soc$setState(SpiderState.VANILLA);
        }
        if (soc$getState().equals(SpiderState.CEILING) && !SpidersOnCeilingUtils.canChangeToCeilingMode(this)){
            soc$setState(SpiderState.VANILLA);
        }
    }
}
