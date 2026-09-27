package mielon.thesift.mixin;

import java.util.Map;
import mielon.thesift.fluid.IchorState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Builder.class})
public abstract class IchorStateDefinitionMixin {
   @Shadow
   @Final
   private Object owner;
   @Shadow
   @Final
   private Map<String, Property<?>> properties;

   @Inject(
      method = {"create"},
      at = {@At("HEAD")}
   )
   private void theSift$addFluidState(CallbackInfoReturnable<?> cir) {
      if (this.owner instanceof Block && this.properties.get("waterlogged") instanceof BooleanProperty) {
         this.properties.putIfAbsent("ichorlogged", IchorState.ICHORLOGGED);
      }
   }
}
