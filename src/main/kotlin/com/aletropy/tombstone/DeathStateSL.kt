package com.aletropy.tombstone

import net.minecraft.entity.LivingEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.GlobalPos
import net.minecraft.world.PersistentState
import net.minecraft.world.World
import java.lang.reflect.Type
import java.util.*
import java.util.function.Consumer


class DeathState
{
	var lastTombPosition: GlobalPos? = null
}

class DeathStateSL : PersistentState()
{
	var players = hashMapOf<UUID, DeathState>()

	override fun writeNbt(nbt: NbtCompound): NbtCompound
	{
		val playersNbt = NbtCompound()

		for ((uuid, playerData) in players)
		{
			val tombGlobalPos = playerData.lastTombPosition ?: continue
			val pos = tombGlobalPos.pos
			val playerNbt = NbtCompound()

			playerNbt.putInt("x", pos.x)
			playerNbt.putInt("y", pos.y)
			playerNbt.putInt("z", pos.z)

			playerNbt.putString("dimension", playerData.lastTombPosition!!.dimension.toString())

			playersNbt.put(uuid.toString(), playerNbt)
		}

		nbt.put("players", playersNbt)

		return nbt
	}

	companion object
	{
		private fun createNew(): DeathStateSL
		{
			val state = DeathStateSL()
			state.players = hashMapOf()
			return state
		}

		private fun createFromNbt(tag: NbtCompound): DeathStateSL
		{
			val state = DeathStateSL()

			val playersNbt = tag.getCompound("players")
			playersNbt.keys.forEach(Consumer<String> { key: String ->
				val playerData = DeathState()

				val playerNbt = playersNbt.getCompound(key)
				val x = playerNbt.getInt("x")
				val y = playerNbt.getInt("y")
				val z = playerNbt.getInt("z")
				val dimension = RegistryKey.of(
					RegistryKeys.WORLD,
					Identifier.tryParse(playerNbt.getString("dimension"))
				)

				playerData.lastTombPosition = GlobalPos.create(
					dimension, BlockPos(x, y, z)
				)

				val uuid = UUID.fromString(key)
				state.players[uuid] = playerData
			})

			return state
		}


		private fun getServerState(server: MinecraftServer): DeathStateSL
		{
			val persistentStateManager = server.getWorld(World.OVERWORLD)!!.persistentStateManager

			val state = persistentStateManager.getOrCreate(
				{ createFromNbt(it) }, { createNew() }, JustTombstone.MOD_ID)

			state.markDirty()

			return state
		}

		fun getPlayerState(player: LivingEntity): DeathState
		{
			val serverState = getServerState(player.world.server!!)

			val playerState: DeathState = serverState.players.computeIfAbsent(player.uuid) { DeathState() }

			return playerState
		}
	}
}