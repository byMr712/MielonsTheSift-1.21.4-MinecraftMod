package mielon.thesift.block.entity;

import java.util.ArrayList;
import java.util.List;
import mielon.thesift.block.ModBlocks;
import mielon.thesift.block.SonorousNoteBlocks;
import mielon.thesift.portal.PortalAutoActivation;
import mielon.thesift.portal.SonorousBeams;
import mielon.thesift.portal.SonorousConsoles;
import mielon.thesift.portal.SonorousEffects;
import mielon.thesift.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.nbt.CompoundTag;

public class SonorousDeepslateBlockEntity extends BlockEntity {
   private static final int BEAM_GROW_TICKS = 40;
   private static final int BEAM_SHRINK_TICKS = 40;
   private static final int AUTOPLAY_TICKS_BETWEEN_NOTES = 15;
   private int beamColor = -1;
   private int growAge = 0;
   private boolean shrinking = false;
   private int shrinkAge = 0;
   private int pendingShrinkTicks = -1;
   private boolean autoplayActive = false;
   private boolean autoplayClosing = false;
   private int autoplayIndex = 0;
   private int autoplayCooldown = 0;
   private long[] autoplayNotePositions = new long[0];
   private int[] autoplaySoundIndices = new int[0];

   public SonorousDeepslateBlockEntity(BlockPos pos, BlockState state) {
      super(ModBlocks.SONOROUS_DEEPSLATE_BLOCK_ENTITY, pos, state);
   }

   public void startBeam(int colorRGB) {
      this.beamColor = colorRGB;
      this.growAge = 0;
      this.shrinking = false;
      this.shrinkAge = 0;
      this.pendingShrinkTicks = -1;
      this.setChanged();
   }

   public void clearBeam() {
      if (this.beamColor != -1) {
         this.beamColor = -1;
         this.growAge = 0;
         this.shrinking = false;
         this.shrinkAge = 0;
         this.pendingShrinkTicks = -1;
         this.setChanged();
      }
   }

   public void scheduleShrink(int ticks) {
      if (this.beamColor != -1 && !this.shrinking) {
         this.pendingShrinkTicks = Math.max(0, ticks);
         this.setChanged();
      }
   }

   public void startShrink() {
      if (this.beamColor != -1 && !this.shrinking) {
         this.shrinking = true;
         this.shrinkAge = 0;
         this.pendingShrinkTicks = -1;
         this.setChanged();
      }
   }

   public boolean hasBeam() {
      return this.beamColor != -1;
   }

   public int getBeamColor() {
      return this.beamColor;
   }

   public float getBeamGrowth(float partialTick) {
      if (this.beamColor == -1) {
         return 0.0F;
      } else if (this.shrinking) {
         float shrinkFraction = Mth.clamp(((float)this.shrinkAge + partialTick) / 40.0F, 0.0F, 1.0F);
         return 1.0F - shrinkFraction;
      } else {
         return Mth.clamp(((float)this.growAge + partialTick) / 40.0F, 0.0F, 1.0F);
      }
   }

   public void startAutoplay(SonorousConsoles.MatchedSequence matched) {
      int count = matched.notes().size();
      if (count <= 0) {
         this.stopAutoplay();
      } else {
         this.autoplayNotePositions = new long[count];
         this.autoplaySoundIndices = new int[count];

         for (int i = 0; i < count; i++) {
            SonorousConsoles.PlayedNote note = matched.notes().get(i);
            this.autoplayNotePositions[i] = note.notePos().asLong();
            this.autoplaySoundIndices[i] = note.soundIndex1to8();
         }

         this.autoplayClosing = matched.type() == SonorousConsoles.SequenceType.CLOSING;
         this.autoplayIndex = 0;
         this.autoplayCooldown = 0;
         this.autoplayActive = true;
         this.markPersistentChanged();
      }
   }

   public boolean stopAutoplay() {
      if (!this.autoplayActive) {
         return false;
      } else {
         this.clearAutoplayState();
         this.markPersistentChanged();
         return true;
      }
   }

   private void clearAutoplayState() {
      this.autoplayActive = false;
      this.autoplayClosing = false;
      this.autoplayIndex = 0;
      this.autoplayCooldown = 0;
      this.autoplayNotePositions = new long[0];
      this.autoplaySoundIndices = new int[0];
   }

   private void tickAutoplay(ServerLevel level) {
      if (this.autoplayActive) {
         if (this.autoplayNotePositions.length == 0 || this.autoplayNotePositions.length != this.autoplaySoundIndices.length) {
            this.clearAutoplayState();
            this.markPersistentChanged();
         } else if (this.autoplayCooldown > 0) {
            this.autoplayCooldown--;
            this.markPersistentChanged();
         } else if (this.autoplayIndex >= this.autoplayNotePositions.length) {
            this.finishAutoplay(level);
         } else {
            BlockPos notePos = BlockPos.of(this.autoplayNotePositions[this.autoplayIndex]);
            int soundIndex1to8 = this.autoplaySoundIndices[this.autoplayIndex];
            if (level.getBlockState(notePos).is(Blocks.NOTE_BLOCK) && SonorousNoteBlocks.isSonorous(level, notePos)) {
               SonorousNoteBlocks.playSound(level, notePos, soundIndex1to8 - 1);
               SonorousEffects.spawnColorBurst(level, notePos, soundIndex1to8);
               SonorousBeams.start(level, notePos, soundIndex1to8);
               this.autoplayIndex++;
               this.autoplayCooldown = 15;
               this.markPersistentChanged();
            } else {
               SonorousBeams.clearGroup(level, this.worldPosition);
               level.playSound(null, notePos, ModSounds.SONOROUS_AUTOPLAY_GLITCH, SoundSource.RECORDS, 3.0F, 1.0F);
               this.clearAutoplayState();
               this.markPersistentChanged();
            }
         }
      }
   }

