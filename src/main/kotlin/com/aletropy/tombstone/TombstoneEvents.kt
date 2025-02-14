package com.aletropy.tombstone

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.block.entity.ModBlockEntities
import net.minecraft.block.Block
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.tick.TickPriority
import kotlin.jvm.optionals.getOrNull

object TombstoneEvents
{
	fun playerDie(damageSource: DamageSource, player: PlayerEntity)
	{
		val world = player.world

		if (world.isClient || player.isSpectator) return

		val server = (world as? ServerWorld)?.server ?: return
		val pos = player.blockPos

		if (world.setBlockState(pos, ModBlocks.TOMBSTONE.defaultState, Block.NOTIFY_ALL))
		{
			val blockEntity = world.getBlockEntity(pos, ModBlockEntities.TOMBSTONE)
				.getOrNull() ?: return

			blockEntity.initialize(player)
		}
	}
}