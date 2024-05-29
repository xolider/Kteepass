package dev.vicart.kteepass.loader.helper

import dev.vicart.kteepass.exception.URINotSupportedException
import dev.vicart.kteepass.utils.SupportedURIEnum
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