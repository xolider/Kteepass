package dev.vicart.kteepass.crypto.enums

import dev.vicart.kteepass.crypto.IEncryptionAlgorithm
import dev.vicart.kteepass.crypto.impl.AES256
import dev.vicart.kteepass.crypto.impl.ChaCha20

enum class EncryptionAlgorithm(val uuid: String, val implementation: IEncryptionAlgorithm) {
    AES256("31C1F2E6BF714350BE5805216AFC5AFF", AES256()),
    CHACHA20("D6038A2B8B6F4CB5A524339A31DBB59A", ChaCha20());

    companion object {
        fun from(uuid: String) : EncryptionAlgorithm = entries.find { it.uuid == uuid }!!
    }
}