package dev.vicart.kteepass.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.MessageDigest

fun Int.encodeToByteArray() : ByteArray =
    ByteBuffer.allocate(Int.SIZE_BYTES).order(ByteOrder.LITTLE_ENDIAN).putInt(this).array()

fun Int.toDecimalInt(size: Int = 32, offset: Int = 0) : Int = (this shr offset) and ((size * 8) - 1)

fun ByteArray.toLong() : Long = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).getLong()
fun ByteArray.toInt() : Int = ByteBuffer.wrap(this).order(ByteOrder.LITTLE_ENDIAN).getInt()

fun ByteArray.sha256() : ByteArray = MessageDigest.getInstance("SHA-256").digest(this)
fun ByteArray.sha512() : ByteArray = MessageDigest.getInstance("SHA-512").digest(this)

fun ULong.encodeToByteArray() : ByteArray = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(this.toLong()).array()