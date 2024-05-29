package dev.vicart.kteepass.loader.helper.model.kdf

import dev.vicart.kteepass.constant.KDFConstants
import dev.vicart.kteepass.exception.KDFParameterNotFoundException
import dev.vicart.kteepass.loader.helper.model.VariantDictionary
import dev.vicart.kteepass.utils.toInt
import dev.vicart.kteepass.utils.toLong

object KDFFactory {

    @OptIn(ExperimentalStdlibApi::class)
    fun fromVariantDictionary(dictionary: VariantDictionary) : IKDF {
        val uuid = getKDFParameterValue(dictionary, KDFConstants.UUID_NAME).toHexString()
        val salt = getKDFParameterValue(dictionary, KDFConstants.SEED_NAME)
        return when(uuid.uppercase()) {
            KDFConstants.AES_KDF_IDENTIFIER -> AESKDF(salt)
            KDFConstants.ARGON2D_KDF_IDENTIFIER -> {
                val iterations = getKDFParameterValue(dictionary, KDFConstants.ITERATIONS_NAME).toLong().toULong()
                val memory = getKDFParameterValue(dictionary, KDFConstants.MEMORY_NAME).toLong().toULong()
                val parallelism = getKDFParameterValue(dictionary, KDFConstants.PARALLELISM_NAME).toInt().toUInt()
                val version = getKDFParameterValue(dictionary, KDFConstants.VERSION_NAME).toInt().toUInt()
                Argon2dKDF(salt, salt.size, iterations, memory, parallelism, version)
            }
            KDFConstants.ARGON2ID_KDF_IDENTIFIER -> throw NotImplementedError()
            else -> throw NotImplementedError()
        }
    }

    private fun getKDFParameterValue(dictionary: VariantDictionary, parameterName: String) : ByteArray {
        return (dictionary.items.find { it.name == parameterName }?.value ?: throw KDFParameterNotFoundException(parameterName)) as ByteArray
    }
}