package net.secretplaysmc.secrets_magic.spells.triggers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.Spell;

public class InstantTrigger implements SpellTrigger {
    @Override
    public void execute(Level world, ServerPlayer player, Spell spell) {
        spell.getEffect().apply(world, player, player.getItemInHand(InteractionHand.MAIN_HAND), spell.getModifiers());
    }
}