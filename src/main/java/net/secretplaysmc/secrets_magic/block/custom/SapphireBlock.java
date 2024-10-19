package net.secretplaysmc.secrets_magic.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.secretplaysmc.secrets_magic.util.gui.CustomSpellContainer;

public class SapphireBlock extends Block {
    public SapphireBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, new SimpleMenuProvider(
                    (id, inventory, playerEntity) -> new CustomSpellContainer(id, inventory),
                    Component.literal("Spell Customization")
            ), pPos);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}
