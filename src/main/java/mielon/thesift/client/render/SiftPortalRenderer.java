package mielon.thesift.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mielon.thesift.block.entity.SiftPortalBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
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
      BlockPos pos = blockEntity.getBlockPos();
      long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
      float time = ((float)gameTime + partialTick) * 0.04F;

      poseStack.pushPose();
      Matrix4f pose = poseStack.last().pose();

      // Pass 1: Semi-transparent celestial cyan backdrop (translucent, analogous to Nether portal)
      VertexConsumer skyBuf = bufferSource.getBuffer(SiftPortalRenderTypes.getShaderpackRenderType());
      renderPortalWorldFace(blockEntity, skyBuf, pose, pos,
         time * 0.035F, time * 0.020F,
         0.20F, 0.28F, 0.85F, 0.98F, 0.75F, 0.0F);

      // Pass 2: Deep drifting nebular mist layer
      VertexConsumer mistBuf = bufferSource.getBuffer(SiftPortalRenderTypes.getMistRenderType());
      renderPortalWorldFace(blockEntity, mistBuf, pose, pos,
         -time * 0.065F, time * 0.045F,
         0.32F, 0.35F, 0.60F, 0.95F, 0.68F, 0.0F);

      // Pass 3: Radiant high-speed surface swirl shimmer layer
      renderPortalWorldFace(blockEntity, mistBuf, pose, pos,
         time * 0.095F, -time * 0.080F,
         0.48F, 0.55F, 0.95F, 1.0F, 0.57F, 0.0F);

      poseStack.popPose();
   }

   private static void renderPortalWorldFace(
      SiftPortalBlockEntity blockEntity,
      VertexConsumer buffer,
      Matrix4f pose,
      BlockPos pos,
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

      // World X and Z coordinate mapping for seamless texture tiling across all portal blocks
      float wx0 = (float)pos.getX() * uvScale + uOffset;
      float wx1 = (float)(pos.getX() + 1) * uvScale + uOffset;
      float wz0 = (float)pos.getZ() * uvScale + vOffset;
      float wz1 = (float)(pos.getZ() + 1) * uvScale + vOffset;
      float wy0 = (float)pos.getY() * uvScale + vOffset;
      float wy1 = (float)(pos.getY() + 1) * uvScale + vOffset;

      // TOP face (Y = 1.0 - inset)
      if (blockEntity.shouldRenderFace(Direction.UP)) {
         addVertex(buffer, pose, min, max, min, wx0, wz0, red, green, blue, alpha, 0.0F, 1.0F, 0.0F);
         addVertex(buffer, pose, min, max, max, wx0, wz1, red, green, blue, alpha, 0.0F, 1.0F, 0.0F);
         addVertex(buffer, pose, max, max, max, wx1, wz1, red, green, blue, alpha, 0.0F, 1.0F, 0.0F);
         addVertex(buffer, pose, max, max, min, wx1, wz0, red, green, blue, alpha, 0.0F, 1.0F, 0.0F);
      }

      // BOTTOM face (Y = 0.0 + inset)
      if (blockEntity.shouldRenderFace(Direction.DOWN)) {
         addVertex(buffer, pose, min, min, max, wx0, wz1, red, green, blue, alpha, 0.0F, -1.0F, 0.0F);
         addVertex(buffer, pose, min, min, min, wx0, wz0, red, green, blue, alpha, 0.0F, -1.0F, 0.0F);
         addVertex(buffer, pose, max, min, min, wx1, wz0, red, green, blue, alpha, 0.0F, -1.0F, 0.0F);
         addVertex(buffer, pose, max, min, max, wx1, wz1, red, green, blue, alpha, 0.0F, -1.0F, 0.0F);
      }

      // NORTH face (Z = 0.0 + inset)
      if (blockEntity.shouldRenderFace(Direction.NORTH)) {
         addVertex(buffer, pose, max, min, min, wx1, wy0, red, green, blue, alpha, 0.0F, 0.0F, -1.0F);
         addVertex(buffer, pose, min, min, min, wx0, wy0, red, green, blue, alpha, 0.0F, 0.0F, -1.0F);
         addVertex(buffer, pose, min, max, min, wx0, wy1, red, green, blue, alpha, 0.0F, 0.0F, -1.0F);
         addVertex(buffer, pose, max, max, min, wx1, wy1, red, green, blue, alpha, 0.0F, 0.0F, -1.0F);
      }

      // SOUTH face (Z = 1.0 - inset)
      if (blockEntity.shouldRenderFace(Direction.SOUTH)) {
         addVertex(buffer, pose, min, min, max, wx0, wy0, red, green, blue, alpha, 0.0F, 0.0F, 1.0F);
         addVertex(buffer, pose, max, min, max, wx1, wy0, red, green, blue, alpha, 0.0F, 0.0F, 1.0F);
         addVertex(buffer, pose, max, max, max, wx1, wy1, red, green, blue, alpha, 0.0F, 0.0F, 1.0F);
         addVertex(buffer, pose, min, max, max, wx0, wy1, red, green, blue, alpha, 0.0F, 0.0F, 1.0F);
      }

      // WEST face (X = 0.0 + inset)
      if (blockEntity.shouldRenderFace(Direction.WEST)) {
         addVertex(buffer, pose, min, min, min, wz0, wy0, red, green, blue, alpha, -1.0F, 0.0F, 0.0F);
         addVertex(buffer, pose, min, min, max, wz1, wy0, red, green, blue, alpha, -1.0F, 0.0F, 0.0F);
         addVertex(buffer, pose, min, max, max, wz1, wy1, red, green, blue, alpha, -1.0F, 0.0F, 0.0F);
         addVertex(buffer, pose, min, max, min, wz0, wy1, red, green, blue, alpha, -1.0F, 0.0F, 0.0F);
      }

      // EAST face (X = 1.0 - inset)
      if (blockEntity.shouldRenderFace(Direction.EAST)) {
         addVertex(buffer, pose, max, min, max, wz1, wy0, red, green, blue, alpha, 1.0F, 0.0F, 0.0F);
         addVertex(buffer, pose, max, min, min, wz0, wy0, red, green, blue, alpha, 1.0F, 0.0F, 0.0F);
         addVertex(buffer, pose, max, max, min, wz0, wy1, red, green, blue, alpha, 1.0F, 0.0F, 0.0F);
         addVertex(buffer, pose, max, max, max, wz1, wy1, red, green, blue, alpha, 1.0F, 0.0F, 0.0F);
      }
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
