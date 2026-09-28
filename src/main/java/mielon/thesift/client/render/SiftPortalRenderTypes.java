package mielon.thesift.client.render;

import mielon.thesift.client.compat.SiftShaderCompat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class SiftPortalRenderTypes {
   public static final ResourceLocation MIST_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/block/sift_portal_mist.png");
   public static final ResourceLocation SHADERPACK_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/misc/sift_portal_shaderpack.png");

   public static final RenderType MIST_EMISSIVE = RenderType.entityTranslucentEmissive(MIST_TEXTURE);
   public static final RenderType SHADERPACK_EMISSIVE = RenderType.entityTranslucentEmissive(SHADERPACK_TEXTURE);
   public static final RenderType MIST_BEAM = RenderType.beaconBeam(MIST_TEXTURE, true);
   public static final RenderType SHADERPACK_BEAM = RenderType.beaconBeam(SHADERPACK_TEXTURE, true);

   private SiftPortalRenderTypes() {
   }

   public static RenderType getPortalRenderType() {
      return SiftShaderCompat.isShaderPackInUse() ? SHADERPACK_EMISSIVE : MIST_EMISSIVE;
   }

   public static RenderType getPortalBeamRenderType() {
      return SiftShaderCompat.isShaderPackInUse() ? SHADERPACK_BEAM : MIST_BEAM;
   }
}
