package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.server.level.ServerPlayer;

public class SkillTreeHandler {
    public static void openSkillTree(ServerPlayer player) {
        player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
            if (!skills.isNodeUnlocked(ModSkillTree.starterNode.getName())) {
                ModSkillTree.starterNode.unlock(player);
                skills.unlockNode(ModSkillTree.starterNode.getName());
            }

            if (!skills.isNodeUnlocked(ModSkillTree.arrowNode.getName()) && ModSkillTree.arrowNode.canUnlock(player)) {
                ModSkillTree.arrowNode.unlock(player);
                skills.unlockNode(ModSkillTree.arrowNode.getName());
            }
        });
    }
}
