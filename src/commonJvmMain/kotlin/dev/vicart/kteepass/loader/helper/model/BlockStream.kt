package dev.vicart.kteepass.loader.helper.model

data class BlockStream(
    val encryptedHash: ByteArray,
    val blockSize: Int,
    val encryptedPayload: ByteArray,
    val blockBytes: ByteArray
)
