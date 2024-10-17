package net.secretplaysmc.secrets_magic.spells;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;
import net.secretplaysmc.secrets_magic.spells.triggers.SpellTrigger;

import java.util.List;

public class Spell {
    private final String name;
    private final int manaCost;
    private final int castTime;
    private final SpellEffect effect;
    private final List<SpellModifier> modifiers;
    private final SpellTrigger trigger;

    public Spell(String name, int manaCost, int castTime, SpellEffect effect, List<SpellModifier> modifiers, SpellTrigger trigger) {
        this.name = name;
        this.manaCost = manaCost;
        this.castTime = castTime;
        this.effect = effect;
        this.modifiers = modifiers;
        this.trigger = trigger;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void cast(Level world, ServerPlayer player, ItemStack wandItem) {
        trigger.execute(world, player, this);
    }

    public SpellEffect getEffect() {
        return effect;
    }

    public List<SpellModifier> getModifiers() {
        return modifiers;
    }

    public String getSpellName() {
        return name;
    }
}
