package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.block.entity.SiftPortalBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

public class SiftPortalRenderer implements BlockEntityRenderer<SiftPortalBlockEntity> {
   public SiftPortalRenderer(BlockEntityRendererProvider.Context context) {
   }

   @Override
   public void render(
      SiftPortalBlockEntity blockEntity,
      float partialTick,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      int packedLight,
      int packedOverlay
   ) {
      long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
      float time = ((float)gameTime + partialTick) * 0.015F;

      poseStack.pushPose();
      Matrix4f pose = poseStack.last().pose();

      // Render outer background shaderpack sky layer
      VertexConsumer shaderpackBuf = bufferSource.getBuffer(SiftPortalRenderTypes.getShaderpackRenderType());
      renderPortalLayers(blockEntity, shaderpackBuf, pose, time * 0.4F, time * 0.25F, 1.0F, 0.9F, 0.95F, 1.0F, 0.85F, 0.0F);

      // Render inner moving mist wave layers for deep celestial 3D parallax
      VertexConsumer mistBuf = bufferSource.getBuffer(SiftPortalRenderTypes.getMistRenderType());
      renderPortalLayers(blockEntity, mistBuf, pose, -time * 0.7F, time * 0.5F, 1.8F, 0.75F, 0.95F, 1.0F, 0.65F, 0.02F);
      renderPortalLayers(blockEntity, mistBuf, pose, time * 0.9F, -time * 0.6F, 2.4F, 0.85F, 1.0F, 1.0F, 0.45F, 0.04F);

      poseStack.popPose();
   }

   private static void renderPortalLayers(
      SiftPortalBlockEntity blockEntity,
      VertexConsumer buffer,
      Matrix4f pose,
      float uOffset,
      float vOffset,
      float uvScale,
      float red,
      float green,
      float blue,
      float alpha,
      float inset
   ) {
      float min = 0.0F + inset;
      float max = 1.0F - inset;

      // TOP face (Y = 1.0 - inset)
      if (blockEntity.shouldRenderFace(Direction.UP)) {
         renderQuad(buffer, pose, min, max, min, max, max, min, min, max, max, max, min, max,
            uOffset, vOffset, uvScale, red, green, blue, alpha, 0.0F, 1.0F, 0.0F);
      }

      // BOTTOM face (Y = 0.0 + inset)
      if (blockEntity.shouldRenderFace(Direction.DOWN)) {
         renderQuad(buffer, pose, min, min, max, max, min, max, max, min, min, min, min, min,
            uOffset, vOffset, uvScale, red, green, blue, alpha, 0.0F, -1.0F, 0.0F);
      }

      // NORTH face (Z = 0.0 + inset)
      if (blockEntity.shouldRenderFace(Direction.NORTH)) {
         renderQuad(buffer, pose, max, min, min, min, min, min, min, max, min, max, max, min,
            uOffset, vOffset, uvScale, red, green, blue, alpha, 0.0F, 0.0F, -1.0F);
      }

      // SOUTH face (Z = 1.0 - inset)
      if (blockEntity.shouldRenderFace(Direction.SOUTH)) {
         renderQuad(buffer, pose, min, min, max, max, min, max, max, max, max, min, max, max,
            uOffset, vOffset, uvScale, red, green, blue, alpha, 0.0F, 0.0F, 1.0F);
      }

      // WEST face (X = 0.0 + inset)
      if (blockEntity.shouldRenderFace(Direction.WEST)) {
         renderQuad(buffer, pose, min, min, min, min, min, max, min, max, max, min, max, min,
            uOffset, vOffset, uvScale, red, green, blue, alpha, -1.0F, 0.0F, 0.0F);
      }

      // EAST face (X = 1.0 - inset)
      if (blockEntity.shouldRenderFace(Direction.EAST)) {
         renderQuad(buffer, pose, max, min, max, max, min, min, max, max, min, max, max, max,
            uOffset, vOffset, uvScale, red, green, blue, alpha, 1.0F, 0.0F, 0.0F);
      }
   }

   private static void renderQuad(
      VertexConsumer buffer,
      Matrix4f pose,
      float x0, float y0, float z0,
      float x1, float y1, float z1,
      float x2, float y2, float z2,
      float x3, float y3, float z3,
      float uOffset, float vOffset, float uvScale,
      float red, float green, float blue, float alpha,
      float nx, float ny, float nz
   ) {
      addVertex(buffer, pose, x0, y0, z0, uOffset, vOffset + uvScale, red, green, blue, alpha, nx, ny, nz);
      addVertex(buffer, pose, x1, y1, z1, uOffset + uvScale, vOffset + uvScale, red, green, blue, alpha, nx, ny, nz);
      addVertex(buffer, pose, x2, y2, z2, uOffset + uvScale, vOffset, red, green, blue, alpha, nx, ny, nz);
      addVertex(buffer, pose, x3, y3, z3, uOffset, vOffset, red, green, blue, alpha, nx, ny, nz);
   }

   private static void addVertex(
      VertexConsumer buffer,
      Matrix4f pose,
      float x, float y, float z,
      float u, float v,
      float red, float green, float blue, float alpha,
      float nx, float ny, float nz
   ) {
      buffer.addVertex(pose, x, y, z)
         .setColor(red, green, blue, alpha)
         .setUv(u, v)
         .setLight(15728880)
         .setNormal(nx, ny, nz);
   }
}
