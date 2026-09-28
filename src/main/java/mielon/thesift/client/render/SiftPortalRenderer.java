package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.block.entity.SiftPortalBlockEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

public class SiftPortalRenderer implements BlockEntityRenderer<SiftPortalBlockEntity> {
   public SiftPortalRenderer(BlockEntityRendererProvider.Context context) {
   }

   public SiftPortalRenderer() {
   }

   @Override
   public int getViewDistance() {
      return 256;
   }

   @Override
   public void render(SiftPortalBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
      long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
      float time = ((float)gameTime + partialTick) * 0.02F;
      
      PoseStack.Pose entry = poseStack.last();
      Matrix4f matrix = entry.pose();

      // Main emissive portal surface
      VertexConsumer buffer = bufferSource.getBuffer(SiftPortalRenderTypes.getPortalRenderType());
      renderAllFaces(blockEntity, matrix, entry, buffer, time, 235);

      // Inner swirling glowing pass for animated depth
      VertexConsumer beamBuffer = bufferSource.getBuffer(SiftPortalRenderTypes.getPortalBeamRenderType());
      renderAllFaces(blockEntity, matrix, entry, beamBuffer, -time * 0.7F, 160);
   }

   private void renderAllFaces(SiftPortalBlockEntity blockEntity, Matrix4f matrix, PoseStack.Pose entry, VertexConsumer buffer, float time, int alpha) {
      renderFace(blockEntity, Direction.SOUTH, matrix, entry, buffer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, time, alpha);
      renderFace(blockEntity, Direction.NORTH, matrix, entry, buffer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, time, alpha);
      renderFace(blockEntity, Direction.EAST, matrix, entry, buffer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, time, alpha);
      renderFace(blockEntity, Direction.WEST, matrix, entry, buffer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, time, alpha);
      renderFace(blockEntity, Direction.DOWN, matrix, entry, buffer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, time, alpha);
      renderFace(blockEntity, Direction.UP, matrix, entry, buffer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, time, alpha);
   }

   private void renderFace(
      SiftPortalBlockEntity blockEntity,
      Direction direction,
      Matrix4f matrix,
      PoseStack.Pose entry,
      VertexConsumer buffer,
      float x0,
      float x1,
      float y0,
      float y1,
      float z0,
      float z1,
      float z2,
      float z3,
      float time,
      int alpha
   ) {
      if (blockEntity.shouldRenderFace(direction)) {
         float u0 = 0.0F + time;
         float u1 = 1.0F + time;
         float v0 = 0.0F - time * 0.5F;
         float v1 = 1.0F - time * 0.5F;
         int light = LightTexture.FULL_BRIGHT;
         int overlay = OverlayTexture.NO_OVERLAY;
         float nx = (float)direction.getStepX();
         float ny = (float)direction.getStepY();
         float nz = (float)direction.getStepZ();

         // Front face
         buffer.addVertex(matrix, x0, y0, z0).setColor(255, 255, 255, alpha).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(entry, nx, ny, nz);
         buffer.addVertex(matrix, x1, y0, z1).setColor(255, 255, 255, alpha).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(entry, nx, ny, nz);
         buffer.addVertex(matrix, x1, y1, z2).setColor(255, 255, 255, alpha).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(entry, nx, ny, nz);
         buffer.addVertex(matrix, x0, y1, z3).setColor(255, 255, 255, alpha).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(entry, nx, ny, nz);

         // Back face (so portal interior is visible from both sides)
         buffer.addVertex(matrix, x0, y1, z3).setColor(255, 255, 255, alpha).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(entry, -nx, -ny, -nz);
         buffer.addVertex(matrix, x1, y1, z2).setColor(255, 255, 255, alpha).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(entry, -nx, -ny, -nz);
         buffer.addVertex(matrix, x1, y0, z1).setColor(255, 255, 255, alpha).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(entry, -nx, -ny, -nz);
         buffer.addVertex(matrix, x0, y0, z0).setColor(255, 255, 255, alpha).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(entry, -nx, -ny, -nz);
      }
   }
}
