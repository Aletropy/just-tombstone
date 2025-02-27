package com.aletropy.tombstone.block

import com.aletropy.tombstone.JustTombstoneMain
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object ModBlocks
{
	val TOMBSTONE = register(
		TombstoneBlock(AbstractBlock.Settings.create()
			.sounds(BlockSoundGroup.STONE)
			.dropsNothing()
			.strength(-1.0f, 3600000.0f)
			.allowsSpawning(Blocks::never)
			.luminance { 12 }
			.nonOpaque()
			.solid()
		),
		"tombstone",
		true
	)

	private fun register(block : Block, name : String, shouldCreateItem : Boolean) : Block
	{
		val id = Identifier(
			JustTombstoneMain.MOD_ID, name
		)

		if(shouldCreateItem) {
			val item = BlockItem(block, FabricItemSettings())
			Registry.register(Registries.ITEM, id, item)
		}

		return Registry.register(Registries.BLOCK, id, block)
	}

	fun registerBlocks()
	{
		LoggerFactory.getLogger(JustTombstoneMain.MOD_ID).info("Registering Blocks")
	}
}