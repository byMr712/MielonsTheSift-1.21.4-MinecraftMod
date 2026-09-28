package mielon.thesift.client;

import mielon.thesift.block.ModBlocks;
import mielon.thesift.client.compat.SiftShaderCompat;
import mielon.thesift.client.entity.BlubRenderer;
import mielon.thesift.client.entity.DarkSnifferRenderer;
import mielon.thesift.client.entity.EchoGolemRenderer;
import mielon.thesift.client.entity.MiniRiftRenderer;
import mielon.thesift.client.entity.RiftRenderer;
import mielon.thesift.client.entity.SifterRenderer;
import mielon.thesift.client.entity.SingerRenderer;
import mielon.thesift.client.particle.IchorFluidParticle;
import mielon.thesift.client.particle.IchorSurfaceParticle;
import mielon.thesift.client.particle.SiftNoteParticle;
import mielon.thesift.client.particle.SiftParallaxParticle;
import mielon.thesift.client.particle.SingerSoundWaveParticle;
import mielon.thesift.client.particle.SoulParticle;
import mielon.thesift.client.particle.SoundWaveParticle;
import mielon.thesift.client.render.IchorFluidRendering;
import mielon.thesift.client.render.SiftPortalRenderer;
import mielon.thesift.client.render.SonorousBeamRenderTypes;
import mielon.thesift.client.render.SonorousBeamRenderer;
import mielon.thesift.entity.ModEntities;
import mielon.thesift.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;

public final class TheSiftClient implements ClientModInitializer {
   private static final ModelLayerLocation OVERGROWN_WILLOW_BOAT_LAYER = new ModelLayerLocation(
      ResourceLocation.fromNamespaceAndPath("the_sift", "boat/overgrown_willow"), "main"
   );
   private static final ModelLayerLocation OVERGROWN_WILLOW_CHEST_BOAT_LAYER = new ModelLayerLocation(
      ResourceLocation.fromNamespaceAndPath("the_sift", "chest_boat/overgrown_willow"), "main"
   );

   @Override
   public void onInitializeClient() {
      SiftShaderCompat.initialize();
      SonorousBeamRenderTypes.initialize();
      IchorFluidRendering.register();

      BlockRenderLayerMap.INSTANCE.putBlocks(
         RenderType.cutout(),
         ModBlocks.SCULKFLOWER_CROP,
         ModBlocks.SCULKFLOWER,
         ModBlocks.OVERGROWN_CHARD,
         ModBlocks.OVERGROWN_STALKS,
         ModBlocks.OVERGROWN_FRONDS,
         ModBlocks.OVERGROWN_LOTUS,
         ModBlocks.SUNBURST_PLANT,
         ModBlocks.WHISPERBLOOM,
         ModBlocks.SIFTSLATE_STALKS,
         ModBlocks.HEALTHY_SCULK_SPROUTS,
         ModBlocks.DRY_HEALTHY_SCULK_SPROUTS,
         ModBlocks.SIFTSLATE_HANGING_ROOTS,
         ModBlocks.OVERGROWN_HANGING_ROOTS,
         ModBlocks.OVERGROWN_WILLOW_DOOR,
         ModBlocks.OVERGROWN_WILLOW_TRAPDOOR,
         ModBlocks.OVERGROWN_WILLOW_SAPLING,
         ModBlocks.OVERGROWN_WILLOW_VINES,
         ModBlocks.OVERGROWN_WILLOW_VINES_PLANT
      );
      BlockRenderLayerMap.INSTANCE.putBlocks(
         RenderType.cutoutMipped(),
         ModBlocks.OVERGROWN_WILLOW_FOLIAGE
      );
      EntityModelLayerRegistry.registerModelLayer(OVERGROWN_WILLOW_BOAT_LAYER, BoatModel::createBoatModel);
      EntityModelLayerRegistry.registerModelLayer(OVERGROWN_WILLOW_CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
      EntityRendererRegistry.register(ModEntities.OVERGROWN_WILLOW_BOAT, context -> new BoatRenderer(context, OVERGROWN_WILLOW_BOAT_LAYER));
      EntityRendererRegistry.register(ModEntities.OVERGROWN_WILLOW_CHEST_BOAT, context -> new BoatRenderer(context, OVERGROWN_WILLOW_CHEST_BOAT_LAYER));
      BlockEntityRendererRegistry.register(ModBlocks.SIFT_PORTAL_BLOCK_ENTITY, SiftPortalRenderer::new);
      BlockEntityRendererRegistry.register(ModBlocks.SONOROUS_DEEPSLATE_BLOCK_ENTITY, SonorousBeamRenderer::new);
      EntityRendererRegistry.register(ModEntities.SINGER, SingerRenderer::new);
      EntityRendererRegistry.register(ModEntities.ECHO_GOLEM, EchoGolemRenderer::new);
      EntityRendererRegistry.register(ModEntities.DARK_SNIFFER, DarkSnifferRenderer::new);
      EntityRendererRegistry.register(ModEntities.BLUB, BlubRenderer::new);
      EntityRendererRegistry.register(ModEntities.SIFTER, SifterRenderer::new);
      EntityRendererRegistry.register(ModEntities.RIFT, RiftRenderer::new);
      EntityRendererRegistry.register(ModEntities.MINI_RIFT, MiniRiftRenderer::new);
      EntityRendererRegistry.register(ModEntities.SIFTITE_RETURN, context -> new ThrownItemRenderer<>(context, 1.0F, true));
      ParticleFactoryRegistry.getInstance().register(ModParticles.SIFT_PARALLAX, SiftParallaxParticle.Provider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.SIFT_NOTE, SiftNoteParticle.Provider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.SOUND_WAVE, SoundWaveParticle.Provider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.SINGER_SOUND_WAVE, SingerSoundWaveParticle.Provider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.CANYON_SOUL, SoulParticle.CanyonSoulProvider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.SOUL_FRAGMENT, SoulParticle.Provider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.ICHOR_SURFACE_MIST, IchorSurfaceParticle.Provider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.ICHOR_BUBBLE, IchorFluidParticle.BubbleProvider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.ICHOR_SPLASH, IchorFluidParticle.SplashProvider::new);
      ParticleFactoryRegistry.getInstance().register(ModParticles.ICHOR_RAIN_SPLASH, IchorFluidParticle.RainSplashProvider::new);
   }
}
