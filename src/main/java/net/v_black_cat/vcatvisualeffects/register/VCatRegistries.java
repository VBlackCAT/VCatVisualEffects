package net.v_black_cat.vcatvisualeffects.register;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.v_black_cat.vcatvisualeffects.visual.EntityVisualEffectType;

import static net.v_black_cat.vcatvisualeffects.VCatVisualEffectsMod.MODID;

public class VCatRegistries {


    public static final ResourceKey<Registry<EntityVisualEffectType>> ENTITY_VISUAL_EFFECT_TYPE_KEY =
            ResourceKey.createRegistryKey(new ResourceLocation(MODID, "entity_visual_effect_types"));

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
    public static class VCatRegistriesEvents {
        @SubscribeEvent
        public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        }
    }
}
