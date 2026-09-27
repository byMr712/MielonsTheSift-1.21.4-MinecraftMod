package mielon.thesift.worldgen;

import com.mojang.serialization.MapCodec;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mielon.thesift.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public final class AbandonedMainPortalFeature extends Feature<NoneFeatureConfiguration> {
   private static final String[] OVERGROWN_TEMPLATES = templateIds("abandoned_portal_overgrown_");
   private static final String[] WASTES_TEMPLATES = templateIds("abandoned_portal_wastes_");
   private static final BlockPos PIVOT = new BlockPos(4, 0, 19);
   private static final int MAX_RELIEF = 20;
   private static final int FOUNDATION_BLEND_RADIUS = 15;

   public AbandonedMainPortalFeature() {
      super(NoneFeatureConfiguration.CODEC);
   }

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      return place(context.level(), context.chunkGenerator(), context.random(), context.origin());
   }

   public boolean place(WorldGenLevel level, ChunkGenerator generator, RandomSource random, BlockPos origin) {
      if (!SiftLandmarkPlacement.isSelectedChunk(level.getSeed(), origin, 32, 4702392765337521733L)) {
         return false;
      } else {
         SiftWorldgenBounds bounds = SiftWorldgenBounds.aroundWithNeighbourMargin(origin);
         int centerX = (origin.getX() & -16) + 8;
         int centerZ = (origin.getZ() & -16) + 8;
         int surfaceY = level.getHeight(Types.WORLD_SURFACE_WG, centerX, centerZ) - 1;
         BlockPos center = new BlockPos(centerX, surfaceY, centerZ);
         if (surfaceY > level.getMinY() + 4 && !SiftFeaturePlacementGuard.intersectsPortal(center, 28) && isSiftTerrain(level.getBlockState(center))) {
            ResourceLocation templateId = chooseTemplate(level, center, random);
            Optional<StructureTemplate> optional = level.getLevel().getStructureManager().get(templateId);
            if (optional.isEmpty()) {
               return false;
            } else {
               Rotation rotation = Rotation.getRandom(random);
               Mirror mirror = random.nextBoolean() ? Mirror.NONE : Mirror.LEFT_RIGHT;
               BlockPos templateOrigin = center.offset(-PIVOT.getX(), 0, -PIVOT.getZ());
               StructurePlaceSettings settings = new StructurePlaceSettings()
                  .setMirror(mirror)
                  .setRotation(rotation)
                  .setRotationPivot(PIVOT)
                  .setIgnoreEntities(true)
                  .setKnownShape(false)
                  .setRandom(random)
                  .addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
               if (!terrainFits(level, center, rotation)) {
                  return false;
               } else {
                  StructureTemplate template = optional.get();
                  List<StructureBlockInfo> naturalBlocks = new ArrayList<>();
                  naturalBlocks.addAll(template.filterBlocks(templateOrigin, settings, ModBlocks.SIFTSLATE));
                  naturalBlocks.addAll(template.filterBlocks(templateOrigin, settings, ModBlocks.SIFTSLATE_GROWTH));
                  naturalBlocks.addAll(template.filterBlocks(templateOrigin, settings, ModBlocks.HEALTHY_SCULK));
                  naturalBlocks.addAll(template.filterBlocks(templateOrigin, settings, ModBlocks.DRY_HEALTHY_SCULK));
                  naturalBlocks.addAll(template.filterBlocks(templateOrigin, settings, ModBlocks.DRY_HEALTHY_SCULK_GROWTH));
                  naturalBlocks.sort(Comparator.comparingInt(info -> info.pos().getY()));
                  List<StructureBlockInfo> structureBlocks = new ArrayList<>(naturalBlocks);
                  structureBlocks.addAll(template.filterBlocks(templateOrigin, settings, Blocks.CHEST));
                  if (!naturalBlocks.isEmpty() && allBlocksWritable(level, bounds, structureBlocks)) {
                     int baseY = naturalBlocks.getFirst().pos().getY();
                     List<BlockPos> blendedSurfaces = blendNaturalFoundation(level, bounds, random, naturalBlocks, baseY);
                     if (!template.placeInWorld(level, templateOrigin, templateOrigin, settings, random, 2)) {
                        return false;
                     } else {
                        for (StructureBlockInfo info : naturalBlocks) {
                           SiftSurfaceDecorator.decorateStructureSurface(level, random, info.pos(), 0.96F, 2);
                        }

                        for (BlockPos surface : blendedSurfaces) {
                           SiftSurfaceDecorator.decorate(level, random, surface, 0.72F, 2);
                        }

                        SiftLandmarkTracker.record(SiftLandmarkTracker.Kind.ABANDONED_MAIN_PORTAL, center);
                        return true;
                     }
                  } else {
                     return false;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   private static String[] templateIds(String prefix) {
      String[] ids = new String[5];

      for (int index = 0; index < ids.length; index++) {
         ids[index] = prefix + (index + 1);
      }

      return ids;
   }

   private static ResourceLocation chooseTemplate(WorldGenLevel level, BlockPos center, RandomSource random) {
      String biomePath = level.getBiome(center).unwrapKey().map(key -> key.location().getPath()).orElse("");
      boolean overgrown = biomePath.startsWith("overgrown_") || "ichor_snowy_peaks".equals(biomePath);
      String[] templates = overgrown ? OVERGROWN_TEMPLATES : WASTES_TEMPLATES;
      return ResourceLocation.fromNamespaceAndPath("the_sift", templates[random.nextInt(templates.length)]);
   }

   private static boolean terrainFits(WorldGenLevel level, BlockPos center, Rotation rotation) {
      int min = Integer.MAX_VALUE;
      int max = Integer.MIN_VALUE;

      for (int lateral = -4; lateral <= 4; lateral += 4) {
         for (int lengthwise = -16; lengthwise <= 16; lengthwise += 8) {
            BlockPos sample = rotateOffset(center, lateral, lengthwise, rotation);
            int height = level.getHeight(Types.WORLD_SURFACE_WG, sample.getX(), sample.getZ()) - 1;
            min = Math.min(min, height);
            max = Math.max(max, height);
            if (max - min > 20) {
               return false;
            }
         }
      }

      return true;
   }

   private static BlockPos rotateOffset(BlockPos center, int x, int z, Rotation rotation) {
      return switch (rotation) {
         case NONE -> center.offset(x, 0, z);
         case CLOCKWISE_90 -> center.offset(-z, 0, x);
         case CLOCKWISE_180 -> center.offset(-x, 0, -z);
         case COUNTERCLOCKWISE_90 -> center.offset(z, 0, -x);
         default -> throw new MatchException(null, null);
      };
   }

   private static boolean allBlocksWritable(WorldGenLevel level, SiftWorldgenBounds bounds, List<StructureBlockInfo> blocks) {
      for (StructureBlockInfo info : blocks) {
         if (!bounds.contains(info.pos()) || !level.ensureCanWrite(info.pos()) || !canReplace(level.getBlockState(info.pos()))) {
            return false;
         }
      }

      return true;
   }

   private static List<BlockPos> blendNaturalFoundation(
      WorldGenLevel level, SiftWorldgenBounds bounds, RandomSource random, List<StructureBlockInfo> naturalBlocks, int baseY
   ) {
      List<BlockPos> baseColumns = new ArrayList<>();
      Set<Long> baseKeys = new HashSet<>();
      int minX = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int minZ = Integer.MAX_VALUE;
      int maxZ = Integer.MIN_VALUE;

      for (StructureBlockInfo info : naturalBlocks) {
         BlockPos pos = info.pos();
         if (pos.getY() == baseY) {
            long key = BlockPos.asLong(pos.getX(), 0, pos.getZ());
            if (baseKeys.add(key)) {
               baseColumns.add(new BlockPos(pos.getX(), baseY, pos.getZ()));
               minX = Math.min(minX, pos.getX());
               maxX = Math.max(maxX, pos.getX());
               minZ = Math.min(minZ, pos.getZ());
               maxZ = Math.max(maxZ, pos.getZ());
            }
         }
      }

      if (baseColumns.isEmpty()) {
         return List.of();
      } else {
         long noiseSalt = random.nextLong();
         List<BlockPos> blendedSurfaces = new ArrayList<>();
         MutableBlockPos cursor = new MutableBlockPos();

         for (int x = minX - 15; x <= maxX + 15; x++) {
            for (int z = minZ - 15; z <= maxZ + 15; z++) {
               if (bounds.contains(x, z)) {
                  double distance = distanceToBase(x, z, baseColumns);
                  double broadNoise = blendNoise(x, z, noiseSalt);
                  double edge = 15.0 + broadNoise * 1.35;
                  if (!(distance > edge)) {
                     boolean core = baseKeys.contains(BlockPos.asLong(x, 0, z));
                     double fineNoise = blendNoise(x * 3 + 17, z * 3 - 29, noiseSalt ^ -7046029254386353131L);
                     double erosion = distance * (0.62 + fineNoise * 0.075) + distance * distance * 0.021;
                     int targetTop = core ? baseY - 1 : baseY - 1 - Math.max(1, (int)Math.floor(erosion));
                     int surfaceY = SiftMonolithFeature.findTerrainSurface(level, x, z);
                     if (surfaceY >= 0) {
                        int naturalTop = surfaceY - 1;
                        if (naturalTop < targetTop) {
                           cursor.set(x, naturalTop, z);
                           BlockState originalTop = level.getBlockState(cursor);
                           if (SiftMonolithFeature.isSiftTerrain(originalTop)) {
                              boolean complete = true;

                              for (int y = naturalTop + 1; y <= targetTop; y++) {
                                 cursor.set(x, y, z);
                                 BlockState current = level.getBlockState(cursor);
                                 if (!bounds.contains(cursor) || !level.ensureCanWrite(cursor) || !canReplace(current)) {
                                    complete = false;
                                    break;
                                 }

                                 level.setBlock(cursor, ModBlocks.SIFTSLATE.defaultBlockState(), 2);
                              }

                              if (complete) {
                                 cursor.set(x, targetTop, z);
                                 BlockState surfaceState = chooseBlendedSurface(originalTop, core, distance);
                                 level.setBlock(cursor, surfaceState, 2);
                                 if (!core && distance > 2.5) {
                                    blendedSurfaces.add(cursor.immutable());
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return blendedSurfaces;
      }
   }

   private static BlockState chooseBlendedSurface(BlockState originalTop, boolean core, double distance) {
      if (core || distance < 2.5) {
         return ModBlocks.SIFTSLATE.defaultBlockState();
      } else if (originalTop.is(ModBlocks.SIFTSLATE_GROWTH)) {
         return ModBlocks.SIFTSLATE_GROWTH.defaultBlockState();
      } else if (originalTop.is(ModBlocks.DRY_HEALTHY_SCULK_GROWTH)) {
         return ModBlocks.DRY_HEALTHY_SCULK_GROWTH.defaultBlockState();
      } else if (originalTop.is(ModBlocks.HEALTHY_SCULK)) {
         return ModBlocks.HEALTHY_SCULK.defaultBlockState();
      } else {
         return originalTop.is(ModBlocks.DRY_HEALTHY_SCULK) ? ModBlocks.DRY_HEALTHY_SCULK.defaultBlockState() : ModBlocks.SIFTSLATE.defaultBlockState();
      }
   }

   private static double distanceToBase(int x, int z, List<BlockPos> baseColumns) {
      int bestSquared = Integer.MAX_VALUE;

      for (BlockPos base : baseColumns) {
         int dx = x - base.getX();
         int dz = z - base.getZ();
         bestSquared = Math.min(bestSquared, dx * dx + dz * dz);
         if (bestSquared == 0) {
            return 0.0;
         }
      }

      return Math.sqrt((double)bestSquared);
   }

   private static double blendNoise(int x, int z, long salt) {
      double phase = (double)(salt & 65535L) * 1.91E-4;
      double broad = Math.sin((double)x * 0.087 + (double)z * 0.039 + phase);
      double cross = Math.cos((double)x * 0.043 - (double)z * 0.074 - phase * 0.71);
      double detail = Math.sin((double)(x + z) * 0.023 + phase * 1.83);
      return broad * 0.5 + cross * 0.33 + detail * 0.17;
   }

   private static boolean canReplace(BlockState state) {
      return state.isAir()
         || isSiftTerrain(state)
         || state.is(ModBlocks.OVERGROWN_FRONDS)
         || state.is(ModBlocks.OVERGROWN_CHARD)
         || state.is(ModBlocks.OVERGROWN_STALKS)
         || state.is(ModBlocks.SIFTSLATE_STALKS)
         || state.is(ModBlocks.HEALTHY_SCULK_SPROUTS)
         || state.is(ModBlocks.DRY_HEALTHY_SCULK_SPROUTS)
         || state.is(ModBlocks.SIFTSLATE_HANGING_ROOTS)
         || state.is(ModBlocks.OVERGROWN_HANGING_ROOTS);
   }

   private static boolean isSiftTerrain(BlockState state) {
      return state.is(ModBlocks.SIFTSLATE)
         || state.is(ModBlocks.SIFTSLATE_GROWTH)
         || state.is(ModBlocks.HEALTHY_SCULK)
         || state.is(ModBlocks.DRY_HEALTHY_SCULK)
         || state.is(ModBlocks.DRY_HEALTHY_SCULK_GROWTH);
   }
}
