package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public class SkillTreeNode {
    private final String name;
    private final Predicate<Player> unlockCondition;
    private boolean isUnlocked;

    public SkillTreeNode(String name, Predicate<Player> unlockCondition) {
        this.name = name;
        this.unlockCondition = unlockCondition;
        this.isUnlocked = false;
    }

    public String getName() {
        return name;
    }

    public boolean isUnlocked(Player player) {
        return player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS)
                .map(skills -> skills.isNodeUnlocked(this.getName()))
                .orElse(false);
    }

    public Predicate<Player> getUnlockCondition() {
        return unlockCondition;
    }

    public boolean canUnlock(Player player) {
        return unlockCondition.test(player);
    }

    public void unlock(Player player) {
        player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
            skills.unlockNode(this.getName());
            skills.syncSkillsWithClient((ServerPlayer) player);
        });
    }
}
