package com.vomiter.spidersonceiling.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vomiter.spidersonceiling.common.SpidersOnCeilingUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobRenderer.class)
public abstract class SpiderRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/world/entity/Mob;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At("HEAD")
    )
    private void soc$flipSpiderOnCeiling(
            Mob mob,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci
    ) {
        if(mob instanceof Spider spider && SpidersOnCeilingUtils.canChangeToCeilingMode(spider)) {
            poseStack.translate(0.0D, spider.getBbHeight(), 0.0D);
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        }

        if(mob.getVehicle() instanceof Spider vehicleSpider && SpidersOnCeilingUtils.canChangeToCeilingMode(vehicleSpider)){
            poseStack.translate(0.0D, mob.getBbHeight() + vehicleSpider.getBbHeight() * 0.5, 0.0D);
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        }
    }


}