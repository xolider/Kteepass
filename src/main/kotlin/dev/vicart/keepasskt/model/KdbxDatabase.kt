package dev.vicart.keepasskt.model

import dev.vicart.keepasskt.helper.DatabaseLoaderHelper
import dev.vicart.keepasskt.helper.URILoaderHelper
import java.io.File
import java.io.InputStream
import java.net.URI

data class KdbxDatabase(
    val header: KdbxHeader
) {

    companion object {
        fun load(from: File) : KdbxDatabase = load(from.inputStream())

        fun load(from: InputStream) : KdbxDatabase {
            return from.use { stream ->
                val bytes = stream.readBytes()
                val helper = DatabaseLoaderHelper(bytes)

                val header = KdbxHeader(
                    helper.headerFirstSignature,
                    helper.headerSecondSignature,
                    KdbxVersion(helper.formatVersion.first, helper.formatVersion.second)
                )

                KdbxDatabase(header)
            }
        }

        fun load(from: URI): KdbxDatabase = load(URILoaderHelper.loadFrom(from))
    }
}
