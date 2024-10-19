package net.secretplaysmc.secrets_magic.spells;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.secretplaysmc.secrets_magic.SecretsMagic;

import java.util.HashMap;
import java.util.Map;

public class CustomSpellsSavedData extends SavedData {
    private static final String DATA_NAME = SecretsMagic.MOD_ID + "_custom_spells";
    private final Map<String, Spell> customSpells = new HashMap<>();

    public CustomSpellsSavedData() {}

    public static CustomSpellsSavedData load(CompoundTag nbt) {
        CustomSpellsSavedData data = new CustomSpellsSavedData();
        ListTag spellList = nbt.getList("customSpells", ListTag.TAG_COMPOUND);
        for (Tag spellTag : spellList) {
            CompoundTag spellData = (CompoundTag) spellTag;
            String spellName = spellData.getString("name");

            Spell spell = Spell.fromNBT(spellData);
            data.customSpells.put(spellName, spell);
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag spellList = new ListTag();
        for (Spell spell : customSpells.values()) {
            CompoundTag spellData = new CompoundTag();
            spell.toNBT(spellData);
            spellList.add(spellData);
        }
        nbt.put("customSpells", spellList);
        return nbt;
    }

    public Map<String, Spell> getCustomSpells() {
        return customSpells;
    }

    public void addCustomSpell(String name, Spell spell) {
        customSpells.put(name, spell);
        setDirty();
    }

    public static CustomSpellsSavedData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(CustomSpellsSavedData::load, CustomSpellsSavedData::new, DATA_NAME);
    }
}
