package dev.vicart.kteepass.loader.helper

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.exception.*
import dev.vicart.kteepass.loader.helper.model.VariantDictionary
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

    private fun headerFieldExtractor(byteId: Byte) : Any {
        val size = buffer.getInt()
        val content = ByteArray(size)
        buffer.get(content, 0, content.size)
        return when(byteId) {
            dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_ENCRYPTION_ALGORITHM -> { //Encryption algorithm
                UUID.nameUUIDFromBytes(content)
            }
            dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_END -> { //End of header fields
                content
            }
            dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_COMPRESSION_ALGORITHM -> { //Compression algorithm (1 = GZip)
                content.toUint().toInt()
            }
            dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_MASTER_SEED -> { //Master salt/seed
                content
            }
            dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_ENCRYPTION_NONCE -> { //Nonce of encryption algorithm (variable size)
                content
            }
            dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_KDF -> { //KDF
                VariantDictionary.fromContent(content)
            }
            dev.vicart.kteepass.constant.HeaderConstants.HEADER_FIELD_ID_PUBLIC_CUSTOM_DATA -> { //Custom data
                VariantDictionary.fromContent(content)
            }
            else -> throw UnknownHeaderFieldIDException(byteId)
        }
    }
}
