package com.aletropy.tombstone.datagen

import com.aletropy.tombstone.item.ModItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.recipe.RecipeJsonProvider
import net.minecraft.data.server.recipe.RecipeProvider
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import java.util.function.Consumer

class ModRecipeProvider(output: FabricDataOutput?) : FabricRecipeProvider(output)
{
	override fun generate(exporter: Consumer<RecipeJsonProvider>)
	{
		ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.GOLDEN_CROSS)
			.input('#', Items.GOLD_INGOT)
			.pattern("###")
			.pattern(" # ")
			.pattern(" # ")
			.criterion("has_gold", RecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
			.offerTo(exporter)
	}
}