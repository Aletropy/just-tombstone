package com.aletropy.tombstone.item

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.UseAction
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class GoldenCrossItem(settings: Settings) : Item(settings)
{
	override fun finishUsing(stack: ItemStack, world: World, user: LivingEntity): ItemStack
	{
		if (world.isClient) return super.finishUsing(stack, world, user)
		if (user !is PlayerEntity) return super.finishUsing(stack, world, user)

		val baseLastDeathPos = user.lastDeathPos.getOrNull()?.pos ?: return super.finishUsing(stack, world, user)

		val lastDeathPos = baseLastDeathPos.toCenterPos().add(0.0, 0.5, 0.0)

		user.world.getWorldChunk(baseLastDeathPos).setLoadedToWorld(true)
		user.teleport(lastDeathPos.x, lastDeathPos.y, lastDeathPos.z, true)

		return ItemStack.EMPTY
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