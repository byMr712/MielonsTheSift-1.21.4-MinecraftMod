package mielon.thesift.client.entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import mielon.thesift.entity.BlubEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public final class BlubRenderer extends GeoEntityRenderer<BlubEntity> {
   public BlubRenderer(EntityRendererProvider.Context context) {
      super(context, new BlubModel());
      this.shadowRadius = 0.38F;
   }
}
