package net.secretplaysmc.secrets_magic.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.secretplaysmc.secrets_magic.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        
        if (!pLevel.isClientSide()) {
            scanForSapphireOre(pLevel, pPlayer);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    private void scanForSapphireOre(Level pLevel, Player pPlayer) {
        BlockPos playerPos = pPlayer.blockPosition();
        boolean foundSapphire = false;

        for (int x = -16; x <= 16; x++) {
            for (int z = -16; z <= 16; z++) {
                for (int y = playerPos.getY(); y >= -64; y--) {
                    BlockPos checkPos = playerPos.offset(x,y,z);
                    BlockState blockState = pLevel.getBlockState(checkPos);

                    if (blockState.is(ModBlocks.SAPPHIRE_ORE.get())) {
                        pPlayer.sendSystemMessage(Component.literal("Found Sapphire Ore at (" +
                                checkPos.getX() + ", " + checkPos.getY() + ", " + checkPos.getZ() + ")"));
                        foundSapphire = true;
                    }
                }
            }
        }
        if (!foundSapphire) {
            pPlayer.sendSystemMessage(Component.literal("No Sapphire Ore Found!"));
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.secrets_magic.metal_detector.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
