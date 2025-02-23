package com.aletropy.tombstone.support.trinkets

import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import kotlin.jvm.optionals.getOrNull

class TrinketsHelper
{
	fun getAndClearTrinkets(player : PlayerEntity) : NbtCompound
	{
		val items = NbtCompound()

		val component = TrinketsApi.getTrinketComponent(player).getOrNull() ?: return items

		component.writeToNbt(items)

		component.forEach { slotReference, _ -> slotReference.inventory.setStack(slotReference.index, ItemStack.EMPTY) }

		return items
	}

	fun setTrinketsFromNbt(nbt: NbtCompound, player : PlayerEntity)
	{
		val component = TrinketsApi.getTrinketComponent(player).getOrNull() ?: return

		component.readFromNbt(nbt)
	}
}