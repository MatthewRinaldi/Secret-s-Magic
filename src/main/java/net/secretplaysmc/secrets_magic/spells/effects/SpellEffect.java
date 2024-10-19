package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;

import java.util.List;
import java.util.function.Function;

public interface SpellEffect {
    void apply(Level world, ServerPlayer player, ItemStack wandItem, List<SpellModifier> modifiers);
    CompoundTag toNBT();
    static SpellEffect fromNBT(CompoundTag tag) {
        String type = tag.getString("effectType");

        Function<CompoundTag, SpellEffect> effectDeserializer = SpellEffectRegistry.getEffect(type);

        if (effectDeserializer != null) {
            return effectDeserializer.apply(tag);
        } else {
            throw new IllegalArgumentException("Unknown SpellEffect type: " + type);
        }
    }
}
