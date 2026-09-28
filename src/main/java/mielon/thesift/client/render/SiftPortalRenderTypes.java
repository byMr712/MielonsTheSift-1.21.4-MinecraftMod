package mielon.thesift.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class SiftPortalRenderTypes {
   public static final ResourceLocation MIST_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/block/sift_portal_mist.png");
   public static final ResourceLocation SHADERPACK_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/misc/sift_portal_shaderpack.png");

   public static final RenderType MIST_LAYER = RenderType.entityTranslucentEmissive(MIST_TEXTURE);
   public static final RenderType SHADERPACK_LAYER = RenderType.entityTranslucentEmissive(SHADERPACK_TEXTURE);

   private SiftPortalRenderTypes() {
   }

   public static RenderType getMistRenderType() {
      return MIST_LAYER;
   }

   public static RenderType getShaderpackRenderType() {
      return SHADERPACK_LAYER;
   }
}
