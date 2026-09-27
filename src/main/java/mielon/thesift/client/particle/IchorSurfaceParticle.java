package mielon.thesift.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;

public final class IchorSurfaceParticle extends TextureSheetParticle {
   private final SpriteSet sprites;
   private final float surfaceYaw;

   private IchorSurfaceParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
      super(level, x, y, z);
      this.sprites = sprites;
      this.setSprite(sprites.get(level.getRandom()));
      this.surfaceYaw = this.random.nextFloat() * (float) (Math.PI * 2);
      this.quadSize = 0.12F + this.random.nextFloat() * 0.12F;
      this.lifetime = 40 + this.random.nextInt(31);
      this.hasPhysics = false;
      this.gravity = 0.0F;
      this.friction = 1.0F;
      this.xd = 0.0;
      this.yd = 0.0;
      this.zd = 0.0;
      this.setAlpha(0.0F);
      this.setSpriteFromAge(sprites);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.removed) {
         float progress = (float)this.age / (float)this.lifetime;
         this.setAlpha((float)Math.sin(Math.PI * (double)progress) * 0.55F);
         this.setSpriteFromAge(this.sprites);
      }
   }

   @Override
   public void render(VertexConsumer buffer, Camera camera, float partialTick) {
      Quaternionf rotation = new Quaternionf().rotationY(this.surfaceYaw).rotateX((float) (-Math.PI / 2));
      this.renderRotatedQuad(buffer, camera, rotation, partialTick);
   }

   @Override
   public int getLightColor(float partialTickTime) {
      return 15728880;
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   public static final class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprites;

      public Provider(SpriteSet sprites) {
         this.sprites = sprites;
      }

      @Override
      public Particle createParticle(
         SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd
      ) {
         return new IchorSurfaceParticle(level, x, y, z, this.sprites);
      }
   }
}
