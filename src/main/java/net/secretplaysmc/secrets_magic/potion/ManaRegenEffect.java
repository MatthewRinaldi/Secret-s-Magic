package net.secretplaysmc.secrets_magic.potion;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.secretplaysmc.secrets_magic.mana.ManaCapabilityProvider;

public class ManaRegenEffect extends MobEffect {
    public ManaRegenEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x5A8DEB);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if(!pLivingEntity.level().isClientSide() && pLivingEntity instanceof ServerPlayer player) {
            player.getCapability(ManaCapabilityProvider.MANA_CAPABILITY).ifPresent(mana -> {
                mana.regenerateMana(1);
                mana.syncManaWithClient(player);
            });
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
