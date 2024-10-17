package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;

import java.util.List;

public interface SpellEffect {
    void apply(Level world, ServerPlayer player, ItemStack wandItem, List<SpellModifier> modifiers);
}
