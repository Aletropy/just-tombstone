package com.aletropy.tombstone.block.entity

import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.math.RotationAxis
import kotlin.math.asin
import kotlin.math.atan2

class TombstoneBlockEntityRenderer : BlockEntityRenderer<TombstoneBlockEntity> {

	override fun render(
		entity: TombstoneBlockEntity,
		tickDelta: Float,
		matrices: MatrixStack,
		vertexConsumers: VertexConsumerProvider,
		light: Int,
		overlay: Int
	) {
		matrices.push()

		// Posiciona o texto no centro do bloco e acima dele
		matrices.translate(0.5, 0.16, 0.18) // Ajuste Y para altura desejada

		// Rotaciona o texto para seguir a câmera do jogador (billboarding)
		val cameraPos = MinecraftClient.getInstance().gameRenderer.camera.pos
		val blockPos = entity.pos.toCenterPos()
		val direction = cameraPos.subtract(blockPos).normalize()

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f))

		val textRenderer = MinecraftClient.getInstance().textRenderer
		val text = Text.of(entity.owner)
		val scale = 0.006f // Ajuste o tamanho do texto

		// Centraliza o texto
		val textWidth = textRenderer.getWidth(text) * scale / 2
		matrices.translate(-textWidth.toDouble(), 0.0, 0.0)

		// Renderiza o texto em duas passagens (frente e verso)
		matrices.scale(scale, -scale, scale)
		renderText(text, textRenderer, matrices, vertexConsumers, light, true) // Frente
		matrices.scale(1f, 1f, -1f) // Inverte Z para renderizar atrás
		renderText(text, textRenderer, matrices, vertexConsumers, light, false) // Verso

		matrices.pop()
	}

	private fun renderText(
		text: Text,
		textRenderer: TextRenderer,
		matrices: MatrixStack,
		vertexConsumers: VertexConsumerProvider,
		light: Int,
		front: Boolean
	) {
		val matrix = matrices.peek().positionMatrix

		textRenderer.draw(
			text,
			0f, 0f, 0xFF333333.toInt(),
			false,
			matrix,
			vertexConsumers,
			TextRenderer.TextLayerType.NORMAL,
			if (front) 0 else 0x00000000,
			light
		)
	}
}