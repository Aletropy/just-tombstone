package com.aletropy.tombstone.item

import com.aletropy.tombstone.JustTombstone
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroups
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import org.slf4j.LoggerFactory

object ModItems
{
	val GOLDEN_CROSS = register(
		GoldenCrossItem(FabricItemSettings()
			.maxCount(1)
			.rarity(Rarity.UNCOMMON)
			.fireproof()
		), "golden_cross"
	)

	val CREATIVE_CROSS = register(
		CreativeCrossItem(FabricItemSettings()
			.maxCount(1)
			.rarity(Rarity.EPIC)
			.fireproof()
		), "creative_cross"
	)

	private fun register(item : Item, name : String) : Item
	{
		val id = Identifier(JustTombstone.MOD_ID, name)
		return Registry.register(Registries.ITEM, id, item)
	}

	private fun registerItemGroups()
	{
		logger.info("Registering item groups")
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register {
			it.addAfter(Items.TOTEM_OF_UNDYING, GOLDEN_CROSS)
		}

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register {
			it.add(CREATIVE_CROSS)
		}
	}

	fun registerItems()
	{
		logger.info("Registering items")
		registerItemGroups()
	}

	private val logger = LoggerFactory.getLogger(JustTombstone.MOD_ID)
}