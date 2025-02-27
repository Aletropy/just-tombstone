package com.aletropy.tombstone

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.block.entity.ModBlockEntities
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos
import net.minecraft.world.GameRules
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull
import kotlin.math.abs

object TombstoneEvents
{
	private const val RADIUS = 16

	fun playerDie(damageSource: DamageSource, player: PlayerEntity)
	{
		val world = player.world

		if(world.isClient || player.isSpectator) return
		if(world.gameRules.getBoolean(GameRules.KEEP_INVENTORY)) return

		val server = (world as? ServerWorld)?.server ?: return

		val pos = if(damageSource == player.damageSources.outOfWorld())
			getVoidAndCreate(player)
		else getSafeOrCreate(player)

		val deathState = DeathStateSL.getPlayerState(player)

		deathState.lastTombPosition = GlobalPos.create(
			player.world.registryKey, pos
		)

		if (world.setBlockState(pos, ModBlocks.TOMBSTONE.defaultState, Block.NOTIFY_ALL))
		{
			val blockEntity = world.getBlockEntity(pos, ModBlockEntities.TOMBSTONE)
				.getOrNull() ?: return

			blockEntity.initialize(player)
		}
	}

	private fun getVoidAndCreate(player: PlayerEntity): BlockPos
	{
		val pos = BlockPos(player.blockX, player.world.bottomY+10, player.blockZ)
		generatePlatform(player, pos)
		return pos
	}

	private fun getSafeOrCreate(player : PlayerEntity) : BlockPos
	{
		var safePos = getSafePosition(player)

		if(safePos != null) return safePos

		val pos = player.blockPos

		for(y in pos.y..player.world.topY)
		{
			if (!player.world.getBlockState(
					BlockPos(
						pos.x, y, pos.z
					)
				).isAir
			) continue

			safePos = pos.withY(y)
			break
		}

		generatePlatform(player, safePos!!)

		return safePos
	}

	private fun generatePlatform(player : PlayerEntity, pos : BlockPos)
	{
		for(dx in -1..1)
		{
			for(dz in -1..1)
			{
				for(dy in -1..2)
				{
					val currentPos = pos.add(dx, dy, dz)
					val block = when(dy)
					{
						-1 -> Blocks.STONE.defaultState
						else -> Blocks.AIR.defaultState
					}
					if(dy != -1 || !player.world.getBlockState(currentPos).isSolid)
						player.world.setBlockState(currentPos, block, Block.NOTIFY_ALL)
				}
			}
		}
	}

	private fun getSafePositionFromLiquid(world : World, startPos : BlockPos) : BlockPos?
	{
		for(r in 1..RADIUS)
		{
			for(dx in -r..r)
			{
				for(dz in -r..r)
				{
					if(abs(dx) != r && abs(dz) != r) continue

					val basePos = BlockPos(
						startPos.x + dx,
						startPos.y,
						startPos.z + dz,
					)

					var safeY : Int? = null

					for(dy in startPos.y..world.topY)
					{
						if(world.getBlockState(BlockPos(basePos.withY(dy))).isAir)
						{
							safeY = dy
							break
						}
					}

					if(safeY == null) continue

					val candidatePos = basePos.withY(safeY)
					if(isValidLocation(candidatePos, world)) return candidatePos
				}
			}
		}

		return null
	}

	private fun getSafePosition(player : PlayerEntity) : BlockPos?
	{
		val world = player.world
		val startPos = player.blockPos

		if(isValidLocation(startPos, world)) return startPos
		val currentState = world.getBlockState(startPos)
		if(currentState.isLiquid) return getSafePositionFromLiquid(world, startPos)

		// Spiral Search
		for(r in 1..RADIUS)
		{
			for(dx in -r..r)
			{
				for(dz in -r..r)
				{
					// Check if is in the border of spiral
					if(abs(dx) != r && abs(dz) != r) continue

					val basePos = BlockPos(startPos.x + dx,startPos.y + dz,startPos.z + dz)

					val maxHeight = world.topY
					val minHeight = world.bottomY

					// Check for blocks above first
					for(y in startPos.y..maxHeight) {
						val pos = basePos.withY(y)
						if(isValidLocation(pos, world)) return pos
					}

					// Check for blocks below
					for(y in startPos.y downTo minHeight) {
						val pos = basePos.withY(y)
						if(isValidLocation(pos, world)) return pos
					}
				}
			}
		}

		return null
	}

	private fun isValidLocation(pos : BlockPos, world : World) : Boolean
	{
		val block = world.getBlockState(pos)
		return block.isAir
				&& world.getBlockState(pos.up()).isAir
				&& world.getBlockState(pos.down()).isSolid
	}
}