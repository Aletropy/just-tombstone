package com.aletropy.tombstone.block

import com.aletropy.tombstone.block.entity.ModBlockEntities
import com.aletropy.tombstone.block.entity.TombstoneBlockEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.scoreboard.ScoreboardCriterion
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import kotlin.jvm.optionals.getOrNull

class TombstoneBlock(settings: Settings) : BlockWithEntity(settings)
{
	companion object
	{
		const val PICKUP : Int = 1 shl 10
	}

	@Deprecated(
		"Deprecated in Java",
		ReplaceWith("super.getOutlineShape(state, world, pos, context)", "net.minecraft.block.Block")
	)
	override fun onUse(
		state: BlockState,
		world: World,
		pos: BlockPos,
		player: PlayerEntity,
		hand: Hand,
		hit: BlockHitResult
	): ActionResult
	{
		if(world.isClient) return ActionResult.PASS

		val blockEntity = world.getBlockEntity(pos, ModBlockEntities.TOMBSTONE).getOrNull() ?: return ActionResult.PASS

		if(blockEntity.tryRetrieve(player))
		{
			world.removeBlock(pos, true)
			world.chunkManager.setChunkForced(world.getChunk(pos).pos, false)
		}

		return ActionResult.SUCCESS
	}

	override fun <T : BlockEntity?> getTicker(
		world: World,
		state: BlockState,
		type: BlockEntityType<T>
	): BlockEntityTicker<T>?
	{
		return checkType(type, ModBlockEntities.TOMBSTONE, TombstoneBlockEntity::tick)
	}

	override fun getOutlineShape(
		state: BlockState,
		world: BlockView,
		pos: BlockPos,
		context: ShapeContext
	): VoxelShape
	{
		return Block.createCuboidShape(
			3.75, 0.0, 0.0, 12.25, 2.0, 14.9
		)
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity
	{
		return TombstoneBlockEntity(pos, state)
	}

	override fun getRenderType(state: BlockState?): BlockRenderType
	{
		return BlockRenderType.MODEL
	}
}