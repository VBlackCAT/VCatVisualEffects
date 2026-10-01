package net.v_black_cat.vcatvisualeffects.compat;

import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Oculus / Iris shader-pack bridge.
 *
 * <p>Screen-space effects must run after the shader pack has finished its own composite pass, and
 * the depth buffer they sample is only available in that situation. The API is therefore resolved
 * reflectively so the mod stays free of any hard dependency on Oculus or Iris.</p>
 */
public final class ShaderModCompat {

    @Nullable
    private static Method getInstanceMethod;
    @Nullable
    private static Method isShaderPackInUseMethod;
    private static boolean triedResolveApi;

    private ShaderModCompat() {
    }

    public static boolean isShaderModLoaded() {
        ModList modList = ModList.get();
        return modList != null && (modList.isLoaded("oculus") || modList.isLoaded("iris"));
    }

    public static boolean isShaderPackInUse() {
        if (!isShaderModLoaded()) {
            return false;
        }

        try {
            if (!resolveApi()) {
                return false;
            }

            Object irisApi = getInstanceMethod.invoke(null);
            return Boolean.TRUE.equals(isShaderPackInUseMethod.invoke(irisApi));
        } catch (ReflectiveOperationException | LinkageError exception) {
            return false;
        }
    }

    private static boolean resolveApi() throws ReflectiveOperationException {
        if (!triedResolveApi) {
            triedResolveApi = true;
            Class<?> irisApiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            getInstanceMethod = irisApiClass.getMethod("getInstance");
            isShaderPackInUseMethod = irisApiClass.getMethod("isShaderPackInUse");
        }

        return getInstanceMethod != null && isShaderPackInUseMethod != null;
    }
}
