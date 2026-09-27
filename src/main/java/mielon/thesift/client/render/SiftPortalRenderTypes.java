package mielon.thesift.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public final class SiftPortalRenderTypes {
   private static final ResourceLocation MIST_TEXTURE = ResourceLocation.fromNamespaceAndPath("the_sift", "textures/block/sift_portal_mist.png");
   public static final RenderType SIFT_PORTAL = RenderType.endPortal();

   private SiftPortalRenderTypes() {
   }
}
