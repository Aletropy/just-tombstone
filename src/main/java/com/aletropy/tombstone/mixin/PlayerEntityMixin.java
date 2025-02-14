package com.aletropy.tombstone.mixin;

import com.aletropy.tombstone.event.player.BeforePlayerDieCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerEntityMixin
{
    @Inject(at = @At("HEAD"), method = "onDeath")
    private void init(DamageSource damageSource, CallbackInfo ci)
    {
        BeforePlayerDieCallback.Companion.getEVENT().invoker().interact(damageSource, (PlayerEntity)(Object) this);
    }
}