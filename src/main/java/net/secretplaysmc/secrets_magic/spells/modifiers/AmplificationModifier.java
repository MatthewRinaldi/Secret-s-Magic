package net.secretplaysmc.secrets_magic.spells.modifiers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.BuffEffect;
import net.secretplaysmc.secrets_magic.spells.effects.FireballEffect;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;

public class AmplificationModifier implements SpellModifier{

    private final int enhanceAmplification;

    public AmplificationModifier(int enhanceAmplification) {
        this.enhanceAmplification = enhanceAmplification;
    }

    @Override
    public void modify(SpellEffect effect, Level world, ServerPlayer player, ItemStack wandItem) {
        if (effect instanceof BuffEffect buffEffect) {
            buffEffect.increaseAmplification(enhanceAmplification);
        } else if (effect instanceof FireballEffect fireballEffect) {
            fireballEffect.amplifyVelocity((float) enhanceAmplification);
        }
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        tag.putString("modifierType", "amplificationModifier");

        tag.putInt("enhancement", this.enhanceAmplification);
        return tag;
    }

    public static AmplificationModifier fromNBT(CompoundTag tag) {

        int enhancement = tag.getInt("enhancement");

        return new AmplificationModifier(enhancement);
    }
}
