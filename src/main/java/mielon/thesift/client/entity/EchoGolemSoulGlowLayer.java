package mielon.thesift.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import mielon.thesift.entity.EchoGolemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class EchoGolemSoulGlowLayer extends AutoGlowingGeoLayer<EchoGolemEntity> {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/echo_golem/echo_golem_emissive.png");

   public EchoGolemSoulGlowLayer(GeoRenderer<EchoGolemEntity> renderer) {
      super(renderer);
   }

   @Override
   protected ResourceLocation getTextureResource(EchoGolemEntity animatable) {
      return TEXTURE;
   }

   @Override
   public void render(PoseStack poseStack, EchoGolemEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int renderColor) {
      if (animatable != null && animatable.hasSoulBlock()) {
         super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay, renderColor);
      }
   }
}
