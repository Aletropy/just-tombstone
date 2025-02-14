package com.aletropy.tombstone.event.player

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult

fun interface BeforePlayerDieCallback
{
	fun interact(damageSource : DamageSource, player: PlayerEntity) : Unit

	companion object
	{
		val EVENT = EventFactory.createArrayBacked(
			BeforePlayerDieCallback::class.java
		) { listeners ->
			BeforePlayerDieCallback { damageSource, player ->
				for (listener in listeners)
					listener.interact(damageSource, player)
				ActionResult.PASS
			}
		}
	}
}
