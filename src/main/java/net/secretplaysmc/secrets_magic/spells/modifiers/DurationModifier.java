package net.secretplaysmc.secrets_magic.spells.modifiers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.BuffEffect;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;

public class DurationModifier implements SpellModifier{

    private final int extendedDuration;

    public DurationModifier(int extendedDuration) {
        this.extendedDuration = extendedDuration;
    }

    @Override
    public void modify(SpellEffect effect, Level world, ServerPlayer player, ItemStack wandItem) {
        if (effect instanceof BuffEffect buffEffect) {
            buffEffect.increaseDuration(extendedDuration);
        }
    }
}
