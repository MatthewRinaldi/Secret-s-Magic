package net.secretplaysmc.secrets_magic.spells.modifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffectRegistry;

import java.util.function.Function;

public interface SpellModifier {
    void modify(SpellEffect effect, Level world, ServerPlayer player, ItemStack wandItem);
    CompoundTag toNBT();
    static SpellModifier fromNBT(CompoundTag tag) {
        String type = tag.getString("modifierType");

        Function<CompoundTag, SpellModifier> effectDeserializer = SpellModifierRegistry.getModifier(type);

        if (effectDeserializer != null) {
            return effectDeserializer.apply(tag);
        } else {
            throw new IllegalArgumentException("Unknown SpellModifier type: " + type);
        }
    }
}
