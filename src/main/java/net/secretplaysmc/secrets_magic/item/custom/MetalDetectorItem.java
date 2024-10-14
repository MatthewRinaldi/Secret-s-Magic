package net.secretplaysmc.secrets_magic.item.custom;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.secretplaysmc.secrets_magic.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Properties pProperties) {
        super(pProperties);
    }
    private static BlockState valuableBlock;
    private static boolean blockSelected = false;

    @Override
    public boolean isFoil(ItemStack pStack) {
        return blockSelected;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isCrouching()) {
            blockSelected = false;
            valuableBlock = null;
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            BlockPos positionClicked = pContext.getClickedPos();
            Player player = pContext.getPlayer();
            boolean foundBlock = false;

            if (player.isCrouching()) {
                BlockState state = pContext.getLevel().getBlockState(positionClicked);
                setValuableBlock(state);
                player.sendSystemMessage(Component.literal("Detector set to " + I18n.get(state.getBlock().getDescriptionId())));

                blockSelected = true;
            } else {
                for (int i = 0; i <= positionClicked.getY() + 64; i++) {
                    BlockState state = pContext.getLevel().getBlockState(positionClicked.below(i));
                    if (valuableBlock == null) {
                        if(isValuableBlockTags(state)) {
                            outputValuableCoordinates(positionClicked.below(i), player, state.getBlock());
                            foundBlock = true;
                        }
                    } else {
                        if (isValuableBlock(state)) {
                            outputValuableCoordinates(positionClicked.below(i), player, state.getBlock());
                            foundBlock = true;
                        }
                    }
                }

                if(!foundBlock) {
                    player.sendSystemMessage(Component.literal("No ores found!"));
                }
            }
        }

        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.tutorial_mod.metal_detector.tooltip"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void setValuableBlock(BlockState state) {
        valuableBlock = state;
    }


    private void outputValuableCoordinates(BlockPos blockPos, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found " + I18n.get(block.getDescriptionId()) + " at " +
                "(" + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ() + ")"));
    }

    private boolean isValuableBlock(BlockState state) {
        return state.is(valuableBlock.getBlock());
    }

    private boolean isValuableBlockTags(BlockState state) {
        return state.is(ModTags.Blocks.METAL_DETECTOR_VALUABLES);
    }


}
