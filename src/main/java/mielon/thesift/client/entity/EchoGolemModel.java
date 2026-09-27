package mielon.thesift.client.entity;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import mielon.thesift.entity.EchoGolemEntity;
import net.minecraft.resources.ResourceLocation;

public final class EchoGolemModel extends GeoModel<EchoGolemEntity> {
   private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("the_sift", "geo/entity/echo_golem.geo.json");
   private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath("the_sift", "animations/entity/echo_golem.animation.json");
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/echo_golem.png");

   @Override
   public ResourceLocation getModelResource(EchoGolemEntity animatable, @Nullable GeoRenderer<EchoGolemEntity> renderer) {
      return MODEL;
   }

   @Override
   public ResourceLocation getTextureResource(EchoGolemEntity animatable, @Nullable GeoRenderer<EchoGolemEntity> renderer) {
      return TEXTURE;
   }

   @Override
   public ResourceLocation getAnimationResource(EchoGolemEntity animatable) {
      return ANIMATION;
   }
}
