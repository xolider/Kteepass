package dev.vicart.kteepass.loader.helper.model

import kotlin.reflect.KClass

data class VariantDictionaryItem(
    val valueType: Byte,
    val nameSize: Int,
    val name: String,
    val valueSize: Int,
    val value: Any
) {
    companion object {
        val valueTypes: Map<Byte, KClass<*>> = mapOf(
            0x04.toByte() to UInt::class,
            0x05.toByte() to ULong::class,
            0x08.toByte() to Boolean::class,
            0x0C.toByte() to Int::class,
            0x0D.toByte() to Long::class,
            0x18.toByte() to String::class,
            0x42.toByte() to ByteArray::class
        )
    }
}
