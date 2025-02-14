package com.aletropy.tombstone.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = Explosion.class)
public class ExplosionMixin
{
    @Shadow @Final private Map<PlayerEntity, Vec3d> affectedPlayers;

    @Shadow @Final private ObjectArrayList<BlockPos> affectedBlocks;

    @Inject(at = @At("TAIL"), method = "collectBlocksAndDamageEntities")
    private void collectBlocksAndDamageEntities(CallbackInfo ci)
    {
        affectedPlayers.forEach((playerEntity, vec3d) -> {
           if(playerEntity.isDead()) {
               affectedBlocks.remove(playerEntity.getBlockPos());
           }
        });
    }
}
