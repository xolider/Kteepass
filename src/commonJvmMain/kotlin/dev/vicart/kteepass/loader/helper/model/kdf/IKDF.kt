package dev.vicart.kteepass.loader.helper.model.kdf

interface IKDF {
    val seedSize: Int
    val salt: ByteArray

    fun derive(masterPassword: ByteArray): ByteArray
}