package mielon.thesift.client.entity;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public final class SiftGlowingOutlineLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
   public SiftGlowingOutlineLayer(GeoRenderer<T> renderer) {
      super(renderer);
   }
}
