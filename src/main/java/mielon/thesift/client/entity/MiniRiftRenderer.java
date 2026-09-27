package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import mielon.thesift.client.render.RiftRenderTypes;
import mielon.thesift.entity.MiniRiftEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;

public final class MiniRiftRenderer extends EntityRenderer<MiniRiftEntity, MiniRiftRenderer.State> {
   private static final RiftMesh CUBE = new RiftMesh(List.of(new RiftAssetModel.Box(-0.4F, -0.4F, -0.4F, 0.4F, 0.4F, 0.4F)));

   public MiniRiftRenderer(Context context) {
      super(context);
      this.shadowRadius = 0.0F;
   }

   @Override
   public MiniRiftRenderer.State createRenderState() {
      return new MiniRiftRenderer.State();
   }

   @Override
   public void extractRenderState(MiniRiftEntity entity, MiniRiftRenderer.State state, float partial) {
      super.extractRenderState(entity, state, partial);
      state.open = entity.getOpenScale(partial);
   }

   @Override
   protected int getBlockLightLevel(MiniRiftEntity entity, BlockPos pos) {
      return 15;
   }

   @Override
   public void render(MiniRiftRenderer.State state, PoseStack poses, MultiBufferSource bufferSource, int packedLight) {
      if (!(state.open <= 0.0F)) {
         poses.pushPose();
         float size = state.open * state.open * (3.0F - 2.0F * state.open);
         poses.scale(size, size, size);
         CUBE.draw(bufferSource.getBuffer(RiftRenderTypes.SIFT), poses.last(), true, false);
         CUBE.draw(bufferSource.getBuffer(RiftRenderTypes.GLOW), poses.last(), true, true);
         poses.popPose();
      }
   }

   public static final class State extends EntityRenderState {
      float open;
   }
}
