package dev.vicart.kteepass.loader

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.constant.KteepassConstants
import dev.vicart.kteepass.exception.*
import dev.vicart.kteepass.loader.helper.DatabaseLoaderHelper
import dev.vicart.kteepass.loader.helper.URILoaderHelper
import dev.vicart.kteepass.loader.helper.model.VariantDictionary
import dev.vicart.kteepass.loader.provider.KdbxFileProvider
import dev.vicart.kteepass.model.KdbxDatabase
import dev.vicart.kteepass.model.KdbxHeader
import dev.vicart.kteepass.model.KdbxVersion
import dev.vicart.kteepass.utils.toDecimalInt
import java.io.File
import java.io.InputStream
import java.net.URI
import java.security.MessageDigest

class KdbxDatabaseLoader private constructor(private val helper: DatabaseLoaderHelper) {

    private val warnings = mutableListOf<LoadingWarning>()

    companion object {
        fun from(from: File) : KdbxDatabaseLoader = from(from.inputStream())

        fun from(from: InputStream) : KdbxDatabaseLoader {
            val bytes = from.use {
                it.readBytes()
            }
            val helper = DatabaseLoaderHelper(bytes)
            return KdbxDatabaseLoader(helper)
        }

        fun from(from: URI): KdbxDatabaseLoader = from(URILoaderHelper.loadFrom(from))

        fun from(provider: KdbxFileProvider): KdbxDatabaseLoader = from(provider.get())
    }

    fun load(): KdbxLoadingResult {
        val header = KdbxHeader(
            helper.headerFirstSignature,
            helper.headerSecondSignature,
            KdbxVersion(
                helper.formatVersion.toDecimalInt(offset = 16),
                helper.formatVersion.toDecimalInt()
            ),
            helper.fields,
            helper.headerHash,
            helper.headerHmacHash,
            helper.headerBytes
        )

        checkHeader(header)

        val database = KdbxDatabase(
            header
        )

        return KdbxLoadingResult(
            database,
            warnings
        )
    }

    private fun checkHeader(header: KdbxHeader) {
        if(header.firstSignature != dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIRST_SIGNATURE.toUInt()||
            header.secondSignature != dev.vicart.kteepass.constant.HeaderConstants.HEADER_SECOND_SIGNATURE.toUInt()) {
            throw HeaderSignatureNotMatch()
        }
        if(header.version.major > KteepassConstants.MAJOR_SUPPORTED_KDBX) {
            throw KdbxVersionNotSupportedException()
        }
        if(header.version.minor > KteepassConstants.MINOR_SUPPORTED_KDBX) {
            warnings.add(LoadingWarning.HEADER_MINOR_VERSION_NOT_SUPPORTED)
        }
        if(!(header.fields[dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_END] as ByteArray).contentEquals(
                dev.vicart.kteepass.constant.HeaderConstants.headerEndValue)) {
            throw WrongHeaderEndException()
        }
        arrayOf(dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_KDF, dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_PUBLIC_CUSTOM_DATA).forEach {
            val dictionary = header.fields[it] as VariantDictionary?
            dictionary?.let {
                val majorVersion = it.version.toInt().toDecimalInt(size = 16, offset = 8)
                val minorVersion = it.version.toInt().toDecimalInt(size = 16)
                if(majorVersion > KteepassConstants.MAJOR_SUPPORTED_VARIANT_DICTIONARY) {
                    throw VariantDictionaryVersionNotSupportedException()
                }
                if(minorVersion > KteepassConstants.MINOR_SUPPORTED_VARIANT_DICTIONARY) {
                    warnings.add(LoadingWarning.VARIANT_DIRECTORY_MINOR_VERSION_NOT_SUPPORTED)
                }
            }
        }
        val computedHeaderHash = MessageDigest.getInstance("SHA-256").digest(helper.headerBytes)
        if(!computedHeaderHash.contentEquals(header.hash)) {
            throw DatabaseCorruptedException()
        }
    }
}