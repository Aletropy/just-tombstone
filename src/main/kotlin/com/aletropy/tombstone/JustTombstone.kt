package com.aletropy.tombstone

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.block.entity.ModBlockEntities
import com.aletropy.tombstone.event.player.BeforePlayerDieCallback
import net.fabricmc.api.ModInitializer
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

object JustTombstone : ModInitializer
{
	const val MOD_ID = "just-tombstone"
    private val logger = LoggerFactory.getLogger(MOD_ID)

	override fun onInitialize()
	{
		ModBlocks.registerBlocks()
		ModBlockEntities.registerBlockEntities()

		BeforePlayerDieCallback.EVENT.register(TombstoneEvents::playerDie)
	}
}