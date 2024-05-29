package dev.vicart.kteepass.crypto

import dev.vicart.kteepass.utils.sha256

data class PasswordKey(val password: String) : IKey {

    override fun hash(): ByteArray {
        return password.encodeToByteArray().sha256()
    }
}
