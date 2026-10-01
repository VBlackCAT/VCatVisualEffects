package net.v_black_cat.vcatvisualeffects.mixin;

import net.minecraft.world.entity.Entity;
import net.v_black_cat.vcatvisualeffects.visual.EntityVisualEffects;
import net.v_black_cat.vcatvisualeffects.visual.IVisualEffectHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityVisualEffectMixin implements IVisualEffectHolder {

    @Unique
    private EntityVisualEffects vcatve$effects;

    @Override
    public EntityVisualEffects vcatve$getVisualEffects() {
        return vcatve$effects;
    }

    @Override
    public void vcatve$setVisualEffects(EntityVisualEffects effects) {
        this.vcatve$effects = effects;
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void onRemove(CallbackInfo ci) {
        this.vcatve$effects = null;
    }
}