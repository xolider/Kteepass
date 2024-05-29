package dev.vicart.kteepass.loader.helper

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.constant.KteepassConstants
import dev.vicart.kteepass.exception.*
import dev.vicart.kteepass.loader.LoadingWarning
import dev.vicart.kteepass.loader.helper.model.BlockStream
import dev.vicart.kteepass.loader.helper.model.VariantDictionary
import dev.vicart.kteepass.utils.toDecimalInt
import dev.vicart.kteepass.utils.toUint
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

class DatabaseLoaderHelper(bytes: ByteArray) {

    private val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

    val headerFirstSignature = buffer.getInt().toUInt()
    val headerSecondSignature = buffer.getInt().toUInt()
    val formatVersion = buffer.getInt()
    val fields: Map<Byte, Any> = buildMap {
        var currentId: Byte
        do {
            currentId = buffer.get()
            this[currentId] = headerFieldExtractor(currentId)
        } while(currentId != 0.toByte())
    }
    val headerBytes = bytes.sliceArray(0 until buffer.position())

    val headerHash = with(ByteArray(32)) {
        buffer.get(this, 0, this.size)
        this
    }

    val headerHmacHash = with(ByteArray(32)) {
        buffer.get(this, 0, this.size)
        this
    }

    val blocks: Array<BlockStream> = buildList {
        var index = 0
        var blockSize: Int
        do {
            val hmac = ByteArray(32)
            buffer.get(hmac, 0, hmac.size)
            val blockBytePos = buffer.position()
            blockSize = buffer.getInt()

            val block = ByteArray(blockSize)
            buffer.get(block, 0, block.size)
            add(index++, BlockStream(hmac, blockSize, block, bytes.sliceArray(blockBytePos until buffer.position())))
        } while (blockSize != 0)
    }.toTypedArray()

    @OptIn(ExperimentalStdlibApi::class)
    private fun headerFieldExtractor(byteId: Byte) : Any {
        val size = buffer.getInt()
        val content = ByteArray(size)
        buffer.get(content, 0, content.size)
        return when(byteId) {
            HeaderConstants.HEADER_FIELD_ID_ENCRYPTION_ALGORITHM -> { //Encryption algorithm
                content.toHexString().uppercase()
            }
            HeaderConstants.HEADER_FIELD_ID_END -> { //End of header fields
                content
            }
            HeaderConstants.HEADER_FIELD_ID_COMPRESSION_ALGORITHM -> { //Compression algorithm (1 = GZip)
                content.toUint().toInt()
            }
            HeaderConstants.HEADER_FIELD_ID_MASTER_SEED -> { //Master salt/seed
                content
            }
            HeaderConstants.HEADER_FIELD_ID_ENCRYPTION_NONCE -> { //Nonce of encryption algorithm (variable size)
                content
            }
            HeaderConstants.HEADER_FIELD_ID_KDF -> { //KDF
                VariantDictionary.fromContent(content).also(::checkVariantDictionary)
            }
            HeaderConstants.HEADER_FIELD_ID_PUBLIC_CUSTOM_DATA -> { //Custom data
                VariantDictionary.fromContent(content).also(::checkVariantDictionary)
            }
            else -> throw UnknownHeaderFieldIDException(byteId)
        }
    }

    private fun checkVariantDictionary(dictionary: VariantDictionary) {
        val majorVersion = dictionary.version.toInt().toDecimalInt(size = 16, offset = 8)
        if(majorVersion > KteepassConstants.MAJOR_SUPPORTED_VARIANT_DICTIONARY) {
            throw VariantDictionaryVersionNotSupportedException()
        }
    }
}
