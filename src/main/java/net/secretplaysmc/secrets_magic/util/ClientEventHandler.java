package net.secretplaysmc.secrets_magic.util;

import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.secretplaysmc.secrets_magic.SecretsMagic;
import net.secretplaysmc.secrets_magic.skillTree.SkillTreeScreen;
import net.secretplaysmc.secrets_magic.util.worldgen.dimension.ModDimensions;

@Mod.EventBusSubscriber(modid = SecretsMagic.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().player == null) return;

        if (ModKeybindings.openSkillTreeKey.isDown()) {
            if (Minecraft.getInstance().screen == null) {
                Minecraft.getInstance().setScreen(new SkillTreeScreen());
            }
        }
    }
    @SubscribeEvent
    public static void onDimensionChange(EntityTravelToDimensionEvent event) {
        if (event.getEntity().equals(Minecraft.getInstance().player)) {
            ResourceKey<Level> targetDimension = event.getDimension();
            if (targetDimension.equals(ModDimensions.SHOREDIM_LEVEL_KEY)) {
                Minecraft.getInstance().options.cloudStatus().set(CloudStatus.OFF);
            } else {
                Minecraft.getInstance().options.cloudStatus().set(CloudStatus.FANCY);
            }
        }
    }
}
