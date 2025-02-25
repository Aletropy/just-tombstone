package com.aletropy.tombstone

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.block.TombstoneBlock
import com.aletropy.tombstone.block.entity.ModBlockEntities
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.tick.TickPriority
import kotlin.jvm.optionals.getOrNull
import kotlin.math.abs

object TombstoneEvents
{
	fun playerDie(damageSource: DamageSource, player: PlayerEntity)
	{
		val world = player.world

		if (world.isClient || player.isSpectator) return

		val server = (world as? ServerWorld)?.server ?: return

		val pos = if(damageSource == player.damageSources.outOfWorld())
			getVoidAndCreate(player)
		else getSafeOrCreate(player)

		if (world.setBlockState(pos, ModBlocks.TOMBSTONE.defaultState, Block.NOTIFY_ALL))
		{
			val blockEntity = world.getBlockEntity(pos, ModBlockEntities.TOMBSTONE)
				.getOrNull() ?: return

			blockEntity.initialize(player)
		}
	}

	private fun getVoidAndCreate(player: PlayerEntity): BlockPos
	{
		val pos = BlockPos(player.blockX, player.world.bottomY+5, player.blockZ)
		generatePlatform(player, pos)
		return pos
	}

	private fun getSafeOrCreate(player : PlayerEntity) : BlockPos
	{
		var safePos = getSafePosition(player)
		if(safePos != null) return safePos

		safePos = player.blockPos
		generatePlatform(player, safePos)

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
					player.world.setBlockState(currentPos, block, Block.NOTIFY_ALL)
				}
			}
		}
	}

	private fun getSafePosition(player : PlayerEntity) : BlockPos?
	{
		val world = player.world
		val startPos = player.blockPos

		if(isValidLocation(startPos, world)) return startPos

		val radius = 16

		for(r in 1..radius)
		{
			for(dx in -r..r)
			{
				for(dz in -r..r)
				{
					if(abs(dx) != r && abs(dz) != r) continue

					val basePos = BlockPos(startPos.x + dx,startPos.y + dz,startPos.z + dz)

					var maxHeight = startPos.y
					val minHeight = world.bottomY
					for(dy in maxHeight downTo minHeight)
					{
						val candidatePos = basePos.add(0, dy, 0)
						if(isValidLocation(candidatePos, world)) {
							return candidatePos
						}
					}
					maxHeight += radius
					for(dy in maxHeight downTo minHeight)
					{
						val candidatePos = basePos.add(0, dy, 0)
						if(isValidLocation(candidatePos, world)) {
							return candidatePos
						}
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