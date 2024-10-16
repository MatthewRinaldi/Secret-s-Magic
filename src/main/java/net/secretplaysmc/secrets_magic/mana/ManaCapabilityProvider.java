package net.secretplaysmc.secrets_magic.mana;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerMana> MANA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    public final LazyOptional<PlayerMana> instance = LazyOptional.of(PlayerMana::new);
    private final PlayerMana playerMana = new PlayerMana();
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        instance.ifPresent(mana -> {
            tag.putInt("mana", mana.getMana());
            tag.putInt("maxMana", mana.getMaxMana());
        });
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.ifPresent(mana -> mana.setMana(nbt.getInt("mana")));
    }

    public PlayerMana getPlayerMana() {
        return playerMana;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ManaCapabilityProvider.MANA_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }
}
