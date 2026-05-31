package com.vomiter.spidersonceiling;

import com.mojang.logging.LogUtils;
import com.vomiter.spidersonceiling.common.event.EventHandler;
import com.vomiter.spidersonceiling.common.registry.ModRegistries;
import com.vomiter.spidersonceiling.data.ModDataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SpidersOnCeiling.MOD_ID)
public class SpidersOnCeiling
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "spidersonceiling";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation modLoc(String path){
        return Helpers.id(SpidersOnCeiling.MOD_ID, path);
    }

    public SpidersOnCeiling(FMLJavaModLoadingContext context) {
        EventHandler.init();
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(ModDataGenerator::generateData);
        ModRegistries.register(modBus);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

}
