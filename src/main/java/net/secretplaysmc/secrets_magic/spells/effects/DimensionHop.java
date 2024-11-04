package net.secretplaysmc.secrets_magic.spells.effects;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.secretplaysmc.secrets_magic.spells.modifiers.SpellModifier;
import net.secretplaysmc.secrets_magic.util.worldgen.dimension.ModDimensions;
import net.secretplaysmc.secrets_magic.util.worldgen.portal.ModTeleporter;

import java.util.List;

public class DimensionHop implements SpellEffect{
    private BlockPos playerPos;

    @Override
    public void apply(Level world, ServerPlayer player, ItemStack wandItem, List<SpellModifier> modifiers) {
        if (player.level() instanceof ServerLevel serverLevel) {
            MinecraftServer minecraftServer = serverLevel.getServer();
            ResourceKey<Level> resourceKey = player.level().dimension() == ModDimensions.SHOREDIM_LEVEL_KEY ?
                    Level.OVERWORLD : ModDimensions.SHOREDIM_LEVEL_KEY;

            ServerLevel portalDimension = minecraftServer.getLevel(resourceKey);
            if (portalDimension != null && !player.isPassenger()) {
                if (resourceKey == ModDimensions.SHOREDIM_LEVEL_KEY) {
                    this.playerPos = player.getOnPos();
                    BlockPos targetPos = new BlockPos(0, 165, 0);
                    player.changeDimension(portalDimension, new ModTeleporter(targetPos, true));
                } else {
                    if (this.playerPos == null) {
                        this.playerPos = new BlockPos(0, 64, 0);
                    }
                    player.changeDimension(portalDimension, new ModTeleporter(this.playerPos, false));
                }
            }
        }
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("effectType", "dimensionHop");
        if (this.playerPos != null) {
            tag.putInt("pX", this.playerPos.getX());
            tag.putInt("pY", this.playerPos.getY());
            tag.putInt("pZ", this.playerPos.getZ());
        }
        return tag;
    }

    public static DimensionHop fromNBT(CompoundTag tag) {

        DimensionHop dimensionHop = new DimensionHop();

        if (tag.contains("pX") && tag.contains("pY") && tag.contains("pZ")) {
            dimensionHop.playerPos = new BlockPos(tag.getInt("pX"), tag.getInt("pY"), tag.getInt("pZ"));
        }

        return dimensionHop;
    }
}
