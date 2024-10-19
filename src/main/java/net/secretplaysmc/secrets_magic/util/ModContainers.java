package net.secretplaysmc.secrets_magic.util;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.util.gui.CustomSpellContainer;

public class ModContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SecretsMagic.MOD_ID);

    public static final RegistryObject<MenuType<CustomSpellContainer>> CUSTOM_SPELL = CONTAINERS.register("custom_spell",
            () -> IForgeMenuType.create(((windowId, inv, data) -> new CustomSpellContainer(windowId, inv))));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
