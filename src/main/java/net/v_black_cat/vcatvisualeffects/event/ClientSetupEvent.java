package net.v_black_cat.vcatvisualeffects.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.v_black_cat.vcatvisualeffects.VCatVisualEffectsMod;
import net.v_black_cat.vcatvisualeffects.visual.client.ScreenSpaceDepthEffectPostProcessor;

/**
 * 客户端资源重载监听注册。
 *
 * <p>屏幕空间（领域类）特效走的是 {@code EffectInstance} / {@code PostChain}，它**只能**由
 * {@link ScreenSpaceDepthEffectPostProcessor#reloadListener()} 在资源重载时创建。如果不在这里注册这个
 * reload listener，{@code ScreenSpaceDepthEffectPostProcessor.effect} 会永远是 {@code null}，
 * {@code process()} 第一行就返回 —— 表现就是所有领域类特效（cosmic_domain / lunar_domain / ...）
 * 静默不显示，而世界空间的核心着色器特效照常工作。</p>
 */
@Mod.EventBusSubscriber(modid = VCatVisualEffectsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientSetupEvent {

    private ClientSetupEvent() {
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ScreenSpaceDepthEffectPostProcessor.reloadListener());
    }
}
