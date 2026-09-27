package mielon.thesift.client.entity;

import net.minecraft.client.renderer.entity.SnifferRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.state.SnifferRenderState;
import net.minecraft.resources.ResourceLocation;

public final class DarkSnifferRenderer extends SnifferRenderer {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/entity/dark_sniffer.png");

   public DarkSnifferRenderer(Context context) {
      super(context);
   }

   public ResourceLocation getTextureLocation(SnifferRenderState state) {
      return TEXTURE;
   }
}
