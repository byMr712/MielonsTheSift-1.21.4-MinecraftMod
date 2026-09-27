package mielon.thesift.mixin;

import mielon.thesift.fluid.IchorState;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({WallBlock.class})
public abstract class WallBlockMixin {
   @ModifyVariable(
      method = {"getShape"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private BlockState theSift$dryWallShape(BlockState state) {
      return IchorState.dryDefault(state);
   }

   @ModifyVariable(
      method = {"getCollisionShape"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private BlockState theSift$dryWallCollisionShape(BlockState state) {
      return IchorState.dryDefault(state);
   }
}
