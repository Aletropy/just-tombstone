package com.aletropy.tombstone.item

import net.minecraft.item.ItemStack

class CreativeCrossItem(settings: Settings) : GoldenCrossItem(settings)
{
	override fun getMaxUseTime(stack: ItemStack): Int
	{
		return 1
	}

	override fun hasGlint(stack: ItemStack?): Boolean
	{
		return true
	}
}