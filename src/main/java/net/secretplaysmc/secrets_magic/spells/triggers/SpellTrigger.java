package net.secretplaysmc.secrets_magic.spells.triggers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.Spell;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifierRegistry;

import java.util.function.Function;

public interface SpellTrigger {
    void execute(Level world, ServerPlayer player, Spell spell);
    CompoundTag toNBT();
    static SpellTrigger fromNBT(CompoundTag tag) {
        String type = tag.getString("triggerType");

        Function<CompoundTag, SpellTrigger> effectDeserializer = SpellTriggerRegistry.getTrigger(type);

        if (effectDeserializer != null) {
            return effectDeserializer.apply(tag);
        } else {
            throw new IllegalArgumentException("Unknown SpellTrigger type: " + type);
        }
    }
}
