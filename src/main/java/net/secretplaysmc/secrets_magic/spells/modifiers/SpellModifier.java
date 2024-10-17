package net.secretplaysmc.secrets_magic.spells.modifiers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;

public interface SpellModifier {
    void modify(SpellEffect effect, Level world, ServerPlayer player, ItemStack wandItem);
}
