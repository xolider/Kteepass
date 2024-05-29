package dev.vicart.kteepass.model

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.crypto.enums.EncryptionAlgorithm
import dev.vicart.kteepass.crypto.decrypt.HMacDecrypter
import dev.vicart.kteepass.crypto.enums.CompressionAlgorithm
import dev.vicart.kteepass.exception.DatabaseDecryptionException
import dev.vicart.kteepass.loader.helper.DatabaseLoaderHelper
import dev.vicart.kteepass.loader.helper.model.VariantDictionary
import dev.vicart.kteepass.loader.helper.model.kdf.IKDF
import dev.vicart.kteepass.loader.helper.model.kdf.KDFFactory
import dev.vicart.kteepass.utils.toDecimalInt
import java.util.UUID

class KdbxHeader(helper: DatabaseLoaderHelper) {

    val firstSignature: UInt = helper.headerFirstSignature
    val secondSignature: UInt = helper.headerSecondSignature
    val version = with(helper.formatVersion) {
        KdbxVersion(this.toDecimalInt(offset = 16), this.toDecimalInt())
    }
    val encryptionAlgorithm = EncryptionAlgorithm.from(helper.fields[HeaderConstants.HEADER_FIELD_ID_ENCRYPTION_ALGORITHM].toString())
    val compression = if((helper.fields[HeaderConstants.HEADER_FIELD_ID_COMPRESSION_ALGORITHM] as Int) == 1) CompressionAlgorithm.GZIP else CompressionAlgorithm.NONE
    val masterSeed = helper.fields[HeaderConstants.HEADER_FIELD_ID_MASTER_SEED] as ByteArray
    val encryptionIV = helper.fields[HeaderConstants.HEADER_FIELD_ID_ENCRYPTION_NONCE] as ByteArray
    val kdf = with(helper.fields[HeaderConstants.HEADER_FIELD_ID_KDF] as VariantDictionary) {
        KDFFactory.fromVariantDictionary(this)
    }
    val publicData = helper.fields[HeaderConstants.HEADER_FIELD_ID_PUBLIC_CUSTOM_DATA] as VariantDictionary?
    val endHeader = helper.fields[HeaderConstants.HEADER_FIELD_ID_END] as ByteArray
    val headerBytes = helper.headerBytes
    val sha256Hash = helper.headerHash
    val hmacSha256Hash = helper.headerHmacHash
}
