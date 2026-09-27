package mielon.thesift.client.entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import mielon.thesift.entity.SifterEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public final class SifterRenderer extends GeoEntityRenderer<SifterEntity> {
   public SifterRenderer(EntityRendererProvider.Context context) {
      super(context, new SifterModel());
      this.shadowRadius = 0.42F;
   }
}
