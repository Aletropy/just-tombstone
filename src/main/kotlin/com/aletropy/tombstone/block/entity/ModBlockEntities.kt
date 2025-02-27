package com.aletropy.tombstone.block.entity

import com.aletropy.tombstone.JustTombstoneMain
import com.aletropy.tombstone.block.ModBlocks
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object ModBlockEntities
{
	val TOMBSTONE = register(
		FabricBlockEntityTypeBuilder.create(
			{ pos, state, -> TombstoneBlockEntity(pos, state) },
			ModBlocks.TOMBSTONE
		).build(),
		"tombstone"
	)

	private fun <T : BlockEntity> register(blockEntityType : BlockEntityType<T>, name : String) : BlockEntityType<T>
	{
		val id = Identifier(JustTombstoneMain.MOD_ID, name)

		return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType)
	}

	fun registerBlockEntities()
	{
		LoggerFactory.getLogger(
			JustTombstoneMain.MOD_ID
		).info("Registering Block Entities")
	}
}