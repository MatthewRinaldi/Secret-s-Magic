package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SpellEffectRegistry {
    private static final Map<String, Function<CompoundTag, SpellEffect>> registry = new HashMap<>();

    static {
        registry.put("projectileEffect", ProjectileEffect::fromNBT);
        registry.put("fireballEffect", FireballEffect::fromNBT);
        registry.put("buffEffect", BuffEffect::fromNBT);
        registry.put("dimensionHop", DimensionHop::fromNBT);
    }

    public static Function<CompoundTag, SpellEffect> getEffect(String type) {
        return registry.get(type);
    }

    public static List<String> getAllEffectNames() {
        return new ArrayList<>(registry.keySet());
    }
}
