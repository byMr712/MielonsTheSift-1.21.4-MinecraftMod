package mielon.thesift.client.compat;

import java.lang.reflect.Method;
import mielon.thesift.TheSiftMod;
import net.fabricmc.loader.api.FabricLoader;

public final class SiftShaderCompat {
   private static boolean initialized;
   private static boolean irisPresent;
   private static Object irisApi;
   private static Method isShaderPackInUse;

   private SiftShaderCompat() {
   }

   public static void initialize() {
      if (!initialized) {
         initialized = true;
         irisPresent = FabricLoader.getInstance().isModLoaded("iris");
         if (irisPresent) {
            try {
               Class<?> apiClass = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
               irisApi = apiClass.getMethod("getInstance").invoke(null);
               isShaderPackInUse = apiClass.getMethod("isShaderPackInUse");
               TheSiftMod.LOGGER.info("Iris detected; The Sift shader compatibility is enabled.");
            } catch (LinkageError | ReflectiveOperationException var3) {
               irisApi = null;
               isShaderPackInUse = null;
               TheSiftMod.LOGGER.warn("Iris was detected, but its compatibility API could not be initialized.", var3);
            }
         }
      }
   }

   public static boolean isIrisPresent() {
      initialize();
      return irisPresent;
   }

   public static boolean isShaderPackInUse() {
      initialize();
      if (irisApi != null && isShaderPackInUse != null) {
         try {
            return Boolean.TRUE.equals(isShaderPackInUse.invoke(irisApi));
         } catch (RuntimeException | ReflectiveOperationException var1) {
            return false;
         }
      } else {
         return false;
      }
   }
}
