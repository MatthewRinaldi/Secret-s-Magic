package net.secretplaysmc.secrets_magic.spells.triggers;

import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SpellTriggerRegistry {
    private static final Map<String, Function<CompoundTag, SpellTrigger>> registry = new HashMap<>();

    static {
        registry.put("instantTrigger", InstantTrigger::fromNBT);
    }

    public static Function<CompoundTag, SpellTrigger> getTrigger(String type) {
        return registry.get(type);
    }
}
