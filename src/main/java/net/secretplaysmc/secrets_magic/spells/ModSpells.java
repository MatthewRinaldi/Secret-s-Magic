package net.secretplaysmc.secrets_magic.spells;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModSpells {
    // You can use RegistryObject to register spells dynamically
    private static final Map<String, Spell> SPELLS = new HashMap<>();

    static {
        SPELLS.put("arrow", new Spell("arrow", 30, SpellType.PROJECTILE));
        SPELLS.put("heal", new Spell("heal", 40, SpellType.BUFF));
    }

    public static Spell getSpell(String name) {
        return SPELLS.get(name);
    }

    public static Collection<Spell> getAllSpells() {
        return SPELLS.values();
    }

}
