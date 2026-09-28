package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import mielon.thesift.client.compat.SiftShaderCompat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class SiftPortalRenderTypes {
   public static final ResourceLocation MIST_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/block/sift_portal_mist.png");
   public static final ResourceLocation SHADERPACK_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/misc/sift_portal_shaderpack.png");

   private static final RenderType MIST_PORTAL = RenderType.create(
      "the_sift_portal",
      DefaultVertexFormat.POSITION,
      VertexFormat.Mode.QUADS,
      1536,
      false,
      false,
      RenderType.CompositeState.builder()
         .setShaderState(RenderStateShard.RENDERTYPE_END_PORTAL_SHADER)
         .setTextureState(
            RenderStateShard.MultiTextureStateShard.builder()
               .add(MIST_TEXTURE, false, false)
               .add(MIST_TEXTURE, false, false)
               .build()
         )
         .createCompositeState(false)
   );

   private static final RenderType SHADERPACK_PORTAL = RenderType.create(
      "the_sift_portal_shaderpack",
      DefaultVertexFormat.POSITION,
      VertexFormat.Mode.QUADS,
      1536,
      false,
      false,
      RenderType.CompositeState.builder()
         .setShaderState(RenderStateShard.RENDERTYPE_END_PORTAL_SHADER)
         .setTextureState(
            RenderStateShard.MultiTextureStateShard.builder()
               .add(SHADERPACK_TEXTURE, false, false)
               .add(SHADERPACK_TEXTURE, false, false)
               .build()
         )
         .createCompositeState(false)
   );

   private SiftPortalRenderTypes() {
   }

   public static RenderType getPortalRenderType() {
      return SiftShaderCompat.isShaderPackInUse() ? SHADERPACK_PORTAL : MIST_PORTAL;
   }
}
