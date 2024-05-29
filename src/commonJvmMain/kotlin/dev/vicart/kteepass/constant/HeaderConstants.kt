package dev.vicart.kteepass.constant

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
}