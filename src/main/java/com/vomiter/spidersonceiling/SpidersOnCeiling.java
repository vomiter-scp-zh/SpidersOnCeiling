package com.vomiter.spidersonceiling;

import com.mojang.logging.LogUtils;
import com.vomiter.spidersonceiling.common.event.EventHandler;
import com.vomiter.spidersonceiling.data.ModDataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.UUID;

@Mod(SpidersOnCeiling.MOD_ID)
public class SpidersOnCeiling
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "spidersonceiling";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final UUID SPIDER_ON_CEILING_GRAVITY_MODIFIER_UUID
            = UUID.fromString("800e821d-6744-4539-926a-f5e886dc0ca0");

    public static AttributeModifier SPIDER_ON_CEILING_GRAVITY_MODIFIER
            = new AttributeModifier(
            SPIDER_ON_CEILING_GRAVITY_MODIFIER_UUID,
            "spider_on_ceiling_gravity_mod",
            -0.16,
            AttributeModifier.Operation.ADDITION
    );


    public static ResourceLocation modLoc(String path){
        return Helpers.id(SpidersOnCeiling.MOD_ID, path);
    }

    public SpidersOnCeiling(FMLJavaModLoadingContext context) {
        EventHandler.init();
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(ModDataGenerator::generateData);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

}
