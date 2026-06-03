package com.vomiter.spidersonceiling;

import com.mojang.logging.LogUtils;
import com.vomiter.spidersonceiling.common.event.EventHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

import java.util.UUID;

@Mod(SpidersOnCeiling.MOD_ID)
public class SpidersOnCeiling
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "spidersonceiling";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation SPIDER_ON_CEILING_GRAVITY_MODIFIER_ID
            = modLoc("spider_on_ceiling_gravity_mod");

    public static AttributeModifier SPIDER_ON_CEILING_GRAVITY_MODIFIER
            = new AttributeModifier(
                    SPIDER_ON_CEILING_GRAVITY_MODIFIER_ID,
            -0.16,
            AttributeModifier.Operation.ADD_VALUE
    );


    public static ResourceLocation modLoc(String path){
        return Helpers.id(SpidersOnCeiling.MOD_ID, path);
    }

    public SpidersOnCeiling(ModContainer mod, IEventBus modBus) {
        EventHandler.init();
        modBus.addListener(this::commonSetup);
        mod.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

}
