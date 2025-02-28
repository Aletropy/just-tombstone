package com.aletropy.tombstone.block

import com.aletropy.tombstone.block.entity.ModBlockEntities
import com.aletropy.tombstone.block.entity.TombstoneBlockEntity
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.util.ParticleUtil
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class TombstoneBlock(settings: Settings) : BlockWithEntity(settings)
{
	init {
		defaultState = defaultState.with(Properties.HORIZONTAL_FACING, Direction.NORTH)
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

	override fun appendProperties(builder: StateManager.Builder<Block, BlockState>)
	{
		builder.add(Properties.HORIZONTAL_FACING)
	}

	override fun getOutlineShape(
		state: BlockState,
		world: BlockView,
		pos: BlockPos,
		context: ShapeContext
	): VoxelShape
	{
		val dir = state.get(Properties.HORIZONTAL_FACING)
		return when(dir)
		{
			Direction.NORTH -> Block.createCuboidShape(3.75, 0.0, 0.0, 12.25, 2.0, 14.9)
			Direction.EAST  -> Block.createCuboidShape(1.0, 0.0, 3.75, 16.0, 2.0, 12.25)
			Direction.SOUTH -> Block.createCuboidShape(3.75, 0.0, 1.1, 12.25, 2.0, 16.0)
			Direction.WEST  -> Block.createCuboidShape(0.0, 0.0, 3.75, 14.9, 2.0, 12.25)
			else -> VoxelShapes.fullCube()
		}
	}

	override fun rotate(state: BlockState, rotation: BlockRotation): BlockState
	{
		return state.with(
			HorizontalFacingBlock.FACING,
			rotation.rotate(state.get(HorizontalFacingBlock.FACING))
		) as BlockState
	}

	override fun mirror(state: BlockState, mirror: BlockMirror): BlockState
	{
		return state.rotate(mirror.getRotation(state.get(HorizontalFacingBlock.FACING)))
	}

	override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random)
	{
		super.randomDisplayTick(state, world, pos, random)
		if(random.nextInt(10) <= 3)
		{
			val blockPos = pos.up()
			ParticleUtil.spawnParticle(world, blockPos, random, ParticleTypes.CHERRY_LEAVES)
		}
	}

	override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity
	{
		return TombstoneBlockEntity(pos, state)
	}

	override fun getRenderType(state: BlockState?): BlockRenderType
	{
		return BlockRenderType.MODEL
	}

	override fun getPlacementState(ctx: ItemPlacementContext): BlockState?
	{
		return super.getPlacementState(ctx)?.with(Properties.HORIZONTAL_FACING, ctx.horizontalPlayerFacing)
	}
}