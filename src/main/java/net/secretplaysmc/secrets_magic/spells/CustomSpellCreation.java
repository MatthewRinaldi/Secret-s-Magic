package net.secretplaysmc.secrets_magic.spells;

import net.minecraft.world.effect.MobEffects;
import net.secretplaysmc.secrets_magic.spells.effects.BuffEffect;
import net.secretplaysmc.secrets_magic.spells.effects.FireballEffect;
import net.secretplaysmc.secrets_magic.spells.effects.ProjectileEffect;
import net.secretplaysmc.secrets_magic.spells.effects.SpellEffect;
import net.secretplaysmc.secrets_magic.spells.modifiers.*;
import net.secretplaysmc.secrets_magic.spells.triggers.InstantTrigger;
import net.secretplaysmc.secrets_magic.spells.triggers.SpellTrigger;

import java.util.ArrayList;
import java.util.List;

public class CustomSpellCreation {
    public static Spell createCustomSpell(String type, String modifier, String trigger) {
        SpellEffect effect = getEffectForType(type);
        List<SpellModifier> modifiers = getModifiersForModifier(modifier);
        SpellTrigger spellTrigger = getTriggerForTrigger(trigger);

        return new Spell(type + "_" + modifier + "_" + trigger, 50, 20, effect, modifiers, spellTrigger);
    }

    private static SpellEffect getEffectForType(String type) {
        // Logic to convert type string into a SpellEffect
        // For example, if (type.equals("Arrow")) return new ProjectileEffect();
        if (type.equals("Arrow")) {
            return new ProjectileEffect();
        } else if (type.equals("Fireball")) {
            return new FireballEffect();
        } else if (type.equals("Heal")) {
            return new BuffEffect(MobEffects.REGENERATION);
        }
        return new ProjectileEffect();
    }

    private static List<SpellModifier> getModifiersForModifier(String modifier) {
        // Logic to convert modifier string into a list of SpellModifiers
        List<SpellModifier> modifiers = new ArrayList<>();
        if (modifier.contains("Amplification")) {
            modifiers.add(new AmplificationModifier(1));
        } else if (modifier.contains("DamageBonus")) {
            modifiers.add(new DamageBoostModifier(1));
        } else if (modifier.contains("Duration")) {
            modifiers.add(new DurationModifier(600));
        }
        return modifiers;
    }

    private static SpellTrigger getTriggerForTrigger(String trigger) {
        // Logic to convert trigger string into a SpellTrigger
        if (trigger.equals("Instant")) {
            return new InstantTrigger();
        }
        return new InstantTrigger();
    }
}
