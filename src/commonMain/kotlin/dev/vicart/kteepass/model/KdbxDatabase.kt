package dev.vicart.kteepass.model

import dev.vicart.kteepass.crypto.PasswordKey

data class KdbxDatabase(
    val header: KdbxHeader
) {
    fun unlock(passwordKey: PasswordKey) {
        val passwordHash = passwordKey.hash()
        header.checkHeaderEncryptedHash(passwordHash)
    }
}
