package net.secretplaysmc.secrets_magic.spells;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.secretplaysmc.secrets_magic.ModCapabilities;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class PlayerSpellsProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<PlayerSpells> PLAYER_SPELLS = CapabilityManager.get(new CapabilityToken<PlayerSpells>(){});

    private final LazyOptional<PlayerSpells> instance = LazyOptional.of(PlayerSpells::new);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.PLAYER_SPELLS ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag spellList = new ListTag();
        instance.ifPresent(spells -> {
            for (String spell : spells.getLearnedSpells()) {
                spellList.add(StringTag.valueOf(spell));
            }
        });
        tag.put("learnedSpells", spellList);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag spellList = nbt.getList("learnedSpells", Tag.TAG_STRING);
        instance.ifPresent(spells -> {
            spells.clearSpells();  // Clear existing spells first
            for (Tag spellTag : spellList) {
                spells.learnSpell(spellTag.getAsString());
            }
        });
    }

}
