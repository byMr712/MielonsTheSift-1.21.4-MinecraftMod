package mielon.thesift.client.render;

import mielon.thesift.fluid.ModFluids;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class IchorFluidRendering {
   private IchorFluidRendering() {
   }

   public static void register() {
      FluidRenderHandlerRegistry.INSTANCE.register(
         ModFluids.ICHOR,
         ModFluids.FLOWING_ICHOR,
         new SimpleFluidRenderHandler(
            ResourceLocation.fromNamespaceAndPath("the_sift", "block/ichor_still"),
            ResourceLocation.fromNamespaceAndPath("the_sift", "block/ichor_flow"),
            ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_overlay"),
            0xFFFFFFFF
         )
      );
      BlockRenderLayerMap.INSTANCE.putFluids(RenderType.translucent(), ModFluids.ICHOR, ModFluids.FLOWING_ICHOR);
   }
}
