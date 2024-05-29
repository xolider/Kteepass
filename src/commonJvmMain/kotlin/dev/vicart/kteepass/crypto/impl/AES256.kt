package dev.vicart.kteepass.crypto.impl

import dev.vicart.kteepass.crypto.IEncryptionAlgorithm
import java.security.AlgorithmParameters
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AES256 : IEncryptionAlgorithm {
    override fun decrypt(crypted: ByteArray, nonce: ByteArray, key: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(nonce))
        return cipher.doFinal(crypted)
    }
}