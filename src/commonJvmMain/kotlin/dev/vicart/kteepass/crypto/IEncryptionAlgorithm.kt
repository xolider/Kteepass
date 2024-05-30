package dev.vicart.kteepass.crypto

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

/**
 * Interface for supported encryption/decryption algorithms
 * @see dev.vicart.kteepass.crypto.impl.AES256
 * @see dev.vicart.kteepass.crypto.impl.ChaCha20
 */
interface IEncryptionAlgorithm {

    /**
     * Decrypt provided data
     * @param crypted The crypted data to decrypt
     * @param nonce the nonce/IV used at the encryption
     * @param key The master key used for decryption
     */
    fun decrypt(crypted: ByteArray, nonce: ByteArray, key: ByteArray) : ByteArray

    companion object {
        /**
         * Add the Bouncy Castle library to JCE
         */
        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }
}