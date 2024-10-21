package net.secretplaysmc.secrets_magic.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;

public class ModKeybindings {
    public static final String CATEGORY = "key.categories.secretsmagic";
    public static final String KEY_SKILL_TREE = "key.secretsmagic.skill_tree";

    public static KeyMapping openSkillTreeKey;

    public static void register() {
        openSkillTreeKey = new KeyMapping(KEY_SKILL_TREE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, openSkillTreeKey);
    }
}
