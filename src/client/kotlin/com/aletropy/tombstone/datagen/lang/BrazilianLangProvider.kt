package com.aletropy.tombstone.datagen.lang

import com.aletropy.tombstone.block.ModBlocks
import com.aletropy.tombstone.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider

class BrazilianLangProvider(output : FabricDataOutput) : FabricLanguageProvider(output, "pt_br")
{
	override fun generateTranslations(translationBuilder: TranslationBuilder)
	{
		translationBuilder.add(ModBlocks.TOMBSTONE, "Tumba")
		translationBuilder.add(ModItems.GOLDEN_CROSS, "Cruz Dourada")
	translationBuilder.add(ModItems.CREATIVE_CROSS, "Cruz Instantânea")
	}
}