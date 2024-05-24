package dev.vicart.keepasskt.helper

import dev.vicart.keepasskt.exception.URINotSupportedException
import dev.vicart.keepasskt.utils.SupportedURIEnum
import java.io.File
import java.io.InputStream
import java.net.URI

object URILoaderHelper {

    fun loadFrom(uri: URI): InputStream {
        val scheme = SupportedURIEnum.entries.find { it.scheme == uri.scheme } ?: throw URINotSupportedException(uri.scheme)
        return when(scheme) {
            SupportedURIEnum.HTTP, SupportedURIEnum.HTTPS -> uri.toURL().openStream()
            SupportedURIEnum.FILE -> File(uri.path).inputStream()
            SupportedURIEnum.FTP -> {
                throw NotImplementedError()
            }
        }
    }
}