package dev.vicart.kteepass.model

import dev.vicart.kteepass.crypto.IEncryptionAlgorithm

data class KdbxInnerHeader(
    val encryptionAlgorithm: IEncryptionAlgorithm?,
    val encryptionKey: ByteArray?,
    val binaryContent: ByteArray?,
    val size: Int
)
