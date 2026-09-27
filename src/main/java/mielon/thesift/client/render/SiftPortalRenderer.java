package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.block.entity.SiftPortalBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
      VertexConsumer buffer = bufferSource.getBuffer(SiftPortalRenderTypes.SIFT_PORTAL);
      Matrix4f matrix = poseStack.last().pose();
      renderFace(blockEntity, Direction.SOUTH, matrix, buffer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
      renderFace(blockEntity, Direction.NORTH, matrix, buffer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      renderFace(blockEntity, Direction.EAST, matrix, buffer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
      renderFace(blockEntity, Direction.WEST, matrix, buffer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F);
      renderFace(blockEntity, Direction.DOWN, matrix, buffer, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
      renderFace(blockEntity, Direction.UP, matrix, buffer, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F);
   }

   private void renderFace(
      SiftPortalBlockEntity blockEntity,
      Direction direction,
      Matrix4f matrix,
      VertexConsumer buffer,
      float x0,
      float x1,
      float y0,
      float y1,
      float z0,
      float z1,
      float z2,
      float z3
   ) {
      if (blockEntity.shouldRenderFace(direction)) {
         buffer.addVertex(matrix, x0, y0, z0);
         buffer.addVertex(matrix, x1, y0, z1);
         buffer.addVertex(matrix, x1, y1, z2);
         buffer.addVertex(matrix, x0, y1, z3);
      }
   }
}
