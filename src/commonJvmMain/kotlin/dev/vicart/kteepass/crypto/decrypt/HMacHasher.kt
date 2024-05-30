package dev.vicart.kteepass.crypto.decrypt

import dev.vicart.kteepass.utils.encodeToByteArray
import dev.vicart.kteepass.utils.sha512
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMacHasher {

    /**
     * Process the HMAC-SHA-256 hash. More information at [Computation of keys](https://keepass.info/help/kb/kdbx.html#keys)
     * @param blockIndex The block index (or 0xFFFFFFFFFFFFFFFF for KDBX header)
     * @param hmacBaseKey The base key to use for HMAC computation
     * @param block the bytes to compute the HMAC on
     * @return The computed HMAC-SHA-256
     */
    fun hash(blockIndex: ULong, hmacBaseKey: ByteArray, block: ByteArray) : ByteArray {
        val hmacKey = (blockIndex.encodeToByteArray() + hmacBaseKey).sha512()
        val mac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(hmacKey, "SHA-512")
        mac.init(secretKey)

        return mac.doFinal(block)
    }
}