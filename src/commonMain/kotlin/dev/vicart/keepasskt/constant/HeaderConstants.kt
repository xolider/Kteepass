package dev.vicart.keepasskt.constant

object HeaderConstants {
    const val HEADER_FIRST_SIGNATURE = 0x9AA2D903
    const val HEADER_SECOND_SIGNATURE = 0xB54BFB67

    val headerEndValue = byteArrayOf(0x0D, 0x0A, 0x0D, 0x0A)
}