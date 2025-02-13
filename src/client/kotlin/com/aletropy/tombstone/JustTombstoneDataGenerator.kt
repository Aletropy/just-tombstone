package com.aletropy.tombstone

import com.aletropy.tombstone.datagen.ModModelProvider
import com.aletropy.tombstone.datagen.lang.BrazilianLangProvider
import com.aletropy.tombstone.datagen.lang.EnglishLangProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput

object JustTombstoneDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator)
	{
		val pack = fabricDataGenerator.createPack()

		pack.addProvider { output -> ModModelProvider(output as FabricDataOutput) }

		// Languages Data Generation
		pack.addProvider { output -> EnglishLangProvider(output as FabricDataOutput) }
		pack.addProvider { output -> BrazilianLangProvider(output as FabricDataOutput) }
	}
}