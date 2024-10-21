package net.secretplaysmc.secrets_magic.spells.effects.customObjects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ExplosiveSmallFireball extends SmallFireball {
    private final float power;
    private int radius = 0;
    public ExplosiveSmallFireball(Level pLevel, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, float power) {
        super(pLevel, pShooter, pOffsetX, pOffsetY, pOffsetZ);
        this.power = power;
    }

    @Override
    public void tick() {
        super.tick();


        if (!this.level().isClientSide && this.tickCount > 60) {
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), this.power, Level.ExplosionInteraction.BLOCK);

            if (this.getOwner() instanceof ServerPlayer serverPlayer) {
                handleAoEFireballSplit(serverPlayer);
            }
            this.discard();
        }
    }

    private void handleAoEFireballSplit(ServerPlayer player) {
        if (this.radius > 0) {
            spawnFireballSplits(this, this.level(), player, radius, this.power/2.0F);
        }
    }

    public void getRadiusFromModifiers(int radius) {
        this.radius = radius;
    }

    private void spawnFireballSplits(ExplosiveSmallFireball originalFireball, Level world, ServerPlayer player, int radius, float power) {
        if (radius <= 0 || power <= 0.1F) {
            return;
        }

        for (int i = 0; i < 4; i++) {
            ExplosiveSmallFireball newFireball = new ExplosiveSmallFireball(world, player, 0, 0 - world.getRandom().nextFloat(), 0, power);
            newFireball.setPos(originalFireball.getX(), originalFireball.getY(), originalFireball.getZ());

            newFireball.shootFromRotation(player, player.getXRot() + (world.random.nextFloat() - 0.5F) * 20,
                    player.getYRot() + (world.random.nextFloat() - 0.5F) * 20,
                    0.0F, power, 1.0F);

            world.addFreshEntity(newFireball);

            if (radius > 1) {
                spawnFireballSplits(newFireball, world, player, radius - 1, power);
            }
        }
    }


}
