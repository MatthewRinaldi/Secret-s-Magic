package net.secretplaysmc.secrets_magic.spells;


import net.minecraft.world.effect.MobEffects;
import net.secretplaysmc.secrets_magic.spells.effects.BuffEffect;
import net.secretplaysmc.secrets_magic.spells.effects.FireballEffect;
import net.secretplaysmc.secrets_magic.spells.effects.ProjectileEffect;
import net.secretplaysmc.secrets_magic.spells.modifiers.AmplificationModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.DamageBoostModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.DurationModifier;
import net.secretplaysmc.secrets_magic.spells.triggers.InstantTrigger;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModSpells {
    private static final Map<String, Spell> SPELLS = new HashMap<>();

    static {
        SPELLS.put("arrow", new Spell(
                "arrow",
                30,
                20,
                new ProjectileEffect(),
                Arrays.asList(new DamageBoostModifier(1)),
                new InstantTrigger()
        ));

        SPELLS.put("heal", new Spell(
                "heal",
                50,
                20,
                new BuffEffect(MobEffects.REGENERATION, 200, 0),
                Arrays.asList(new DurationModifier(400), new AmplificationModifier(2)),
                new InstantTrigger()
        ));

        SPELLS.put("fireball", new Spell(
                "fireball",
                80,
                20,
                new FireballEffect(),
                Arrays.asList(new AmplificationModifier(1), new DamageBoostModifier(0)),
                new InstantTrigger()
        ));
    }

    public static Spell getSpell(String name) {
        return SPELLS.get(name);
    }

    public static Collection<Spell> getAllSpells() {
        return SPELLS.values();
    }

}
