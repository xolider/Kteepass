package dev.vicart.keepasskt.model

data class KdbxHeader(
    val firstSignature: Int,
    val secondSignature: Int,
    val version: KdbxVersion
)
