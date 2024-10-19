package com.alexjlockwood.twentyfortyeight.domain

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Container class that wraps a number and a unique ID for use in the grid.
 */
@Serializable(TileSerializer::class)
data class Tile(
    val num: Int,
    val id: Int = tileIdCounter++,
) {
    companion object {
        // We assign each tile a unique ID and use it to efficiently
        // animate tile objects within the compose UI. Yeah, I know
        // it's a bit weird but this is just a sample app lol.
        private var tileIdCounter = 0
    }

    operator fun times(operand: Int): Tile = Tile(num * operand)
}

object TileSerializer : KSerializer<Tile> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Tile") {
        element<Int>("num")
        element<Int>("id")
    }

    override fun serialize(encoder: Encoder, value: Tile) {
        encoder.encodeSerializableValue(Int.serializer(), value.num)
    }

    override fun deserialize(decoder: Decoder): Tile {
        val num = decoder.decodeSerializableValue(Int.serializer())
        return Tile(num)
    }
}
