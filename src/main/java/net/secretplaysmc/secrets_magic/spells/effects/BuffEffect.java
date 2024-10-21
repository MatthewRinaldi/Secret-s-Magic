package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.secretplaysmc.secrets_magic.spells.modifiers.AoEModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;

import java.util.List;

public class BuffEffect implements SpellEffect{
    private final MobEffect buff;
    private int duration = 200;
    private int amplifier = 0;


    public BuffEffect (MobEffect buff) {
        this.buff = buff;
    }

    @Override
    public void apply(Level world, ServerPlayer player, ItemStack wandItem, List<SpellModifier> modifiers) {
        boolean isAOE = false;
        for (SpellModifier modifier : modifiers) {
            modifier.modify(this, world, player, wandItem);
            if (modifier instanceof AoEModifier) {
                isAOE = true;
            }
        }
        if (!isAOE) {
            player.addEffect(new MobEffectInstance(buff, duration, amplifier, false, false, true));
        }
    }

    public void increaseDuration(int extendedDuration) {
        this.duration += extendedDuration;
    }

    public void increaseAmplification(int enhanceAmplification) {
        this.amplifier += enhanceAmplification;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }

    public int getDuration() {
        return duration;
    }

    public MobEffect getBuff() {
        return buff;
    }

    public int getAmplifier() {
        return amplifier;
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("effectType", "buffEffect");

        ResourceLocation effectKey = ForgeRegistries.MOB_EFFECTS.getKey(this.buff);
        if (effectKey != null) {
            tag.putString("buff", effectKey.toString());
        }
        tag.putInt("duration", this.duration);
        tag.putInt("amplifier", this.amplifier);

        return tag;
    }

    public static BuffEffect fromNBT(CompoundTag tag) {
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(tag.getString("buff")));

        BuffEffect buffEffect = new BuffEffect(effect);
        buffEffect.setDuration(tag.getInt("duration"));
        buffEffect.setAmplifier(tag.getInt("amplifier"));

        return buffEffect;
    }
}
