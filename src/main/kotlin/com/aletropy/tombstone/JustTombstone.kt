package com.aletropy.tombstone

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.block.entity.ModBlockEntities
import com.aletropy.tombstone.event.player.BeforePlayerDieCallback
import com.aletropy.tombstone.item.ModItems
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object JustTombstone : ModInitializer
{
	const val MOD_ID = "just-tombstone"
    private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize()
	{
		ModItems.registerItems()

		ModBlocks.registerBlocks()
		ModBlockEntities.registerBlockEntities()

		BeforePlayerDieCallback.EVENT.register(TombstoneEvents::playerDie)
	}
}