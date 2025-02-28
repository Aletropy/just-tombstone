package com.aletropy.tombstone.datagen

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.Models

class ModModelProvider(output : FabricDataOutput) : FabricModelProvider(output)
{
	override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator)
	{
		blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.TOMBSTONE)
	}

	override fun generateItemModels(itemModelGenerator: ItemModelGenerator)
	{
		itemModelGenerator.register(ModItems.GOLDEN_CROSS, Models.GENERATED)
		itemModelGenerator.register(ModItems.CREATIVE_CROSS, ModItems.GOLDEN_CROSS, Models.GENERATED)
	}

}