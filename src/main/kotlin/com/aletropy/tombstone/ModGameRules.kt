package com.aletropy.tombstone

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.minecraft.world.GameRules
import org.slf4j.LoggerFactory

object ModGameRules
{
	val CROSS_CONSUME_ON_USE = GameRuleRegistry.register(
		"crossConsumeOnUse", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true)
	)

	fun registerGameRules()
	{
		logger.info("Registering gamerules.")
	}

	private val logger = LoggerFactory.getLogger(JustTombstoneMain.MOD_ID)
}