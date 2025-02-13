package com.aletropy.tombstone

import com.aletropy.tombstone.block.entity.ModBlockEntities
import com.aletropy.tombstone.block.entity.TombstoneBlockEntity
import com.aletropy.tombstone.block.entity.TombstoneBlockEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories

object JustTombstoneClient : ClientModInitializer {
	override fun onInitializeClient()
	{
		BlockEntityRendererFactories.register(
			ModBlockEntities.TOMBSTONE
		) { TombstoneBlockEntityRenderer() }
	}
}