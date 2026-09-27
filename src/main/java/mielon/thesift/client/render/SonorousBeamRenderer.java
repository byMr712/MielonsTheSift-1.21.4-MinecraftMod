package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.block.entity.SonorousDeepslateBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Matrix4f;

public class SonorousBeamRenderer implements BlockEntityRenderer<SonorousDeepslateBlockEntity> {
   private static final float MAX_BEAM_HEIGHT = 128.0F;

   public SonorousBeamRenderer(BlockEntityRendererProvider.Context context) {
   }

   @Override
   public void render(SonorousDeepslateBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
      if (blockEntity.hasBeam()) {
         float growth = blockEntity.getBeamGrowth(partialTick);
         if (growth > 0.0F) {
            int colorRGB = blockEntity.getBeamColor();
            float red = (float)(colorRGB >> 16 & 0xFF) / 255.0F;
            float green = (float)(colorRGB >> 8 & 0xFF) / 255.0F;
            float blue = (float)(colorRGB & 0xFF) / 255.0F;
            float bottomY = 1.0F;
            float topY = bottomY + 128.0F * growth;
            float beamHeight = topY - bottomY;
            long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
            float time = (float)gameTime + partialTick;
            float vScroll = -(time / 20.0F) * 1.0F;

            poseStack.pushPose();
            VertexConsumer buffer = bufferSource.getBuffer(SonorousBeamRenderTypes.BEAM);
            renderBeamColumn(buffer, poseStack.last().pose(), 0.35F, bottomY, 0.35F, 0.65F, topY, 0.65F, red, green, blue, vScroll, beamHeight);

            if (blockEntity.getLevel() != null) {
               VertexConsumer waterBuffer = bufferSource.getBuffer(SonorousBeamRenderTypes.WATER_OVERLAY);
               int openSegment = -1;
               for (int offset = 1; offset <= 128 && !((float)offset >= topY); offset++) {
                  Fluid fluid = blockEntity.getLevel().getFluidState(blockEntity.getBlockPos().above(offset)).getType();
                  boolean water = fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
                  if (water) {
                     if (openSegment < 0) {
                        openSegment = offset;
                     }
                  } else if (openSegment >= 0) {
                     float segBottom = (float)openSegment;
                     float segTop = Math.min((float)offset, topY);
                     if (segTop > segBottom) {
                        renderBeamWaterOverlay(waterBuffer, poseStack.last().pose(), segBottom, segTop, topY, red, green, blue, vScroll);
                     }
                     openSegment = -1;
                  }
               }
               if (openSegment >= 0) {
                  float segBottom = (float)openSegment;
                  if (topY > segBottom) {
                     renderBeamWaterOverlay(waterBuffer, poseStack.last().pose(), segBottom, topY, topY, red, green, blue, vScroll);
                  }
               }
            }

            poseStack.popPose();
         }
      }
   }

   private static void renderBeamWaterOverlay(
      VertexConsumer buffer, Matrix4f pose, float minY, float maxY, float beamTopY, float red, float green, float blue, float vScroll
   ) {
      float vBottom = vScroll + (beamTopY - minY) * 0.5F;
      float vTop = vScroll + (beamTopY - maxY) * 0.5F;
      renderBeamColumnWithUv(buffer, pose, 0.35F, minY, 0.35F, 0.65F, maxY, 0.65F, red, green, blue, vTop, vBottom);
   }

   private static void renderBeamColumn(
      VertexConsumer buffer,
      Matrix4f pose,
      float minX,
      float minY,
      float minZ,
      float maxX,
      float maxY,
      float maxZ,
      float red,
      float green,
      float blue,
      float vScroll,
      float beamHeight
   ) {
      renderBeamColumnWithUv(buffer, pose, minX, minY, minZ, maxX, maxY, maxZ, red, green, blue, vScroll, vScroll + beamHeight * 0.5F);
   }

   private static void renderBeamColumnWithUv(
      VertexConsumer buffer,
      Matrix4f pose,
      float minX,
      float minY,
      float minZ,
      float maxX,
      float maxY,
      float maxZ,
      float red,
      float green,
      float blue,
      float v0,
      float v1
   ) {
      addVertex(buffer, pose, minX, minY, maxZ, 0.0F, v1, red, green, blue, 0.0F, 0.0F, 1.0F);
      addVertex(buffer, pose, maxX, minY, maxZ, 1.0F, v1, red, green, blue, 0.0F, 0.0F, 1.0F);
      addVertex(buffer, pose, maxX, maxY, maxZ, 1.0F, v0, red, green, blue, 0.0F, 0.0F, 1.0F);
      addVertex(buffer, pose, minX, maxY, maxZ, 0.0F, v0, red, green, blue, 0.0F, 0.0F, 1.0F);
      addVertex(buffer, pose, maxX, minY, minZ, 0.0F, v1, red, green, blue, 0.0F, 0.0F, -1.0F);
      addVertex(buffer, pose, minX, minY, minZ, 1.0F, v1, red, green, blue, 0.0F, 0.0F, -1.0F);
      addVertex(buffer, pose, minX, maxY, minZ, 1.0F, v0, red, green, blue, 0.0F, 0.0F, -1.0F);
      addVertex(buffer, pose, maxX, maxY, minZ, 0.0F, v0, red, green, blue, 0.0F, 0.0F, -1.0F);
      addVertex(buffer, pose, minX, minY, minZ, 0.0F, v1, red, green, blue, -1.0F, 0.0F, 0.0F);
      addVertex(buffer, pose, minX, minY, maxZ, 1.0F, v1, red, green, blue, -1.0F, 0.0F, 0.0F);
      addVertex(buffer, pose, minX, maxY, maxZ, 1.0F, v0, red, green, blue, -1.0F, 0.0F, 0.0F);
      addVertex(buffer, pose, minX, maxY, minZ, 0.0F, v0, red, green, blue, -1.0F, 0.0F, 0.0F);
      addVertex(buffer, pose, maxX, minY, maxZ, 0.0F, v1, red, green, blue, 1.0F, 0.0F, 0.0F);
      addVertex(buffer, pose, maxX, minY, minZ, 1.0F, v1, red, green, blue, 1.0F, 0.0F, 0.0F);
      addVertex(buffer, pose, maxX, maxY, minZ, 1.0F, v0, red, green, blue, 1.0F, 0.0F, 0.0F);
      addVertex(buffer, pose, maxX, maxY, maxZ, 0.0F, v0, red, green, blue, 1.0F, 0.0F, 0.0F);
   }

   private static void addVertex(
      VertexConsumer buffer,
      Matrix4f pose,
      float x,
      float y,
      float z,
      float u,
      float v,
      float red,
      float green,
      float blue,
      float normalX,
      float normalY,
      float normalZ
   ) {
      buffer.addVertex(pose, x, y, z).setColor(red, green, blue, 0.55F).setUv(u, v).setLight(15728880).setNormal(normalX, normalY, normalZ);
   }
}
