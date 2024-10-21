package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.secretplaysmc.secrets_magic.network.ModNetworking;
import net.secretplaysmc.secrets_magic.network.SkillsSyncPacket;

import java.util.HashSet;
import java.util.Set;

public class PlayerSkills {
    private final Set<String> unlockedNodes = new HashSet<>();

    public Set<String> getUnlockedNodes() {
        return unlockedNodes;
    }

    public void unlockNode(String node) {
        unlockedNodes.add(node);
    }

    public boolean isNodeUnlocked(String node) {
        return unlockedNodes.contains(node);
    }

    public void clearNodes() {
        unlockedNodes.clear();
    }

    public void syncSkillsWithClient(ServerPlayer player) {
        ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SkillsSyncPacket(this.unlockedNodes));
    }

    public void saveNBTData(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();
        for (String node : unlockedNodes) {
            listTag.add(StringTag.valueOf(node));
        }
        compoundTag.put("unlockedNodes", listTag);
    }

    public void loadNBTData(CompoundTag compoundTag) {
        ListTag listTag = compoundTag.getList("unlockedNodes", Tag.TAG_STRING);
        unlockedNodes.clear();
        for (Tag tag : listTag) {
            unlockedNodes.add(tag.getAsString());
        }
    }
}
