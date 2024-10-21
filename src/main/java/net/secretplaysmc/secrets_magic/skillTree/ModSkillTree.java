package net.secretplaysmc.secrets_magic.skillTree;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.secretplaysmc.secrets_magic.SecretsMagic;

import java.util.*;

public class ModSkillTree {
    public static final ResourceLocation STARTER_NODE_TEXTURE = new ResourceLocation(SecretsMagic.MOD_ID, "textures/gui/skills/starter_node.png");
    public static final ResourceLocation ARROW_NODE_TEXTURE = new ResourceLocation(SecretsMagic.MOD_ID, "textures/gui/skills/arrow_node.png");
    public static final ResourceLocation STARTER_NODE_LOCKED_TEXTURE = new ResourceLocation(SecretsMagic.MOD_ID, "textures/gui/skills/starter_node_locked.png");
    public static final ResourceLocation ARROW_NODE_LOCKED_TEXTURE = new ResourceLocation(SecretsMagic.MOD_ID, "textures/gui/skills/arrow_node_locked.png");

    public static SkillTreeNode starterNode = new SkillTreeNode(
            "starter",
            player -> true,
            Collections.emptyList(),
            null,
            STARTER_NODE_TEXTURE,
            STARTER_NODE_LOCKED_TEXTURE,
            1
    );
    public static SkillTreeNode arrowNode = new SkillTreeNode(
            "arrow",
            player -> player.getInventory().countItem(Items.ARROW) >= 32,
            List.of(new NodeRequirement(new ItemStack(Items.ARROW, 32), player -> player.getInventory().countItem(Items.ARROW) >= 32)),
            player -> {
                int arrowsToRemove = 32;
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() == Items.ARROW) {
                        int removed = Math.min(arrowsToRemove, stack.getCount());
                        stack.shrink(removed);
                        arrowsToRemove -= removed;
                        if (arrowsToRemove <= 0) {
                            break; // We have removed enough arrows
                        }
                    }
                }
            },
            ARROW_NODE_TEXTURE,
            ARROW_NODE_LOCKED_TEXTURE,
            0,
            starterNode
    );

    public static final Map<String, SkillTreeNode> NODE_MAP = new HashMap<>();

    static {
        NODE_MAP.put(starterNode.getName(), starterNode);
        NODE_MAP.put(arrowNode.getName(), arrowNode);
    }

    public static SkillTreeNode getNodeByName(String name) {
        return NODE_MAP.get(name);
    }
}
