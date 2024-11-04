package net.secretplaysmc.secrets_magic.spells;


import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.secretplaysmc.secrets_magic.spells.effects.BuffEffect;
import net.secretplaysmc.secrets_magic.spells.effects.DimensionHop;
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
        SPELLS.put("DimensionHop", new Spell(
                "DimensionHop",
                50,
                20,
                new DimensionHop(),
                Arrays.asList(),
                new InstantTrigger()
        ));

        SPELLS.put("Fireball", new Spell(
                "Fireball",
                50,
                20,
                new FireballEffect(),
                Arrays.asList(new DamageBoostModifier(3)),
                new InstantTrigger()
        ));

        SPELLS.put("Recursive Fireball", new Spell(
                "Recursive Fireball",
                50,
                20,
                new FireballEffect(),
                Arrays.asList(new DamageBoostModifier(3), new AoEModifier(3, false)),
                new InstantTrigger()
        ));

        SPELLS.put("Arrow Storm", new Spell(
                "Arrow Storm",
                50,
                20,
                new ProjectileEffect(),
                Arrays.asList(new DamageBoostModifier(3), new AoEModifier(3, true)),
                new InstantTrigger()
        ));

        SPELLS.put("Arrow Scattershot", new Spell(
                "Arrow Scattershot",
                50,
                20,
                new ProjectileEffect(),
                Arrays.asList(new DamageBoostModifier(3), new AoEModifier(3, false)),
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
