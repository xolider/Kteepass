package dev.vicart.kteepass.model

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.crypto.decrypt.HMacDecrypter
import dev.vicart.kteepass.exception.DatabaseDecryptionException
import dev.vicart.kteepass.exception.MasterSeedNotFoundException
import dev.vicart.kteepass.loader.helper.model.VariantDictionary
import dev.vicart.kteepass.loader.helper.model.kdf.IKDF
import dev.vicart.kteepass.loader.helper.model.kdf.KDFFactory
import dev.vicart.kteepass.utils.sha256
import dev.vicart.kteepass.utils.sha512

data class KdbxHeader(
    val firstSignature: UInt,
    val secondSignature: UInt,
    val version: KdbxVersion,
    val fields: Map<Byte, Any>,
    val hash: ByteArray,
    val encryptedHash: ByteArray,
    val bytes: ByteArray
) {
    fun getKeyDerivationFunction() : IKDF {
        return KDFFactory.fromVariantDictionary(fields[HeaderConstants.HEADER_FIELD_ID_KDF] as VariantDictionary)
    }

    fun checkHeaderEncryptedHash(masterPassword: ByteArray) {
        val compositeKey = masterPassword.sha256()

        val kdf = getKeyDerivationFunction()
        val transformedKey = kdf.derive(compositeKey)

        val seed = (fields[HeaderConstants.HEADER_FIELD_ID_MASTER_SEED] ?: throw MasterSeedNotFoundException()) as ByteArray

        val hmacBaseKey = (seed + transformedKey + 0x01).sha512()

        val hmacSha256 = HMacDecrypter.decrypt(0xFFFFFFFFFFFFFFFFuL, hmacBaseKey, bytes)

        if(!hmacSha256.contentEquals(encryptedHash)) {
            throw DatabaseDecryptionException()
        }
    }
}
