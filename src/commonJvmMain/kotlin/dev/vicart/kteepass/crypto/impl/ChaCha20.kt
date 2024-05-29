package dev.vicart.kteepass.crypto.impl

import dev.vicart.kteepass.crypto.IEncryptionAlgorithm

class ChaCha20 : IEncryptionAlgorithm {
    override fun decrypt(crypted: ByteArray, nonce: ByteArray, key: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}