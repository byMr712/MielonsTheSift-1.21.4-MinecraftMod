package mielon.thesift.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public final class OvergrownWillowFoliageBlock extends LeavesBlock {
   public OvergrownWillowFoliageBlock(float leafParticleChance, Properties properties) {
      super(properties);
   }

   public OvergrownWillowFoliageBlock(Properties properties) {
      super(properties);
   }

   @Override
   public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
      super.animateTick(state, level, pos, random);
   }
}
