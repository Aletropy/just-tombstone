package com.aletropy.tombstone.mixin;

import com.aletropy.tombstone.event.player.BeforePlayerDieCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerEntityMixin
{
	@Inject(at = @At("HEAD"), method = "onDeath")
    private void init(DamageSource damageSource, CallbackInfo ci)
    {
        BeforePlayerDieCallback.Companion.getEVENT().invoker().interact((PlayerEntity)(Object) this);
    }
}