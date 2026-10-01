package net.v_black_cat.vcatvisualeffects.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Runtime options for V猫的视觉特效 / VCat Visual Effects.
 *
 * <p>Only the gameplay hooks that the visual-effect system itself triggers are configurable; the
 * rendering parameters of an effect are supplied per-instance through the effect data NBT
 * (see {@code /vcatvisual add ... [duration] [data]}).</p>
 */
public final class VCatConfig {

    public static final ForgeConfigSpec SPEC;
    private static final Common COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        SPEC = builder.build();
    }

    private VCatConfig() {
    }

    public static boolean isSkeletonRedEyeEffectEnabled() {
        return COMMON.skeletonRedEyeEffectEnabled.get();
    }

    public static final class Common {
        private final ForgeConfigSpec.BooleanValue skeletonRedEyeEffectEnabled;

        private Common(ForgeConfigSpec.Builder builder) {
            builder.push("skeletonEye");
            this.skeletonRedEyeEffectEnabled = builder
                    .comment("Whether to enable the skeleton red-eye effect\n是否启用骷髅红眼特效")
                    .define("enabled", false);
            builder.pop();
        }
    }
}
