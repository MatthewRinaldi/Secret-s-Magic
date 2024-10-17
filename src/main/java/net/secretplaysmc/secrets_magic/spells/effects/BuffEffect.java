package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;

import java.util.List;

public class BuffEffect implements SpellEffect{
    private final MobEffect buff;
    private int duration;
    private int amplifier;


    public BuffEffect (MobEffect buff, int defaultDuration, int defaultAmplifier) {
        this.buff = buff;
        this.duration = defaultDuration;
        this.amplifier = defaultAmplifier;
    }

    @Override
    public void apply(Level world, ServerPlayer player, ItemStack wandItem, List<SpellModifier> modifiers) {
        for (SpellModifier modifier : modifiers) {
            modifier.modify(this, world, player, wandItem);
        }

        player.addEffect(new MobEffectInstance(buff, duration, amplifier, false, false, true));
    }

    public void increaseDuration(int extendedDuration) {
        this.duration += extendedDuration;
    }

    public void increaseAmplification(int enhanceAmplification) {
        this.amplifier += enhanceAmplification;
    }
}
