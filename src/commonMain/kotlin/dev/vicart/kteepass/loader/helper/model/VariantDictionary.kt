package dev.vicart.kteepass.loader.helper.model

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class VariantDictionary(
    val version: UShort,
    val items: Array<VariantDictionaryItem>
) {
    companion object {
        fun fromContent(content: ByteArray) : VariantDictionary = with(ByteBuffer.wrap(content).order(ByteOrder.LITTLE_ENDIAN)) {
            val contentBuffer = this

            val version = this.getShort().toUShort()

            val items = arrayListOf<VariantDictionaryItem>()
            var type = this.get()
            while(type != 0x00.toByte()) {
                val nameSize = this.getInt()
                val name = with(ByteArray(nameSize)) {
                    contentBuffer.get(this, 0, this.size)
                    this.decodeToString()
                }
                val valueSize = this.getInt()
                val value = ByteArray(valueSize)
                this.get(value, 0, value.size)

                items.add(
                    VariantDictionaryItem(
                    type,
                    nameSize,
                    name,
                    valueSize,
                    value
                )
                )
                type = this.get()
            }


            VariantDictionary(version, items.toTypedArray())
        }
    }
}
