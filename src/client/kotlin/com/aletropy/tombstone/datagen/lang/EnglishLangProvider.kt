package com.aletropy.tombstone.datagen.lang

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class EnglishLangProvider(output : FabricDataOutput) : FabricLanguageProvider(output)
{
	override fun generateTranslations(translationBuilder: TranslationBuilder)
	{
		translationBuilder.add(ModBlocks.TOMBSTONE, "Tombstone")
		translationBuilder.add(ModItems.GOLDEN_CROSS, "Golden Cross")
	}
}