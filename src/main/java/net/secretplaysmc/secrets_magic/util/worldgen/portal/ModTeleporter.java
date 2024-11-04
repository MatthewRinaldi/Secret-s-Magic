package net.secretplaysmc.secrets_magic.util.worldgen.portal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ModTeleporter implements ITeleporter {
    public final BlockPos destinationPos;
    public final boolean insideDimension;

    public ModTeleporter(BlockPos destinationPos, boolean insideDimension) {
        this.destinationPos = destinationPos;
        this.insideDimension = insideDimension;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        if (entity instanceof ServerPlayer player) {
            // Place the player at the specific coordinates (e.g., 0, 360, 0) in the destination world
            player.teleportTo(destWorld, destinationPos.getX() + 0.5, destinationPos.getY(), destinationPos.getZ() + 0.5, yaw, player.getXRot());
            return player;
        }
        return repositionEntity.apply(false); // For non-player entities, use default repositioning
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        // Define a fixed location, zero velocity, and current yaw/rotation
        return new PortalInfo(
                new Vec3(destinationPos.getX() + 0.5, destinationPos.getY(), destinationPos.getZ() + 0.5),
                Vec3.ZERO,
                entity.getYRot(),
                entity.getXRot()
        );

    }
}
