package dev.vicart.kteepass.crypto.decrypt

import dev.vicart.kteepass.utils.encodeToByteArray
import dev.vicart.kteepass.utils.sha512
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMacDecrypter {

    fun decrypt(blockIndex: ULong, hmacBaseKey: ByteArray, block: ByteArray) : ByteArray {
        val hmacKey = (blockIndex.encodeToByteArray() + hmacBaseKey).sha512()
        val mac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(hmacKey, "SHA-512")
        mac.init(secretKey)

        return mac.doFinal(block)
    }
}