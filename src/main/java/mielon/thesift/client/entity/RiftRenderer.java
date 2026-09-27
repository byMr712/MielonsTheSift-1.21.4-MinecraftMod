package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import mielon.thesift.client.render.RiftRenderTypes;
import mielon.thesift.entity.RiftEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.core.BlockPos;

public final class RiftRenderer extends EntityRenderer<RiftEntity, RiftRenderState> {
   private static final RiftMesh OPEN = new RiftMesh(RiftAssetModel.INSTANCE.boxes(1.0F));

   public RiftRenderer(Context context) {
      super(context);
      this.shadowRadius = 0.0F;
   }

   @Override
   public RiftRenderState createRenderState() {
      return new RiftRenderState();
   }

   @Override
   public void extractRenderState(RiftEntity entity, RiftRenderState state, float partial) {
      super.extractRenderState(entity, state, partial);
      state.openScale = entity.getOpenScale(partial);
      state.longAlongX = entity.isLongAlongX();
      state.targetSift = entity.targetsSift();
   }

   @Override
   protected int getBlockLightLevel(RiftEntity entity, BlockPos pos) {
      return 15;
   }

   @Override
   public void render(RiftRenderState state, PoseStack poses, MultiBufferSource bufferSource, int packedLight) {
      if (!(state.openScale <= 0.0F)) {
         RiftMesh mesh = state.openScale >= 1.0F ? OPEN : new RiftMesh(RiftAssetModel.INSTANCE.boxes(state.openScale));
         boolean axis = state.longAlongX;
         mesh.draw(bufferSource.getBuffer(state.targetSift ? RiftRenderTypes.SIFT : RiftRenderTypes.OVERWORLD), poses.last(), axis, false);
         mesh.draw(bufferSource.getBuffer(RiftRenderTypes.GLOW), poses.last(), axis, true);
      }
   }
}
