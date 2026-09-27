package mielon.thesift.client.entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import mielon.thesift.entity.SingerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SingerRenderer extends GeoEntityRenderer<SingerEntity> {
   public SingerRenderer(EntityRendererProvider.Context context) {
      super(context, new SingerModel());
      this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
      this.shadowRadius = 0.0F;
   }
}
