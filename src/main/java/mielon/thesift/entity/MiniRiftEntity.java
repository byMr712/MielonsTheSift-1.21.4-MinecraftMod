package mielon.thesift.entity;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class MiniRiftEntity extends Entity {
   public static final int ANIMATION_TICKS = 12;
   private static final int EMIT_INTERVAL = 5;
   private static final EntityDataAccessor<Boolean> DATA_CLOSING = SynchedEntityData.defineId(MiniRiftEntity.class, EntityDataSerializers.BOOLEAN);
   private final Deque<ItemStack> pending = new ArrayDeque<>();
   private UUID ownerId;
   private long batchOrder;
   private int nextDeliveryIndex;
   private int closingTicks;

   public MiniRiftEntity(EntityType<? extends MiniRiftEntity> type, Level level) {
      super(type, level);
      this.noPhysics = true;
      this.setNoGravity(true);
   }

   @Override
   protected void defineSynchedData(Builder builder) {
      builder.define(DATA_CLOSING, false);
   }

   public void configure(UUID ownerId, Collection<ItemStack> stacks, long batchOrder) {
      this.ownerId = ownerId;
      this.batchOrder = batchOrder;
      this.nextDeliveryIndex = 0;
      this.pending.clear();
      for (ItemStack stack : stacks) {
         if (!stack.isEmpty()) {
            this.pending.addLast(stack.copy());
         }
      }
   }

   public boolean isClosing() {
      return (Boolean)this.entityData.get(DATA_CLOSING);
   }

   public float getOpenScale(float partialTick) {
      if (this.isClosing()) {
         float progress = 1.0F - (float)this.closingTicks / 12.0F;
         return Math.max(0.0F, Math.min(1.0F, progress));
      } else {
         float progress = (float)this.tickCount / 12.0F;
         return Math.max(0.0F, Math.min(1.0F, progress));
      }
   }

   @Override
   public boolean isPickable() {
      return false;
   }

   @Override
   public boolean isPushable() {
      return false;
   }

   @Override
   public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
      return false;
   }

   @Override
   public void tick() {
      super.tick();
      if (this.isClosing()) {
         this.closingTicks++;
         if (this.closingTicks >= 12 && !this.level().isClientSide()) {
            this.discard();
         }
      } else if (!this.level().isClientSide()) {
         if (!this.pending.isEmpty() && this.tickCount >= 12 && this.tickCount % 5 == 0) {
            ItemStack stack = this.pending.peekFirst();
            SiftiteReturnEntity returning = new SiftiteReturnEntity(ModEntities.SIFTITE_RETURN, this.level());
            long deliveryOrder = (this.batchOrder << 16) + Integer.toUnsignedLong(this.nextDeliveryIndex);
            returning.configure(this.ownerId, stack, deliveryOrder);
            returning.setPos(this.getX(), this.getY(), this.getZ());
            if (this.level().addFreshEntity(returning)) {
               this.pending.removeFirst();
               this.nextDeliveryIndex++;
            }
         }

         if (this.pending.isEmpty() && this.tickCount >= 12) {
            this.entityData.set(DATA_CLOSING, true);
            this.closingTicks = 0;
         }
      }
   }

   @Override
   protected void readAdditionalSaveData(CompoundTag tag) {
      this.ownerId = parseUuid(tag.getString("Owner"));
      this.entityData.set(DATA_CLOSING, tag.getBoolean("Closing"));
      this.closingTicks = tag.getInt("ClosingTicks");
      this.batchOrder = tag.getLong("BatchOrder");
      this.nextDeliveryIndex = tag.getInt("NextDeliveryIndex");
      if (tag.contains("Pending", 9)) {
         ListTag list = tag.getList("Pending", 10);
         for (int i = 0; i < list.size(); i++) {
            ItemStack.parse(this.registryAccess(), list.getCompound(i)).ifPresent(this.pending::addLast);
         }
      }
   }

   @Override
   protected void addAdditionalSaveData(CompoundTag tag) {
      if (this.ownerId != null) {
         tag.putString("Owner", this.ownerId.toString());
      }
      tag.putBoolean("Closing", this.isClosing());
      tag.putInt("ClosingTicks", this.closingTicks);
      tag.putLong("BatchOrder", this.batchOrder);
      tag.putInt("NextDeliveryIndex", this.nextDeliveryIndex);
      ListTag list = new ListTag();
      for (ItemStack stack : this.pending) {
         list.add(stack.save(this.registryAccess()));
      }
      tag.put("Pending", list);
   }

   private static UUID parseUuid(String value) {
      try {
         return UUID.fromString(value);
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }
}
