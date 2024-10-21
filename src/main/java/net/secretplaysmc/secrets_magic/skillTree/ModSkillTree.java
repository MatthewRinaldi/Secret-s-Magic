package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModSkillTree {
    public static SkillTreeNode starterNode = new SkillTreeNode("starter", player -> true);
    public static SkillTreeNode arrowNode = new SkillTreeNode("arrow", player -> {
        boolean hasArrow = player.getInventory().contains(new ItemStack(Items.ARROW));
        player.sendSystemMessage(Component.literal("Has Arrow: " + hasArrow));
        return hasArrow;
    });

    private static final Map<String, SkillTreeNode> NODE_MAP = new HashMap<>();

    static {
        NODE_MAP.put(starterNode.getName(), starterNode);
        NODE_MAP.put(arrowNode.getName(), arrowNode);
    }

    public static SkillTreeNode getNodeByName(String name) {
        return NODE_MAP.get(name);
    }
}
