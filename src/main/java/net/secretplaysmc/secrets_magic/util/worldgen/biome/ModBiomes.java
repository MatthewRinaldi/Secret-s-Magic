package net.secretplaysmc.secrets_magic.util.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.secretplaysmc.secrets_magic.SecretsMagic;

public class ModBiomes {
    public static final ResourceKey<Biome> CUSTOM_OCEAN_BIOME = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(SecretsMagic.MOD_ID, "custom_ocean_biome"));
}
