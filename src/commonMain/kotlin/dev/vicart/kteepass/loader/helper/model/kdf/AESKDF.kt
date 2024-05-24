package dev.vicart.kteepass.loader.helper.model.kdf

data class AESKDF(
    override val salt: ByteArray
) : IKDF {
    override val seedSize: Int = 32

    override fun derive(masterPassword: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}