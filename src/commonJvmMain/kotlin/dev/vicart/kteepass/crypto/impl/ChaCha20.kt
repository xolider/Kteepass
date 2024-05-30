package dev.vicart.kteepass.crypto.impl

import dev.vicart.kteepass.crypto.IEncryptionAlgorithm

/**
 * ChaCha20 encryption/decryption implementation of [IEncryptionAlgorithm]
 */
class ChaCha20 : IEncryptionAlgorithm {

    /**
     * Decrypt provided data
     * @param crypted The crypted data to decrypt
     * @param nonce the nonce used at the encryption
     * @param key The master key used for decryption
     */
    override fun decrypt(crypted: ByteArray, nonce: ByteArray, key: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}