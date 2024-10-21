package net.secretplaysmc.secrets_magic.spells.modifiers;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SpellModifierRegistry {
    private static final Map<String, Function<CompoundTag, SpellModifier>> registry = new HashMap<>();

    static {
        registry.put("amplificationModifier", AmplificationModifier::fromNBT);
        registry.put("damageBoostModifier", DamageBoostModifier::fromNBT);
        registry.put("durationModifier", DurationModifier::fromNBT);
        registry.put("aoe", AoEModifier::fromNBT);
    }

    public static Function<CompoundTag, SpellModifier> getModifier(String type) {
        return registry.get(type);
    }

    public static List<String> getAllModifierNames() {
        return new ArrayList<>(registry.keySet());
    }
}
