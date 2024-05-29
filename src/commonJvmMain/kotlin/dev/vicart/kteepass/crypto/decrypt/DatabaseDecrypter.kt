package dev.vicart.kteepass.crypto.decrypt

import dev.vicart.kteepass.crypto.IKey
import dev.vicart.kteepass.crypto.enums.CompressionAlgorithm
import dev.vicart.kteepass.exception.DatabaseDecryptionException
import dev.vicart.kteepass.loader.helper.model.BlockStream
import dev.vicart.kteepass.model.DatabaseGroup
import dev.vicart.kteepass.model.KdbxDatabase
import dev.vicart.kteepass.model.KdbxHeader
import dev.vicart.kteepass.utils.encodeToByteArray
import dev.vicart.kteepass.utils.sha256
import dev.vicart.kteepass.utils.sha512
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.util.zip.GZIPInputStream

class DatabaseDecrypter(private val header: KdbxHeader, private val blocks: Array<BlockStream>, private val key: IKey) {

    private val transformedKey = header.kdf.derive(key.hash().sha256())
    private val hmacBaseKey = (header.masterSeed + transformedKey + 0x01).sha512()

    fun getDecryptedDatabase() : KdbxDatabase {
        val mergedBlocks = blocks.mapIndexed { index, block ->
            checkAndDecryptBlock(index, block)
        }.reduce(ByteArray::plus)
        println(mergedBlocks.decodeToString())
        return KdbxDatabase(header, DatabaseGroup("", emptyList()))
    }

    private fun checkAndDecryptBlock(index: Int, block: BlockStream) : ByteArray {
        val computedHash = HMacDecrypter.decrypt(index.toULong(), hmacBaseKey,
            (index.toULong().encodeToByteArray() + block.blockBytes))
        if(!computedHash.contentEquals(block.encryptedHash)) {
            throw DatabaseDecryptionException()
        }
        val decrypted = decrypt(block.encryptedPayload)
        val finalBlock = if(header.compression == CompressionAlgorithm.GZIP) decompress(decrypted) else decrypted
        return finalBlock
    }

    private fun decrypt(blockPayload: ByteArray) : ByteArray {
        val key = (header.masterSeed + transformedKey).sha256()
        val algorithm = header.encryptionAlgorithm.implementation

        return algorithm.decrypt(blockPayload, header.encryptionIV, key)
    }

    private fun decompress(input: ByteArray) : ByteArray {
        val result = ByteArrayOutputStream()
        try  {
            return GZIPInputStream(ByteArrayInputStream(input)).use {
                it.copyTo(result)
                result.close()
                result.toByteArray()
            }
        } catch (e: EOFException) {

        }
        return byteArrayOf()
    }
}