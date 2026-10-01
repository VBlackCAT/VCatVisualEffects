package net.v_black_cat.vcatvisualeffects.visual.client;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.vcatvisualeffects.VCatVisualEffectsMod;
import net.v_black_cat.vcatvisualeffects.mixin.EntityVisualEffectMixin;
import net.v_black_cat.vcatvisualeffects.visual.EntityVisualEffectSystem;
import net.v_black_cat.vcatvisualeffects.visual.EntityVisualEffects;
import net.v_black_cat.vcatvisualeffects.visual.IVisualEffectHolder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Mod.EventBusSubscriber(modid = VCatVisualEffectsMod.MODID, value = Dist.CLIENT)
public final class ClientEntityVisualEffectPackets {
    private static final Map<Integer, CompoundTag> PENDING_SYNCS = new HashMap<>();

    private ClientEntityVisualEffectPackets() {
    }

    public static void sync(int entityId, CompoundTag effectsTag) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            PENDING_SYNCS.put(entityId, effectsTag.copy());
            return;
        }

        Entity entity = minecraft.level.getEntity(entityId);
        if (entity == null) {
            PENDING_SYNCS.put(entityId, effectsTag.copy());
            return;
        }

        apply(entity, effectsTag);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || PENDING_SYNCS.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }

        Iterator<Map.Entry<Integer, CompoundTag>> iterator = PENDING_SYNCS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, CompoundTag> entry = iterator.next();
            Entity entity = minecraft.level.getEntity(entry.getKey());
            if (entity != null) {
                apply(entity, entry.getValue());
                iterator.remove();
            }
        }
    }

    private static void apply(Entity entity, CompoundTag effectsTag) {
        EntityVisualEffects effects = null;
        if (entity instanceof IVisualEffectHolder holder) {
            effects = holder.vcatve$getVisualEffects();
        }
        if (effects == null) {
            effects = entity.getCapability(EntityVisualEffectSystem.ENTITY_VISUAL_EFFECTS)
                    .resolve()
                    .orElse(null);
        }
        if (effects != null) {
            effects.deserializeNBT(effectsTag, entity.level().getGameTime());
        }
    }
}
