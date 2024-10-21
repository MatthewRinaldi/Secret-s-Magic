package net.secretplaysmc.secrets_magic.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.secretplaysmc.secrets_magic.skillTree.ModSkillTree;
import net.secretplaysmc.secrets_magic.skillTree.PlayerSkillsProvider;
import net.secretplaysmc.secrets_magic.skillTree.SkillTreeNode;

import java.util.function.Supplier;

public class UnlockNodePacket {
    private final String nodeName;

    public UnlockNodePacket(String nodeName) {
        this.nodeName = nodeName;
    }

    public UnlockNodePacket(FriendlyByteBuf buf) {
        this.nodeName = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(nodeName);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.get().getSender();
            if (serverPlayer != null) {
                SkillTreeNode node = ModSkillTree.getNodeByName(nodeName);
                if (node != null && !node.isUnlocked(serverPlayer) && node.canUnlock(serverPlayer)) {
                    node.unlock(serverPlayer);
                    serverPlayer.sendSystemMessage(Component.literal("Unlocked " + node.getName()));
                } else {
                    serverPlayer.sendSystemMessage(Component.literal("Node already unlocked!"));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
