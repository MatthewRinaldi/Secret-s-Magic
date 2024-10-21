package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SkillTreeNode {
    private final String name;
    private final Predicate<Player> unlockCondition;
    private final List<SkillTreeNode> requiredNodes;
    private final List<NodeRequirement> requirements;
    private final Consumer<Player> unlockAction;
    private final ResourceLocation texture;
    private final ResourceLocation textureLocked;
    private final int nodeLevel;

    public SkillTreeNode(String name, Predicate<Player> unlockCondition, List<NodeRequirement> requirements,
                         Consumer<Player> unlockAction, ResourceLocation texture, ResourceLocation textureLocked, int nodeLevel, SkillTreeNode... requiredNodes) {
        this.name = name;
        this.unlockCondition = unlockCondition;
        this.requiredNodes = Arrays.asList(requiredNodes);
        this.requirements = requirements != null ? requirements : Collections.emptyList();
        this.unlockAction = unlockAction;
        this.texture = texture;
        this.textureLocked = textureLocked;
        this.nodeLevel = nodeLevel;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public ResourceLocation getTextureLocked() {
        return textureLocked;
    }

    public int getNodeLevel() {
        return nodeLevel;
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
        return unlockCondition.test(player) && requiredNodes.stream().allMatch(skillTreeNode -> skillTreeNode.isUnlocked(player) &&
                (requirements.isEmpty() || requirements.stream().allMatch(nodeRequirement -> nodeRequirement.isSatisfied(player))));
    }

    public List<NodeRequirement> getRequirements() {
        return requirements;
    }

    public void unlock(Player player) {
        if (player.isCreative()) {
            player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                skills.unlockNode(this.getName());
                skills.syncSkillsWithClient((ServerPlayer) player);
            });
            return;
        }
        if (this.canUnlock(player)) {
            if (unlockAction != null) {
                unlockAction.accept(player);
            }
            player.getCapability(PlayerSkillsProvider.PLAYER_SKILLS).ifPresent(skills -> {
                skills.unlockNode(this.getName());
                skills.syncSkillsWithClient((ServerPlayer) player);
            });
        } else {
            player.sendSystemMessage(Component.literal("You do not meet the requirements to unlock " + this.getName()));
        }
    }
}
