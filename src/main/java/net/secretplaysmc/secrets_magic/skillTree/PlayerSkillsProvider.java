package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PlayerSkillsProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<PlayerSkills> PLAYER_SKILLS = CapabilityManager.get(new CapabilityToken<PlayerSkills>(){});
    private final LazyOptional<PlayerSkills> instance = LazyOptional.of(PlayerSkills::new);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == PLAYER_SKILLS ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        instance.ifPresent(skills -> {
            Set<String> unlockedNodes = skills.getUnlockedNodes();
            ListTag listTag = new ListTag();
            for (String node : unlockedNodes) {
                if (!listTag.contains(StringTag.valueOf(node))) {
                    listTag.add(StringTag.valueOf(node));
                }
            }
            tag.put("unlockedNodes", listTag);
        });
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.ifPresent(skills -> {
            ListTag unlockedNodes = nbt.getList("unlockedNodes", Tag.TAG_STRING);
            skills.clearNodes();
            for (Tag nodeTag : unlockedNodes) {
                skills.unlockNode(nodeTag.getAsString());
            }
        });
    }
}
