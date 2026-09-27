package mielon.thesift.client.entity;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import mielon.thesift.entity.EchoGolemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public final class EchoGolemRenderer extends GeoEntityRenderer<EchoGolemEntity> {
   public EchoGolemRenderer(EntityRendererProvider.Context context) {
      super(context, new EchoGolemModel());
      this.addRenderLayer(new EchoGolemSoulGlowLayer(this));
      this.shadowRadius = 0.72F;
   }
}
