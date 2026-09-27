package mielon.thesift.client.entity;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import mielon.thesift.entity.SingerEntity;
import net.minecraft.resources.ResourceLocation;

public class SingerModel extends GeoModel<SingerEntity> {
   private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("the_sift", "geo/entity/singer.geo.json");
   private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath("the_sift", "animations/entity/singer.animation.json");
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/singer.png");

   @Override
   public ResourceLocation getModelResource(SingerEntity animatable, @Nullable GeoRenderer<SingerEntity> renderer) {
      return MODEL;
   }

   @Override
   public ResourceLocation getTextureResource(SingerEntity animatable, @Nullable GeoRenderer<SingerEntity> renderer) {
      return TEXTURE;
   }

   @Override
   public ResourceLocation getAnimationResource(SingerEntity animatable) {
      return ANIMATION;
   }
}
