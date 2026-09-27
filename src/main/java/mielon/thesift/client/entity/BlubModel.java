package mielon.thesift.client.entity;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import mielon.thesift.entity.BlubEntity;
import net.minecraft.resources.ResourceLocation;

public final class BlubModel extends GeoModel<BlubEntity> {
   private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("the_sift", "geo/entity/blub.geo.json");
   private static final ResourceLocation ANIMATION = ResourceLocation.fromNamespaceAndPath("the_sift", "animations/entity/blub.animation.json");
   private static final ResourceLocation NORMAL_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/blub.png");
   private static final ResourceLocation TAMED_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/tamed_blub.png");
   private static final ResourceLocation MIELON_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/blub_mielon.png");
   private static final ResourceLocation MIELON_TAMED_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/blub_mielon_tamed.png");

   @Override
   public ResourceLocation getModelResource(BlubEntity blub, @Nullable GeoRenderer<BlubEntity> renderer) {
      return MODEL;
   }

   @Override
   public ResourceLocation getTextureResource(BlubEntity blub, @Nullable GeoRenderer<BlubEntity> renderer) {
      if (isMielon(blub)) {
         return blub != null && blub.isTame() ? MIELON_TAMED_TEXTURE : MIELON_TEXTURE;
      } else {
         return blub != null && blub.isTame() ? TAMED_TEXTURE : NORMAL_TEXTURE;
      }
   }

   private static boolean isMielon(BlubEntity blub) {
      if (blub != null && blub.getCustomName() != null) {
         String name = blub.getCustomName().getString();
         return "mielon".equalsIgnoreCase(name);
      } else {
         return false;
      }
   }

   @Override
   public ResourceLocation getAnimationResource(BlubEntity blub) {
      return ANIMATION;
   }
}
