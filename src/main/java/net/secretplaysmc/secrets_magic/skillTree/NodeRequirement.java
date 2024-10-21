package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class NodeRequirement {
    private final ItemStack requiredItem;
    private final Predicate<Player> requirement;

    public NodeRequirement (ItemStack requiredItem, Predicate<Player> requirement) {
        this.requiredItem = requiredItem;
        this.requirement = requirement;
    }

    public ItemStack getRequiredItem() {
        return requiredItem;
    }

    public boolean isSatisfied(Player player) {
        return requirement.test(player);
    }
}
