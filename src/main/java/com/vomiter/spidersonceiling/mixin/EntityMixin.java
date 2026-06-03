package com.vomiter.spidersonceiling.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vomiter.spidersonceiling.SpidersOnCeiling;
import com.vomiter.spidersonceiling.common.entity.ai.ISpiderStateDuck;
import com.vomiter.spidersonceiling.common.entity.ai.SpiderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "getGravity", at = @At("RETURN"), cancellable = true)
    private void soc$modifyGravity(CallbackInfoReturnable<Double> cir){
        if((Object)this instanceof ISpiderStateDuck spider){
            if(spider.soc$getState().equals(SpiderState.CEILING)) cir.setReturnValue(cir.getReturnValueD() * -1);
        }
    }

    @WrapOperation(
            method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getPassengerRidingPosition(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 soc$modifyPassengerAttachment(Entity vehicle, Entity passenger, Operation<Vec3> original){
        if(vehicle.tickCount % 20 == 0){
            SpidersOnCeiling.LOGGER.info("[SOC] vehicle at {}; passenger at {}", vehicle.getEyePosition(), passenger.getEyePosition());
        }
        if(vehicle instanceof ISpiderStateDuck spider){
            if(spider.soc$getState().equals(SpiderState.CEILING)){
                return original.call(vehicle, passenger).add(0, -passenger.getBbHeight() + vehicle.getBbHeight() / 2, 0);
            } //
        }
        return original.call(vehicle, passenger);
    }

}
