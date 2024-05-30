package dev.vicart.kteepass.model

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class KdbxVersion(
    val major: Int,
    val minor: Int
)
