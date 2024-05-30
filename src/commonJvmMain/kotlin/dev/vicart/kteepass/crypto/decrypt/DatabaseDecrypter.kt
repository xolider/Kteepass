package dev.vicart.kteepass.crypto.decrypt

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.crypto.IKey
import dev.vicart.kteepass.crypto.enums.CompressionAlgorithm
import dev.vicart.kteepass.crypto.impl.ChaCha20
import dev.vicart.kteepass.exception.DatabaseDecryptionException
import dev.vicart.kteepass.loader.helper.model.BlockStream
import dev.vicart.kteepass.model.DatabaseGroup
import dev.vicart.kteepass.model.KdbxDatabase
import dev.vicart.kteepass.model.KdbxHeader
import dev.vicart.kteepass.model.KdbxInnerHeader
import dev.vicart.kteepass.utils.encodeToByteArray
import dev.vicart.kteepass.utils.sha256
import dev.vicart.kteepass.utils.sha512
import dev.vicart.kteepass.utils.toInt
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.GZIPInputStream

/**
 * Class used for decrypting the database
 *
 * @param header The [KDBX Header][KdbxHeader] containing useful information about encryption
 * @param blocks Array of [block stream][BlockStream] to decrypt, optionally decompress and merge
 * @param key The [master key][IKey] provided to decrypt the database
 */
class DatabaseDecrypter(private val header: KdbxHeader, private val blocks: Array<BlockStream>, private val keys: Array<out IKey>) {

    private val compositeKey = keys.map(IKey::encode).reduce(ByteArray::plus).sha256()
    private val transformedKey = header.kdf.derive(compositeKey)
    private val hmacBaseKey = (header.masterSeed + transformedKey + 0x01).sha512()

    /**
     * Process the decryption and decompression of the database
     * @return The entire decrypted database (but not decrypted passwords)
     */
    fun getDecryptedDatabase() : KdbxDatabase {
        val mergedBlocks = blocks.mapIndexed { index, block ->
            checkAndDecryptBlock(index, block)
        }.reduce(ByteArray::plus)
        val innerHeader = extractInnerHeader(mergedBlocks)
        val xml = mergedBlocks.sliceArray(innerHeader.size until mergedBlocks.size)
        return KdbxDatabase(DatabaseGroup("", emptyList()))
    }

    private fun extractInnerHeader(blocks: ByteArray) : KdbxInnerHeader {
        val buffer = ByteBuffer.wrap(blocks).order(ByteOrder.LITTLE_ENDIAN)
        val fields = buildMap<Byte, ByteArray> {
            var byteId = buffer.get()
            while (byteId != HeaderConstants.INNER_HEADER_FIELD_ID_END) {
                val size = buffer.getInt()
                val content = ByteArray(size)
                buffer.get(content, 0, content.size)
                put(byteId, content)
                byteId = buffer.get()
            }
        }
        buffer.getInt()
        return KdbxInnerHeader(
            when(fields[HeaderConstants.INNER_HEADER_FIELD_ID_ENCRYPTION_ALGORITHM]?.toInt()) {
                3 -> ChaCha20()
                else -> null
            },
            fields[HeaderConstants.INNER_HEADER_FIELD_ID_ENCRYPTION_KEY],
            fields[HeaderConstants.INNER_HEADER_FIELD_ID_BINARY_CONTENT],
            buffer.position()
        )
    }

    private fun checkAndDecryptBlock(index: Int, block: BlockStream) : ByteArray {
        val computedHash = HMacHasher.hash(index.toULong(), hmacBaseKey,
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