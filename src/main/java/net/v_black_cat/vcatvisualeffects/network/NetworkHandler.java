package net.v_black_cat.vcatvisualeffects.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.v_black_cat.vcatvisualeffects.VCatVisualEffectsMod;

import java.util.Optional;

/**
 * Network channel of V猫的视觉特效. Only carries the visual-effect state sync of
 * {@link SyncEntityVisualEffectsPacket}; the system is otherwise server-authoritative through the
 * entity capability.
 */
public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VCatVisualEffectsMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(
                id++,
                SyncEntityVisualEffectsPacket.class,
                SyncEntityVisualEffectsPacket::encode,
                SyncEntityVisualEffectsPacket::decode,
                SyncEntityVisualEffectsPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }
}
