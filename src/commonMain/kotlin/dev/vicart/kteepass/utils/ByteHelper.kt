package dev.vicart.kteepass.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun ByteArray.toUint(size: Int = 4, offset: Int = 0): UInt = ByteBuffer.wrap(this, offset, size).order(ByteOrder.LITTLE_ENDIAN).getInt().toUInt()