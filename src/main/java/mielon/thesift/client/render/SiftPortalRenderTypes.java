package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class SiftPortalRenderTypes {
   public static final ResourceLocation MIST_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/block/sift_portal_mist.png");
   public static final ResourceLocation SHADERPACK_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/misc/sift_portal_shaderpack.png");

   public static final RenderType SHADERPACK_LAYER = RenderType.create(
      "sift_portal_shaderpack",
      DefaultVertexFormat.BLOCK,
      VertexFormat.Mode.QUADS,
      1536,
      false,
      false,
      RenderType.CompositeState.builder()
         .setShaderState(RenderStateShard.RENDERTYPE_BEACON_BEAM_SHADER)
         .setTextureState(new RenderStateShard.TextureStateShard(SHADERPACK_TEXTURE, net.minecraft.util.TriState.FALSE, false))
         .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
         .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
         .setCullState(RenderStateShard.NO_CULL)
         .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
         .createCompositeState(false)
   );

   public static final RenderType MIST_LAYER = RenderType.create(
      "sift_portal_mist",
      DefaultVertexFormat.BLOCK,
      VertexFormat.Mode.QUADS,
      1536,
      false,
      false,
      RenderType.CompositeState.builder()
         .setShaderState(RenderStateShard.RENDERTYPE_BEACON_BEAM_SHADER)
         .setTextureState(new RenderStateShard.TextureStateShard(MIST_TEXTURE, net.minecraft.util.TriState.FALSE, false))
         .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
         .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
         .setCullState(RenderStateShard.NO_CULL)
         .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
         .createCompositeState(false)
   );

   private SiftPortalRenderTypes() {
   }

   public static RenderType getMistRenderType() {
      return MIST_LAYER;
   }

   public static RenderType getShaderpackRenderType() {
      return SHADERPACK_LAYER;
   }
}

