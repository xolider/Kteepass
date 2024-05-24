package dev.vicart.kteepass.loader.helper.model.kdf

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters

data class Argon2dKDF(
    override val salt: ByteArray,
    override val seedSize: Int,
    val iterations: ULong,
    val memory: ULong,
    val parallelism: UInt,
    val version: UInt
) : IKDF {
    override fun derive(masterPassword: ByteArray) : ByteArray {
        val parameters = Argon2Parameters.Builder(Argon2Parameters.ARGON2_d)
            .withSalt(salt)
            .withIterations(iterations.toInt())
            .withMemoryAsKB(memory.toInt()/1024)
            .withParallelism(parallelism.toInt())
            .build()

        val outBytes = ByteArray(32)

        Argon2BytesGenerator().apply {
            init(parameters)
        }.generateBytes(masterPassword, outBytes)

        return outBytes
    }
}
