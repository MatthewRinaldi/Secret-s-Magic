package net.secretplaysmc.secrets_magic.spells;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.secretplaysmc.secrets_magic.spells.effects.BuffEffect;
import net.secretplaysmc.secrets_magic.spells.effects.FireballEffect;
import net.secretplaysmc.secrets_magic.spells.effects.ProjectileEffect;
import net.secretplaysmc.secrets_magic.spells.modifiers.AmplificationModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.AoEModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.DamageBoostModifier;
import net.secretplaysmc.secrets_magic.spells.modifiers.DurationModifier;
import net.secretplaysmc.secrets_magic.spells.triggers.InstantTrigger;

import java.util.*;

public class ModSpells {
    private static final Map<String, Spell> SPELLS = new HashMap<>();
    private static Map<String, Spell> CUSTOM_SPELLS = new HashMap<>();

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
                new BuffEffect(MobEffects.REGENERATION),
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

        SPELLS.put("AOETEST_Arrow_Inverted", new Spell(
                "AOETEST_Arrow_Inverted",
                50,
                20,
                new ProjectileEffect(),
                Arrays.asList(new AoEModifier(5, true)),
                new InstantTrigger()
        ));

        SPELLS.put("AOETEST_Arrow_Not_Inverted", new Spell(
                "AOETEST_Arrow_Not_Inverted",
                50,
                20,
                new ProjectileEffect(),
                Arrays.asList(new AoEModifier(5, false)),
                new InstantTrigger()
        ));

        SPELLS.put("AOETEST_Buff", new Spell(
                "AOETEST_Buff",
                50,
                20,
                new BuffEffect(MobEffects.DAMAGE_RESISTANCE),
                Arrays.asList(new DurationModifier(600), new AmplificationModifier(2), new AoEModifier(5, false)),
                new InstantTrigger()
        ));

        SPELLS.put("AOETEST_Fireball", new Spell(
                "AOETEST_Fireball",
                50,
                20,
                new FireballEffect(),
                Arrays.asList(new AoEModifier(1, false)),
                new InstantTrigger()
        ));

        SPELLS.put("AOETEST_Fireball_Recursive3", new Spell(
                "AOETEST_Fireball_Recursive3",
                50,
                20,
                new FireballEffect(),
                Arrays.asList(new AoEModifier(5, false)),
                new InstantTrigger()
        ));
    }

    public static void loadCustomSpells(ServerLevel world) {
        CustomSpellsSavedData data = CustomSpellsSavedData.get(world);
        CUSTOM_SPELLS = data.getCustomSpells();
    }

    public static void saveCustomSpells(ServerLevel world) {
        CustomSpellsSavedData data = CustomSpellsSavedData.get(world);
    }

    public static void addCustomSpell(String name, Spell spell, ServerLevel world) {
        CUSTOM_SPELLS.put(name, spell);
        CustomSpellsSavedData data = CustomSpellsSavedData.get(world);
        data.addCustomSpell(name, spell);
    }

    public static void removeCustomSpell(String name) {
        CUSTOM_SPELLS.remove(name);
    }

    public static Spell getSpell(String name) {
        return SPELLS.containsKey(name) ? SPELLS.get(name) : CUSTOM_SPELLS.get(name);

    }

    public static Collection<Spell> getAllSpells() {
        List<Spell> allSpells = new ArrayList<>(SPELLS.values());
        allSpells.addAll(CUSTOM_SPELLS.values());
        return allSpells;
    }

}
