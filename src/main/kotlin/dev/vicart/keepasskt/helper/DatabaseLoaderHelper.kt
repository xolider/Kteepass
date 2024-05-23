package dev.vicart.keepasskt.helper

import dev.vicart.keepasskt.constant.HeaderConstants
import dev.vicart.keepasskt.constant.KeepassKtConstants
import dev.vicart.keepasskt.exception.HeaderSignatureNotMatch
import dev.vicart.keepasskt.exception.KdbxVersionNotSupportedException
import dev.vicart.keepasskt.exception.UnknownHeaderFieldIDException
import dev.vicart.keepasskt.exception.WrongHeaderEndException
import dev.vicart.keepasskt.utils.toUint
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID
import kotlin.reflect.KClass

class DatabaseLoaderHelper(bytes: ByteArray) {

    private val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

    val headerFirstSignature = buffer.getInt()
    val headerSecondSignature = buffer.getInt()
    val formatVersion = with(buffer.getInt()) {
        ((this shr 16) and 0xFFFF) to (this and 0xFFFF)
    }
    val fields: Map<Byte, Pair<KClass<*>, Any>> = buildMap {
        var currentId: Byte
        do {
            currentId = buffer.get()
            this[currentId] = headerFieldExtractor(currentId)
        } while(currentId != 0.toByte())
    }

    init {
        checkHeader()
        println(fields)
    }

    private fun checkHeader() {
        if(headerFirstSignature != HeaderConstants.HEADER_FIRST_SIGNATURE.toInt() ||
            headerSecondSignature != HeaderConstants.HEADER_SECOND_SIGNATURE.toInt()) {
            throw HeaderSignatureNotMatch()
        }
        if(KeepassKtConstants.MAJOR_SUPPORTED_KDBX > formatVersion.first) {
            throw KdbxVersionNotSupportedException()
        }
        if(KeepassKtConstants.MINOR_SUPPORTED_KDBX > formatVersion.second) {
            //WARN
        }
        if(!(fields[0.toByte()]!!.second as ByteArray).contentEquals(HeaderConstants.headerEndValue)) {
            println((fields[0x0]!!.second as ByteArray).contentToString())
            throw WrongHeaderEndException()
        }
    }

    private fun headerFieldExtractor(byteId: Byte) : Pair<KClass<*>, Any> {
        val size = buffer.getInt()
        val content = ByteArray(size)
        buffer.get(content, 0, content.size)
        return when(byteId) {
            2.toByte() -> { //Encryption algorithm
                UUID::class to UUID.nameUUIDFromBytes(content)
            }
            0.toByte() -> { //End of header fields
                ByteArray::class to content
            }
            3.toByte() -> { //Compression algorithm (1 = GZip)
                Int::class to content.toUint().toInt()
            }
            4.toByte() -> { //Master salt/seed
                ByteArray::class to content
            }
            7.toByte() -> { //Nonce of encryption algorithm (variable size)
                ByteArray::class to content
            }
            11.toByte() -> { //KDF
                Any::class to Any() //TODO: implement
            }
            12.toByte() -> { //Custom data
                Any::class to Any() //TODO: implement
            }
            else -> throw UnknownHeaderFieldIDException(byteId)
        }
    }
}
