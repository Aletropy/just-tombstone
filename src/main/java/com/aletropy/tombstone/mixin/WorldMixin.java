package com.aletropy.tombstone.mixin;

import com.aletropy.tombstone.block.ModBlocks;
import com.aletropy.tombstone.block.TombstoneBlock;
import com.aletropy.tombstone.block.entity.TombstoneBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin implements HeightLimitView
{
    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @Shadow public abstract @Nullable BlockEntity getBlockEntity(BlockPos pos);

    @Inject(
            at = @At("HEAD"),
            method = "removeBlock",
            cancellable = true
    )
    private void removeBlock(BlockPos pos, boolean move, CallbackInfoReturnable<Boolean> cir)
    {
        BlockState state = getBlockState(pos);
        if(state.getBlock() == ModBlocks.INSTANCE.getTOMBSTONE())
        {
            TombstoneBlockEntity entity = (TombstoneBlockEntity)getBlockEntity(pos);
            if(entity != null && !entity.canBeRemoved())
                cir.setReturnValue(false);
        }
    }

    @Inject(
            at = @At("HEAD"),
            method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
            cancellable = true
    )
    private void setBlockState(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir)
    {
        if(state.getBlock() == ModBlocks.INSTANCE.getTOMBSTONE())
        {
            TombstoneBlockEntity entity = (TombstoneBlockEntity)getBlockEntity(pos);
            if(entity != null && !entity.canBeRemoved())
                cir.setReturnValue(false);
        }
    }
}
