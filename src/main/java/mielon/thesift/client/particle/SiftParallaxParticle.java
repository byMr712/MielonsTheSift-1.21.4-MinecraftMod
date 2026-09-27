package mielon.thesift.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class SiftParallaxParticle extends TextureSheetParticle {
   private final float fragmentU;
   private final float fragmentV;
   private final float fragmentWidth;
   private final float fragmentHeight;

   private SiftParallaxParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
      super(level, x, y, z, xd, yd, zd);
      this.setSprite(sprites.get(level.getRandom()));
      RandomSource random = this.random;
      this.quadSize /= 2.0F;
      this.gravity = 1.0F;
      this.friction = 0.98F;
      this.hasPhysics = true;
      this.alpha = 1.0F;
      this.fragmentWidth = 0.3F + random.nextFloat() * 0.3F;
      this.fragmentHeight = 0.3F + random.nextFloat() * 0.3F;
      this.fragmentU = random.nextFloat() * (1.0F - this.fragmentWidth);
      this.fragmentV = random.nextFloat() * (1.0F - this.fragmentHeight);
   }

   @Override
   protected float getU0() {
      return this.sprite.getU(this.fragmentU);
   }

   @Override
   protected float getU1() {
      return this.sprite.getU(this.fragmentU + this.fragmentWidth);
   }

   @Override
   protected float getV0() {
      return this.sprite.getV(this.fragmentV);
   }

   @Override
   protected float getV1() {
      return this.sprite.getV(this.fragmentV + this.fragmentHeight);
   }

   @Override
   public int getLightColor(float partialTickTime) {
      return 15728880;
   }

   @Override
   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
   }

   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprites;

      public Provider(SpriteSet sprites) {
         this.sprites = sprites;
      }

      @Override
      public Particle createParticle(
         SimpleParticleType type, ClientLevel level, double x, double y, double z, double xd, double yd, double zd
      ) {
         return new SiftParallaxParticle(level, x, y, z, xd, yd, zd, this.sprites);
      }
   }
}
