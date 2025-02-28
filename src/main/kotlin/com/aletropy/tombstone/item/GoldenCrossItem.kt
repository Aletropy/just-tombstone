package com.aletropy.tombstone.item

import com.aletropy.tombstone.data.DeathStateSL
import com.aletropy.tombstone.ModGameRules
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import java.util.*

open class GoldenCrossItem(settings: Settings) : Item(settings)
{
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack
	{
		if (world.isClient) return stack
		if (user !is PlayerEntity) return stack

		val deathState = DeathStateSL.getPlayerState(user)
		val lastDeathPos = deathState.lastTombPosition ?: return stack
		val baseLastDeathPos = lastDeathPos.pos

		val targetPos = baseLastDeathPos.toCenterPos().add(0.0, 0.5, 0.0)

		val dimension = world.server!!.getWorld(lastDeathPos.dimension) as ServerWorld

		dimension.chunkManager.setChunkForced(dimension.getChunk(baseLastDeathPos).pos, true)
		user.teleport(dimension, targetPos.x, targetPos.y, targetPos.z, EnumSet.noneOf(PositionFlag::class.java), user.yaw, user.pitch)

		return if(world.gameRules.get(ModGameRules.CROSS_CONSUME_ON_USE).get())
			ItemStack.EMPTY else stack
	}

	override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack>
	{
		val stack = user.getStackInHand(hand)
		user.setCurrentHand(hand)
		return TypedActionResult.consume(stack)
	}

	override fun getUseAction(stack: ItemStack?): UseAction
	{
		return UseAction.BRUSH
	}

	override fun getMaxUseTime(stack: ItemStack): Int
	{
		return 100
	}
}