package dev.vicart.kteepass.crypto

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

interface IEncryptionAlgorithm {
    fun decrypt(crypted: ByteArray, nonce: ByteArray, key: ByteArray) : ByteArray

    companion object {
        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }
}