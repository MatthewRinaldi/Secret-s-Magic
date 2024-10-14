package net.secretplaysmc.secrets_magic.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.block.ModBlocks;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SecretsMagic.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SECRETS_TAB = CREATIVE_MODE_TABS.register("secrets_magic",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SAPPHIRE.get()))
                    .title(Component.translatable("creativetab.secrets_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.SAPPHIRE.get());
                        pOutput.accept(ModItems.METAL_DETECTOR.get());

                        pOutput.accept(ModBlocks.SAPPHIRE_BLOCK.get());
                        pOutput.accept(ModBlocks.SAPPHIRE_ORE.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
