package net.secretplaysmc.secrets_magic.util.gui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.secretplaysmc.secrets_magic.util.ModContainers;
import org.jetbrains.annotations.Nullable;

public class CustomSpellContainer extends AbstractContainerMenu {
    public CustomSpellContainer(int pContainerId, @Nullable Inventory inventory) {
        super(ModContainers.CUSTOM_SPELL.get(), pContainerId);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}
