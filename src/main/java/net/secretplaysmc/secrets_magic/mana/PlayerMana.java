package net.secretplaysmc.secrets_magic.mana;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.secretplaysmc.secrets_magic.network.ManaSyncPacket;
import net.secretplaysmc.secrets_magic.network.ModNetworking;

public class PlayerMana {
    private int mana = 100;  // Default mana value
    private final int maxMana = 100;

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana));// Ensure mana stays within 0 and max
    }

    public void consumeMana(int amount) {
        setMana(this.mana - amount);  // Reduce mana by the specified amount
    }

    public void regenerateMana(int amount) {
        setMana(mana + amount);  // Increase mana by the specified amount
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void syncManaWithClient(ServerPlayer player) {
        ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ManaSyncPacket(this.mana));
    }
}