   private void finishAutoplay(ServerLevel level) {
      List<SonorousConsoles.PlayedNote> sequence = new ArrayList<>(this.autoplayNotePositions.length);

      for (int i = 0; i < this.autoplayNotePositions.length; i++) {
         sequence.add(new SonorousConsoles.PlayedNote(BlockPos.of(this.autoplayNotePositions[i]), this.autoplaySoundIndices[i]));
      }

      boolean closing = this.autoplayClosing;
      this.clearAutoplayState();
      this.markPersistentChanged();
      if (closing) {
         PortalAutoActivation.tryCloseNearNotes(level, sequence);
      } else {
         PortalAutoActivation.tryActivateNearNotes(level, sequence);
      }

      if (!sequence.isEmpty()) {
         SonorousBeams.scheduleRetract(level, this.worldPosition);
      }
   }

   public static void tick(Level level, BlockPos pos, BlockState state, SonorousDeepslateBlockEntity blockEntity) {
      if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
         blockEntity.tickAutoplay(serverLevel);
      }

      if (blockEntity.beamColor != -1) {
         if (blockEntity.pendingShrinkTicks >= 0) {
            if (blockEntity.pendingShrinkTicks == 0) {
               blockEntity.pendingShrinkTicks = -1;
               blockEntity.startShrink();
            } else {
               blockEntity.pendingShrinkTicks--;
               if (!level.isClientSide()) {
                  blockEntity.markPersistentChanged();
               }
            }
         } else {
            if (blockEntity.shrinking) {
               if (blockEntity.shrinkAge < 40) {
                  blockEntity.shrinkAge++;
                  if (!level.isClientSide()) {
                     blockEntity.markPersistentChanged();
                  }
               } else {
                  blockEntity.beamColor = -1;
                  blockEntity.shrinking = false;
                  blockEntity.shrinkAge = 0;
                  blockEntity.pendingShrinkTicks = -1;
                  if (!level.isClientSide()) {
                     blockEntity.setChanged();
                  }
               }
            } else if (blockEntity.growAge < 40) {
               blockEntity.growAge++;
               if (!level.isClientSide()) {
                  blockEntity.markPersistentChanged();
               }
            }
         }
      }
   }

   @Override
   protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
      super.saveAdditional(tag, registries);
      tag.putInt("beam_color", this.beamColor);
      tag.putInt("beam_grow_age", this.growAge);
      tag.putBoolean("beam_shrinking", this.shrinking);
      tag.putInt("beam_shrink_age", this.shrinkAge);
      tag.putInt("beam_pending_shrink_ticks", this.pendingShrinkTicks);
      tag.putInt("persistent_autoplay_version", 1);
      tag.putBoolean("autoplay_active", this.autoplayActive);
      tag.putBoolean("autoplay_closing", this.autoplayClosing);
      tag.putInt("autoplay_index", this.autoplayIndex);
      tag.putInt("autoplay_cooldown", this.autoplayCooldown);
      tag.putInt("autoplay_note_count", this.autoplayNotePositions.length);

      for (int i = 0; i < this.autoplayNotePositions.length; i++) {
         tag.putLong("autoplay_note_pos_" + i, this.autoplayNotePositions[i]);
         tag.putInt("autoplay_note_sound_" + i, this.autoplaySoundIndices[i]);
      }
   }

   @Override
   protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
      super.loadAdditional(tag, registries);
      this.beamColor = tag.contains("beam_color") ? tag.getInt("beam_color") : -1;
      this.growAge = Math.max(0, tag.getInt("beam_grow_age"));
      this.shrinking = tag.getBoolean("beam_shrinking");
      this.shrinkAge = Math.max(0, tag.getInt("beam_shrink_age"));
      this.pendingShrinkTicks = tag.contains("beam_pending_shrink_ticks") ? tag.getInt("beam_pending_shrink_ticks") : -1;
      int persistentAutoplayVersion = tag.getInt("persistent_autoplay_version");
      this.autoplayActive = tag.getBoolean("autoplay_active");
      this.autoplayClosing = tag.getBoolean("autoplay_closing");
      this.autoplayIndex = Math.max(0, tag.getInt("autoplay_index"));
      this.autoplayCooldown = Math.max(0, tag.getInt("autoplay_cooldown"));
      int count = Math.max(0, Math.min(64, tag.getInt("autoplay_note_count")));
      this.autoplayNotePositions = new long[count];
      this.autoplaySoundIndices = new int[count];

      for (int i = 0; i < count; i++) {
         this.autoplayNotePositions[i] = tag.getLong("autoplay_note_pos_" + i);
         this.autoplaySoundIndices[i] = tag.contains("autoplay_note_sound_" + i) ? tag.getInt("autoplay_note_sound_" + i) : 1;
      }

      if (count == 0) {
         this.autoplayActive = false;
      } else {
         this.autoplayIndex = Math.min(this.autoplayIndex, count);
      }

      if (persistentAutoplayVersion == 0 && this.beamColor != -1 && !this.shrinking && this.pendingShrinkTicks < 0) {
         this.pendingShrinkTicks = 60;
      }
   }

   public CompoundTag getUpdateTag(Provider registries) {
      return this.saveWithoutMetadata(registries);
   }

   public Packet<ClientGamePacketListener> getUpdatePacket() {
      return ClientboundBlockEntityDataPacket.create(this);
   }

   private void markPersistentChanged() {
      super.setChanged();
   }

   public void setChanged() {
      super.setChanged();
      if (this.level != null) {
         BlockState state = this.getBlockState();
         this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
      }
   }
}
