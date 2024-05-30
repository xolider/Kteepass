package dev.vicart.kteepass.loader

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.constant.KteepassConstants
import dev.vicart.kteepass.crypto.IKey
import dev.vicart.kteepass.crypto.decrypt.DatabaseDecrypter
import dev.vicart.kteepass.exception.*
import dev.vicart.kteepass.loader.helper.DatabaseLoaderHelper
import dev.vicart.kteepass.loader.helper.URILoaderHelper
import dev.vicart.kteepass.loader.helper.model.VariantDictionary
import dev.vicart.kteepass.loader.provider.KdbxFileProvider
import dev.vicart.kteepass.model.KdbxDatabase
import dev.vicart.kteepass.model.KdbxHeader
import dev.vicart.kteepass.model.KdbxVersion
import dev.vicart.kteepass.utils.sha256
import dev.vicart.kteepass.utils.toDecimalInt
import java.io.File
import java.io.InputStream
import java.net.URI
import java.security.MessageDigest

class KdbxDatabaseLoader private constructor(private val bytes: ByteArray) {

    companion object {
        fun from(from: File) : KdbxDatabaseLoader = from(from.inputStream())

        fun from(from: InputStream) : KdbxDatabaseLoader {
            val bytes = from.use {
                it.readBytes()
            }
            return KdbxDatabaseLoader(bytes)
        }

        fun from(from: URI): KdbxDatabaseLoader = from(URILoaderHelper.loadFrom(from))

        fun from(provider: KdbxFileProvider): KdbxDatabaseLoader = from(provider.get())
    }

    fun open(vararg keys: IKey) : KdbxDatabase {
        val helper = DatabaseLoaderHelper(bytes)
        val header = KdbxHeader(helper)

        checkHeader(header)

        val decrypter = DatabaseDecrypter(header, helper.blocks, keys)
        return decrypter.getDecryptedDatabase()
    }

    private fun checkHeader(header: KdbxHeader) {
        if(header.firstSignature != HeaderConstants.HEADER_FIRST_SIGNATURE.toUInt()||
            header.secondSignature != HeaderConstants.HEADER_SECOND_SIGNATURE.toUInt()) {
            throw HeaderSignatureNotMatch()
        }
        if(header.version.major > KteepassConstants.MAJOR_SUPPORTED_KDBX) {
            throw KdbxVersionNotSupportedException()
        }
        if(!header.endHeader.contentEquals(HeaderConstants.headerEndValue)) {
            throw WrongHeaderEndException()
        }
        val computedHeaderHash = header.headerBytes.sha256()
        if(!computedHeaderHash.contentEquals(header.sha256Hash)) {
            throw DatabaseCorruptedException()
        }
    }
}