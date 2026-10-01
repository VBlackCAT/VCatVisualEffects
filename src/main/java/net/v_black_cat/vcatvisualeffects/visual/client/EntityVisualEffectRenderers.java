package net.v_black_cat.vcatvisualeffects.visual.client;

import net.minecraft.resources.ResourceLocation;
import net.v_black_cat.vcatvisualeffects.render.BackSigilEffectRenderer;
import net.v_black_cat.vcatvisualeffects.render.BlockCrackLightRenderer;
import net.v_black_cat.vcatvisualeffects.render.DepthVisualEffectRenderer;
import net.v_black_cat.vcatvisualeffects.render.OrbitingSphereRenderer;
import net.v_black_cat.vcatvisualeffects.render.PhantomRiftShardsRenderer;
import net.v_black_cat.vcatvisualeffects.render.PlayerHelixTrailRenderer;
import net.v_black_cat.vcatvisualeffects.render.RedEyeFlashRenderer;
import net.v_black_cat.vcatvisualeffects.render.SupremeChaosCosmosRenderer;
import net.v_black_cat.vcatvisualeffects.render.TiltedHaloRenderer;
import net.v_black_cat.vcatvisualeffects.render.VolumetricFlameRenderer;
import net.v_black_cat.vcatvisualeffects.visual.VCatVisualEffects;

import java.util.HashMap;
import java.util.Map;

public final class EntityVisualEffectRenderers {
    private static final Map<ResourceLocation, EntityVisualEffectRenderer> RENDERERS = new HashMap<>();

    private EntityVisualEffectRenderers() {
    }

    public static void registerDefaults() {
        register(VCatVisualEffects.ORBIT_SPHERE.getId(), OrbitingSphereRenderer::render);
        register(VCatVisualEffects.HELIX_TRAIL.getId(), PlayerHelixTrailRenderer::render);
        register(VCatVisualEffects.SOFT_TRAIL.getId(), DepthVisualEffectRenderer::renderSoftTrail);
        register(VCatVisualEffects.BLOCK_CRACK_LIGHT.getId(), BlockCrackLightRenderer::render);
        register(VCatVisualEffects.RED_EYE_FLASH.getId(), RedEyeFlashRenderer::render);
        register(VCatVisualEffects.TILTED_HALO.getId(), TiltedHaloRenderer::render);
        register(VCatVisualEffects.DOOM_CORONA.getId(), BackSigilEffectRenderer::renderDoomCorona);
        register(VCatVisualEffects.ABYSSAL_RIFT_EYE.getId(), BackSigilEffectRenderer::renderAbyssalRiftEye);
        register(VCatVisualEffects.HOLY_JUDGEMENT_HALO.getId(), BackSigilEffectRenderer::renderHolyJudgementHalo);
        register(VCatVisualEffects.ASTRAL_CROWN.getId(), BackSigilEffectRenderer::renderAstralCrown);
        register(VCatVisualEffects.BLOOD_MOON_BACKWHEEL.getId(), BackSigilEffectRenderer::renderBloodMoonBackwheel);
        register(VCatVisualEffects.CAUSAL_CHAINS.getId(), BackSigilEffectRenderer::renderCausalChains);
        register(VCatVisualEffects.INVERTED_CROSS_MARK.getId(), BackSigilEffectRenderer::renderInvertedCrossMark);
        register(VCatVisualEffects.VOLUMETRIC_FLAME.getId(), VolumetricFlameRenderer::render);
        register(VCatVisualEffects.PHANTOM_RIFT_SHARDS.getId(), PhantomRiftShardsRenderer::render);
        register(VCatVisualEffects.SUPREME_CHAOS_COSMOS.getId(), SupremeChaosCosmosRenderer::render);
    }

    public static void register(ResourceLocation id, EntityVisualEffectRenderer renderer) {
        RENDERERS.put(id, renderer);
    }

    public static EntityVisualEffectRenderer get(ResourceLocation id) {
        return RENDERERS.get(id);
    }
}
