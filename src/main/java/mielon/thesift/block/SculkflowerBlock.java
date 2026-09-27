package mielon.thesift.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class SculkflowerBlock extends FlowerBlock {
   public SculkflowerBlock(Properties properties) {
      super(MobEffects.DARKNESS, 8.0F, properties);
   }

   @Override
   protected boolean mayPlaceOn(BlockState floor, BlockGetter level, BlockPos floorPos) {
      return floor.is(Blocks.FARMLAND) || floor.is(Blocks.SCULK) || super.mayPlaceOn(floor, level, floorPos);
   }
}
