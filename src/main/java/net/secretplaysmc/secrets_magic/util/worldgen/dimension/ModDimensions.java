package net.secretplaysmc.secrets_magic.util.worldgen.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.secretplaysmc.secrets_magic.SecretsMagic;

public class ModDimensions {
    public static final ResourceKey<LevelStem> SHOREDIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(SecretsMagic.MOD_ID, "shoredim"));
    public static final ResourceKey<Level> SHOREDIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(SecretsMagic.MOD_ID, "shoredim"));
    public static final ResourceKey<DimensionType> SHORE_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(SecretsMagic.MOD_ID, "shoredim"));
    public static final ResourceKey<DensityFunction> ISLAND_DF = ResourceKey.create(Registries.DENSITY_FUNCTION,
            new ResourceLocation(SecretsMagic.MOD_ID, "island"));
}


