package mielon.thesift.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class SonorousBeamRenderTypes {
   private static final ResourceLocation BEAM_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/block/sonorous_deepslate_beam.png");
   public static final RenderType BEAM = RenderType.beaconBeam(BEAM_TEXTURE, true);
   public static final RenderType WATER_OVERLAY = RenderType.beaconBeam(BEAM_TEXTURE, true);

   private SonorousBeamRenderTypes() {
   }

   public static void initialize() {
   }
}
