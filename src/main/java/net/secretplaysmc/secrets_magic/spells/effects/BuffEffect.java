package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;

import java.util.List;

public class BuffEffect implements SpellEffect{
    private final MobEffect buff;
    private int duration;
    private int amplifier = 0;


    public BuffEffect (MobEffect buff, int defaultDuration) {
        this.buff = buff;
        this.duration = defaultDuration;
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

    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("effectType", "buffEffect");

        ResourceLocation effectKey = ForgeRegistries.MOB_EFFECTS.getKey(this.buff);
        if (effectKey != null) {
            tag.putString("buff", effectKey.toString());
        }
        tag.putInt("duration", this.duration);

        return tag;
    }

    public static BuffEffect fromNBT(CompoundTag tag) {
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(tag.getString("buff")));

        int duration = tag.getInt("duration");

        return new BuffEffect(effect, duration);
    }
}
