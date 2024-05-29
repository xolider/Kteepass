package dev.vicart.kteepass.model

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class KdbxVersion(
    val major: Int,
    val minor: Int
) {
    @OptIn(ExperimentalStdlibApi::class)
    fun computeBytes(): ByteArray {
        val version = (major shl 8) or (minor shl 24)
        val binaryVersion = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(version)
        println(binaryVersion.array().toHexString())
        return binaryVersion.array()
    }
}
