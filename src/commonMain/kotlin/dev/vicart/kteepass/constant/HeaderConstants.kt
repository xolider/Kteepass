package dev.vicart.kteepass.constant

import dev.vicart.kteepass.loader.helper.model.VariantDictionary
import java.nio.ByteBuffer
import java.util.UUID
import kotlin.reflect.typeOf

object HeaderConstants {
    const val HEADER_FIRST_SIGNATURE = 0x9AA2D903
    const val HEADER_SECOND_SIGNATURE = 0xB54BFB67

    val headerEndValue = byteArrayOf(0x0D, 0x0A, 0x0D, 0x0A)

    const val HEADER_FIELD_ID_ENCRYPTION_ALGORITHM: Byte = 2
    const val HEADER_FIELD_ID_COMPRESSION_ALGORITHM: Byte = 3
    const val HEADER_FIELD_ID_MASTER_SEED: Byte = 4
    const val HEADER_FIELD_ID_ENCRYPTION_NONCE: Byte = 7
    const val HEADER_FIELD_ID_KDF: Byte = 11
    const val HEADER_FIELD_ID_PUBLIC_CUSTOM_DATA: Byte = 12
    const val HEADER_FIELD_ID_END: Byte = 0

    val headerHmacConstant = ByteBuffer.allocate(8).putLong(0xFFFFFFFFFFFFFFFFu.toLong())

    val fieldTypes = mapOf(
        HEADER_FIELD_ID_ENCRYPTION_ALGORITHM to typeOf<UUID>(),
        HEADER_FIELD_ID_COMPRESSION_ALGORITHM to typeOf<UInt>(),
        HEADER_FIELD_ID_MASTER_SEED to typeOf<ByteArray>(),
        HEADER_FIELD_ID_ENCRYPTION_NONCE to typeOf<ByteArray>(),
        HEADER_FIELD_ID_KDF to typeOf<VariantDictionary>(),
        HEADER_FIELD_ID_PUBLIC_CUSTOM_DATA to typeOf<VariantDictionary>(),
        HEADER_FIELD_ID_END to typeOf<ByteArray>()
    )
}