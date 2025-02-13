package com.aletropy.tombstone

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.block.entity.ModBlockEntities
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

object TombstoneEvents
{
	fun playerDie(player : PlayerEntity)
	{
		val world = player.world

		if(player.inventory.isEmpty() && player.experienceLevel <= 0) return

		val pos = findSafeBlock(world, player.blockPos)

		if(world.setBlockState(pos ?: player.blockPos,
			ModBlocks.TOMBSTONE.defaultState
		))
		{
			val entity = world.getBlockEntity(player.blockPos, ModBlockEntities.TOMBSTONE).getOrNull() ?: return

			entity.initialize(player)
		}
	}

	private fun findSafeBlock(world: World, pos: BlockPos): BlockPos? {
		fun isSafe(blockPos: BlockPos): Boolean {
			val blockBelow = world.getBlockState(blockPos)
			val blockAbove = world.getBlockState(blockPos.up())
			return blockBelow.isSolidBlock(world, blockPos) && blockAbove.isAir
		}

		if (isSafe(pos)) return pos.up()

		val radius = 5
		for (yOffset in 0..10) {
			for (xOffset in -radius..radius) {
				for (zOffset in -radius..radius) {
					val checkPos = pos.add(xOffset, yOffset, zOffset)
					if (isSafe(checkPos)) return checkPos.up()
				}
			}
		}

		return null
	}


}