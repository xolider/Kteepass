package dev.vicart.kteepass.crypto.impl

import dev.vicart.kteepass.crypto.IEncryptionAlgorithm
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES encryption/decryption implementation of [IEncryptionAlgorithm]
 */
class AES256 : IEncryptionAlgorithm {

    /**
     * Decrypt provided data
     * @param crypted The crypted data to decrypt
     * @param nonce the IV used at the encryption
     * @param key The master key used for decryption
     */
    override fun decrypt(crypted: ByteArray, nonce: ByteArray, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(nonce))
        return cipher.doFinal(crypted)
    }
}