package net.secretplaysmc.secrets_magic.spells.triggers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.Spell;

public interface SpellTrigger {
    void execute(Level world, ServerPlayer player, Spell spell);
}
