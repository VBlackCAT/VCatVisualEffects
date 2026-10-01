package net.v_black_cat.vcatvisualeffects;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.v_black_cat.vcatvisualeffects.config.VCatConfig;
import net.v_black_cat.vcatvisualeffects.network.NetworkHandler;
import net.v_black_cat.vcatvisualeffects.visual.VCatVisualEffects;
import org.slf4j.Logger;

/**
 * V猫的视觉特效 / VCat Visual Effects.
 *
 * <p>A framework for attaching, syncing and rendering composable visual effects on any entity: a
 * per-entity capability holding any number of registered visual effects, synced to tracking
 * clients and rendered either in world space (custom core shaders) or as a full-screen
 * screen-space pass ({@code entity_depth_reconstruct} post chain).</p>
 */
// The value here should match an entry in the META-INF/mods.toml file
@Mod(VCatVisualEffectsMod.MODID)
public class VCatVisualEffectsMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "vcat_visual_effects";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public VCatVisualEffectsMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        VCatVisualEffects.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(
                ModConfig.Type.COMMON,
                VCatConfig.SPEC,
                "vcat_visual_effects-common.toml"
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NetworkHandler.register();
    }
}
