package mielon.thesift.client.entity;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import mielon.thesift.entity.SifterEntity;
import net.minecraft.resources.ResourceLocation;

public final class SifterModel extends GeoModel<SifterEntity> {
   private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("the_sift", "geo/entity/sifter.geo.json");
   private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath("the_sift", "animations/entity/sifter.animation.json");
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/sifter.png");

   @Override
   public ResourceLocation getModelResource(SifterEntity animatable, @Nullable GeoRenderer<SifterEntity> renderer) {
      return MODEL;
   }

   @Override
   public ResourceLocation getTextureResource(SifterEntity animatable, @Nullable GeoRenderer<SifterEntity> renderer) {
      return TEXTURE;
   }

   @Override
   public ResourceLocation getAnimationResource(SifterEntity animatable) {
      return ANIMATION;
   }
}
