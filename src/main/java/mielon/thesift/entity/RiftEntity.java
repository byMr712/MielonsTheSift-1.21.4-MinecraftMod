package mielon.thesift.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mielon.thesift.sound.ModSounds;
import mielon.thesift.world.RiftAnimationClock;
import mielon.thesift.world.RiftDirectory;
import mielon.thesift.world.RiftLightCleanup;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class RiftEntity extends Entity {
   public static final int LIFETIME_TICKS = 6000;
   public static final int ANIMATION_TICKS = 16;
   private static final EntityDataAccessor<Boolean> DATA_TARGET_SIFT = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> DATA_LONG_ALONG_X = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> DATA_CLOSING = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Boolean> DATA_APPEARING = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.BOOLEAN);
   private static final EntityDataAccessor<Long> DATA_BORN = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.LONG);
   private static final EntityDataAccessor<Long> DATA_END = SynchedEntityData.defineId(RiftEntity.class, EntityDataSerializers.LONG);
   private long expiresAt;
   private UUID pairId = UUID.randomUUID();
   private BlockPos linkedPos;
   private long linkedUntil;
   private boolean appearSoundPlayed;
   private int closingTicks;
   private final List<BlockPos> placedLights = new ArrayList<>(3);

   public RiftEntity(EntityType<? extends RiftEntity> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
      this.setInvulnerable(true);
   }

   protected void defineSynchedData(Builder builder) {
      builder.define(DATA_TARGET_SIFT, false);
      builder.define(DATA_LONG_ALONG_X, true);
      builder.define(DATA_CLOSING, false);
      builder.define(DATA_APPEARING, false);
      builder.define(DATA_BORN, 0L);
      builder.define(DATA_END, 0L);
   }

   public void configure(boolean targetSift, boolean longAlongX, long expiresAt, UUID pairId) {
      this.entityData.set(DATA_TARGET_SIFT, targetSift);
      this.entityData.set(DATA_LONG_ALONG_X, longAlongX);
      this.entityData.set(DATA_APPEARING, true);
      this.expiresAt = expiresAt;
      this.pairId = pairId;
      this.entityData.set(DATA_BORN, this.level().getGameTime());
      this.entityData.set(DATA_END, expiresAt);
   }

   public boolean targetsSift() {
      return (Boolean)this.entityData.get(DATA_TARGET_SIFT);
   }

   public boolean isLongAlongX() {
      return (Boolean)this.entityData.get(DATA_LONG_ALONG_X);
   }

   public boolean isClosing() {
      return (Boolean)this.entityData.get(DATA_CLOSING);
   }

   public long getExpiresAt() {
      return this.expiresAt;
   }

   public void extendExpiresAt(long expiry) {
      if (expiry > this.expiresAt) {
         this.expiresAt = expiry;
         this.entityData.set(DATA_END, expiry);
         if (expiry - this.level().getGameTime() > 16L) {
            this.entityData.set(DATA_CLOSING, false);
            this.closingTicks = 0;
         }
      }
   }

   public UUID getPairId() {
      return this.pairId;
   }

   public BlockPos getLinkedPos() {
      return this.linkedUntil > 0L && this.level().getGameTime() >= this.linkedUntil ? null : this.linkedPos;
   }

   public void setLinkedPos(BlockPos linkedPos) {
      this.linkedPos = linkedPos == null ? null : linkedPos.immutable();
      this.linkedUntil = this.expiresAt;
   }

   public void setLinkedPos(BlockPos pos, long until) {
      this.setLinkedPos(pos);
      this.linkedUntil = until;
   }

   public BlockPos getAnchorPos() {
      return BlockPos.containing(this.getX(), this.getY(), this.getZ());
   }

   public AABB getPortalBounds() {
      return portalBoundsAt(this.position(), this.isLongAlongX());
   }

   private static AABB portalBoundsAt(Vec3 position, boolean longAlongX) {
      double halfX = longAlongX ? 4.5 : 0.5;
      double halfZ = longAlongX ? 0.5 : 4.5;
      return new AABB(position.x - halfX, position.y - 0.375, position.z - halfZ, position.x + halfX, position.y + 3.875, position.z + halfZ);
   }

   protected AABB makeBoundingBox(Vec3 position) {
      return portalBoundsAt(position, this.isLongAlongX());
   }

   public float getOpenScale(float partialTick) {
      if (this.isClosing()) {
         return RiftAnimationClock.progress(true, (double)((float)this.closingTicks + partialTick));
      } else {
         return !this.entityData.get(DATA_APPEARING) ? 1.0F : RiftAnimationClock.progress(false, (double)((float)this.tickCount + partialTick));
      }
   }

   public void tick() {
      this.setBoundingBox(this.getPortalBounds());
      super.tick();
      this.noPhysics = true;
      this.setDeltaMovement(Vec3.ZERO);
      this.setBoundingBox(this.getPortalBounds());
      if (this.level().isClientSide()) {
         if (this.isClosing()) {
            this.closingTicks++;
         } else {
            this.closingTicks = 0;
         }
      } else {
         ServerLevel level = (ServerLevel)this.level();
         RiftDirectory.track(this);
         if (this.expiresAt <= 0L) {
            this.expiresAt = level.getGameTime() + 6000L;
            this.entityData.set(DATA_BORN, level.getGameTime());
            this.entityData.set(DATA_END, this.expiresAt);
         }

         if (!this.appearSoundPlayed) {
            this.appearSoundPlayed = true;
            this.playSound(ModSounds.RIFT_APPEAR, 2.0F, 1.0F);
         }

         if ((Boolean)this.entityData.get(DATA_APPEARING) && this.tickCount >= 16) {
            this.entityData.set(DATA_APPEARING, false);
         }

         if (this.placedLights.isEmpty()) {
            this.placeVanillaLights(level);
         }

         long remaining = this.expiresAt - level.getGameTime();
         if (remaining <= 16L && !this.isClosing()) {
            this.entityData.set(DATA_CLOSING, true);
            this.closingTicks = 0;
         }

         if (this.isClosing()) {
            this.closingTicks++;
         }

         if (remaining <= 0L || this.closingTicks > 16) {
            this.discard();
         }
      }
   }

   private void placeVanillaLights(ServerLevel level) {
      BlockPos anchor = this.getAnchorPos();

      for (int offset : new int[]{-3, 0, 3}) {
         BlockPos lightPos = this.isLongAlongX() ? anchor.offset(offset, 2, 0) : anchor.offset(0, 2, offset);
         if (level.getBlockState(lightPos).isAir()) {
            level.setBlock(lightPos, (BlockState)Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15), 3);
            this.placedLights.add(lightPos.immutable());
         }
      }
   }

   private void removeVanillaLights() {
      if (this.level() instanceof ServerLevel level) {
         RiftLightCleanup.enqueue(level, this.placedLights);
         this.placedLights.clear();
      }
   }

   public void onRemoval(RemovalReason reason) {
      if (reason.shouldDestroy()) {
         this.removeVanillaLights();
      }

      if (!this.level().isClientSide()) {
         RiftDirectory.removed(this, reason.shouldDestroy());
      }

      super.onRemoval(reason);
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      this.entityData.set(DATA_TARGET_SIFT, tag.getBoolean("TargetSift"));
      this.entityData.set(DATA_LONG_ALONG_X, tag.getBoolean("LongAlongX"));
      this.entityData.set(DATA_CLOSING, tag.getBoolean("Closing"));
      this.expiresAt = tag.getLong("ExpiresAt");
      this.entityData.set(DATA_END, this.expiresAt);
      this.entityData.set(DATA_BORN, tag.contains("BornAt") ? tag.getLong("BornAt") : this.level().getGameTime() - 16L);
      this.pairId = parseUuid(tag.getString("PairId"), UUID.randomUUID());
      if (tag.contains("LinkedX") && tag.contains("LinkedY") && tag.contains("LinkedZ")) {
         this.setLinkedPos(new BlockPos(tag.getInt("LinkedX"), tag.getInt("LinkedY"), tag.getInt("LinkedZ")));
      }
      this.linkedUntil = tag.getLong("LinkedUntil");
      this.appearSoundPlayed = tag.getBoolean("AppearSoundPlayed");
      this.closingTicks = tag.getInt("ClosingTicks");
      this.placedLights.clear();
      if (tag.contains("PlacedLights", 9)) {
         net.minecraft.nbt.ListTag list = tag.getList("PlacedLights", 10);
         for (int i = 0; i < list.size(); i++) {
            CompoundTag pt = list.getCompound(i);
            this.placedLights.add(new BlockPos(pt.getInt("x"), pt.getInt("y"), pt.getInt("z")));
         }
      }
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      tag.putBoolean("TargetSift", this.targetsSift());
      tag.putBoolean("LongAlongX", this.isLongAlongX());
      tag.putBoolean("Closing", this.isClosing());
      tag.putLong("ExpiresAt", this.expiresAt);
      tag.putLong("BornAt", (Long)this.entityData.get(DATA_BORN));
      tag.putString("PairId", this.pairId.toString());
      if (this.linkedPos != null) {
         tag.putInt("LinkedX", this.linkedPos.getX());
         tag.putInt("LinkedY", this.linkedPos.getY());
         tag.putInt("LinkedZ", this.linkedPos.getZ());
         tag.putLong("LinkedUntil", this.linkedUntil);
      }
      tag.putBoolean("AppearSoundPlayed", this.appearSoundPlayed);
      tag.putInt("ClosingTicks", this.closingTicks);
      net.minecraft.nbt.ListTag lights = new net.minecraft.nbt.ListTag();
      for (BlockPos p : this.placedLights) {
         CompoundTag pt = new CompoundTag();
         pt.putInt("x", p.getX());
         pt.putInt("y", p.getY());
         pt.putInt("z", p.getZ());
         lights.add(pt);
      }
      tag.put("PlacedLights", lights);
   }

   private static UUID parseUuid(String value, UUID fallback) {
      try {
         return UUID.fromString(value);
      } catch (IllegalArgumentException var3) {
         return fallback;
      }
   }

   public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
      return false;
   }

   public boolean hurtClient(DamageSource source) {
      return false;
   }

   public boolean isAttackable() {
      return false;
   }

   public boolean isPickable() {
      return false;
   }

   public boolean canBeHitByProjectile() {
      return false;
   }

   public boolean isPushable() {
      return false;
   }

   public boolean canCollideWith(Entity other) {
      return false;
   }

   public boolean canBeCollidedWith(Entity other) {
      return false;
   }

   public boolean skipAttackInteraction(Entity attacker) {
      return true;
   }

   protected void doWaterSplashEffect() {
   }

   public boolean isPushedByFluid() {
      return false;
   }

   public AABB getFluidInteractionBox() {
      return new AABB(this.getX(), this.getY(), this.getZ(), this.getX(), this.getY(), this.getZ());
   }
}
