package dev.vicart.kteepass.model

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.crypto.PasswordKey
import dev.vicart.kteepass.crypto.decrypt.HMacDecrypter
import dev.vicart.kteepass.exception.DatabaseDecryptionException
import dev.vicart.kteepass.exception.MasterSeedNotFoundException
import dev.vicart.kteepass.loader.helper.model.BlockStream
import dev.vicart.kteepass.utils.encodeToByteArray
import dev.vicart.kteepass.utils.sha256
import dev.vicart.kteepass.utils.sha512

data class KdbxDatabase(
    val header: KdbxHeader,
    val blocks: Array<BlockStream>
) {
    fun unlock(passwordKey: PasswordKey) {
        val passwordHash = passwordKey.hash().sha256()
        val kdf = header.getKeyDerivationFunction()
        val transformedKey = kdf.derive(passwordHash)
        val seed = (header.fields[HeaderConstants.HEADER_FIELD_ID_MASTER_SEED] ?: throw MasterSeedNotFoundException()) as ByteArray
        val hmacBaseKey = (seed + transformedKey + 0x01).sha512()

        header.checkHeaderEncryptedHash(hmacBaseKey)
        buildKdbxPayload(hmacBaseKey)
    }

    private fun buildKdbxPayload(hmacBaseKey: ByteArray) {
        val payloadSize = blocks.sumOf { it.blockSize }
        val payload = ByteArray(payloadSize)

        var offset = 0
        blocks.forEachIndexed { index, block ->
            val decryptedHash = HMacDecrypter.decrypt(index.toLong().toULong(), hmacBaseKey, (index.toULong().encodeToByteArray() + block.blockBytes))
            if(!decryptedHash.contentEquals(block.encryptedHash)) {
                throw DatabaseDecryptionException()
            }
        }
    }
}
