package dev.vicart.kteepass.crypto

/**
 * Interface for supported kind of keys used for encryption/decryption
 * @see dev.vicart.kteepass.crypto.PasswordKey
 */
interface IKey {

    /**
     * @return The encoded form of the key
     */
    fun encode() : ByteArray
}