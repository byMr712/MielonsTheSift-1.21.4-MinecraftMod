package mielon.thesift.mixin;

import mielon.thesift.entity.SnifferInfectionAccess;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;

import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Animal.class})
public abstract class AnimalSnifferInfectionSaveMixin {
   @Inject(
      method = {"addAdditionalSaveData"},
      at = {@At("TAIL")}
   )
   private void theSift$saveSnifferInfection(CompoundTag tag, CallbackInfo ci) {
      if ((Object)this instanceof Sniffer && (Object)this instanceof SnifferInfectionAccess access && access.theSift$getSculkInfectionTicks() > 0) {
         tag.putInt("TheSiftSculkInfection", access.theSift$getSculkInfectionTicks());
      }
   }

   @Inject(
      method = {"readAdditionalSaveData"},
      at = {@At("TAIL")}
   )
   private void theSift$loadSnifferInfection(CompoundTag tag, CallbackInfo ci) {
      if ((Object)this instanceof Sniffer && (Object)this instanceof SnifferInfectionAccess access) {
         access.theSift$setSculkInfectionTicks(tag.getInt("TheSiftSculkInfection"));
      }
   }
}
