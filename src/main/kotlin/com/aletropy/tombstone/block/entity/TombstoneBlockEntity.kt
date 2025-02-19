package com.aletropy.tombstone.block.entity

import com.google.common.collect.ImmutableList
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.TypeFilter
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.UUID

class TombstoneBlockEntity(pos : BlockPos, state : BlockState) : BlockEntity(
	ModBlockEntities.TOMBSTONE, pos, state
)
{
	companion object
	{
		fun tick(world: World, pos: BlockPos, state: BlockState, blockEntity: TombstoneBlockEntity)
		{
			val radius = 12.0

			world.getEntitiesByType(
				TypeFilter.instanceOf(MobEntity::class.java), Box.of(
					Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()), radius, radius, radius
				)
			) { true }.forEach {
				it.velocityDirty = true

				val currentPos = pos.toCenterPos()
				val entityPos = it.pos

				val knockbackDir = entityPos.subtract(currentPos).normalize()

				it.velocity = knockbackDir.multiply(1.2)

				world.playSound(
					entityPos.x, entityPos.y, entityPos.z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f, 1.0f, true
				)
			}
		}
	}

	var owner = ""
	private var main = DefaultedList.ofSize(36, ItemStack.EMPTY)
	private var armor = DefaultedList.ofSize(4, ItemStack.EMPTY)
	private var offHand = DefaultedList.ofSize(1, ItemStack.EMPTY)

	private var experienceProgress = 0.0f
	private var experienceLevel = 0

	fun initialize(player : PlayerEntity)
	{
		owner = player.gameProfile.name

		main = DefaultedList.ofSize(player.inventory.main.size, ItemStack.EMPTY)
		armor = DefaultedList.ofSize(player.inventory.armor.size, ItemStack.EMPTY)
		offHand = DefaultedList.ofSize(player.inventory.offHand.size, ItemStack.EMPTY)

		for (i in player.inventory.main.indices) {
			main[i] = player.inventory.main[i].copy()
		}
		for (i in player.inventory.armor.indices) {
			armor[i] = player.inventory.armor[i].copy()
		}
		for (i in player.inventory.offHand.indices) {
			offHand[i] = player.inventory.offHand[i].copy()
		}

		experienceProgress = player.experienceProgress
		experienceLevel = player.experienceLevel

		player.inventory.clear()
		player.experienceLevel = 0
		player.experienceProgress = 0.0f

		markDirty()
	}

	fun tryRetrieve(player : PlayerEntity) : Boolean
	{
		if(owner != player.gameProfile.name) return false

		val currentMainItems = DefaultedList.ofSize(36, ItemStack.EMPTY)
		val currentArmorItems = DefaultedList.ofSize(4, ItemStack.EMPTY)
		val currentOffhandItem = DefaultedList.ofSize(1, ItemStack.EMPTY)

		for (i in player.inventory.main.indices) {
			currentMainItems[i] = player.inventory.main[i].copy()
		}
		for (i in player.inventory.armor.indices) {
			currentArmorItems[i] = player.inventory.armor[i].copy()
		}
		for (i in player.inventory.offHand.indices) {
			currentOffhandItem[i] = player.inventory.offHand[i].copy()
		}

		val currentItems = ImmutableList.of(currentMainItems, currentArmorItems, currentOffhandItem)

		for (i in main.indices) {
			player.inventory.setStack(i, main[i])
		}
		for (i in armor.indices) {
			player.inventory.armor[i] = armor[i].copy()
		}
		for (i in offHand.indices) {
			player.inventory.offHand[i] = offHand[i].copy()
		}

		for(list in currentItems)
			for(stack in list)
			{
				if(!player.giveItemStack(stack))
					world?.spawnEntity(ItemEntity(world, player.pos.x, player.pos.y, player.pos.z, stack))
			}

		player.experienceLevel = experienceLevel
		player.experienceProgress = experienceProgress

		world?.playSound(null, player.blockPos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f)

		return true
	}

	override fun toInitialChunkDataNbt(): NbtCompound
	{
		val nbt = NbtCompound()
		writeNbt(nbt)
		return nbt
	}

	override fun writeNbt(nbt: NbtCompound)
	{
		val mainInvNbt = NbtCompound()
		val armorInvNbt = NbtCompound()
		val offHandInvNbt = NbtCompound()

		Inventories.writeNbt(mainInvNbt, main)
		Inventories.writeNbt(armorInvNbt, armor)
		Inventories.writeNbt(offHandInvNbt, offHand)

		val inventoriesList = NbtList()
		inventoriesList.add(0, mainInvNbt)
		inventoriesList.add(1, armorInvNbt)
		inventoriesList.add(2, offHandInvNbt)

		nbt.put("inventory", inventoriesList)
		nbt.putFloat("experienceProgress", experienceProgress)
		nbt.putInt("experienceLevel", experienceLevel)

		nbt.putString("owner", owner)

		markDirty()
	}

	override fun readNbt(nbt: NbtCompound)
	{
		val inventoriesList = nbt.get("inventory") as NbtList

		Inventories.readNbt(inventoriesList.getCompound(0), main)
		Inventories.readNbt(inventoriesList.getCompound(1), armor)
		Inventories.readNbt(inventoriesList.getCompound(2), offHand)

		experienceProgress = nbt.getFloat("experienceProgress")
		experienceLevel = nbt.getInt("experienceLevel")

		owner = nbt.getString("owner")

		markDirty()
	}
}