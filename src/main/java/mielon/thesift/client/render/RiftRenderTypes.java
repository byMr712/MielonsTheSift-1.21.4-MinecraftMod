package mielon.thesift.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class RiftRenderTypes {
   public static final RenderType SIFT = RenderType.entityCutoutNoCull(id("textures/entity/rift/sift.png"));
   public static final RenderType OVERWORLD = RenderType.entityCutoutNoCull(id("textures/entity/rift/overworld.png"));
   public static final RenderType GLOW = RenderType.eyes(id("textures/entity/rift/sift.png"));

   private RiftRenderTypes() {
   }

   private static ResourceLocation id(String path) {
      return ResourceLocation.fromNamespaceAndPath("the_sift", path);
   }
}
