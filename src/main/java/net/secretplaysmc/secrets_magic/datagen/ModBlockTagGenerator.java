package net.secretplaysmc.secrets_magic.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.block.ModBlocks;
import net.secretplaysmc.secrets_magic.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SecretsMagic.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(ModBlocks.SAPPHIRE_ORE.get()).addTag(Tags.Blocks.ORES);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SAPPHIRE_ORE.get(),
                        ModBlocks.SAPPHIRE_BLOCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.SAPPHIRE_ORE.get(),
                        ModBlocks.SAPPHIRE_BLOCK.get());
    }
}
