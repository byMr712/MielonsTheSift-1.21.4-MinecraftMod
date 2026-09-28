package mielon.thesift.client.render;

import mielon.thesift.block.entity.SiftPortalBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;

public class SiftPortalRenderer extends TheEndPortalRenderer<SiftPortalBlockEntity> {
   public SiftPortalRenderer(BlockEntityRendererProvider.Context context) {
      super(context);
   }

   @Override
   protected RenderType renderType() {
      return SiftPortalRenderTypes.getPortalRenderType();
   }

   @Override
   protected float getOffsetUp() {
      return 1.0F;
   }

   @Override
   protected float getOffsetDown() {
      return 0.0F;
   }
}
