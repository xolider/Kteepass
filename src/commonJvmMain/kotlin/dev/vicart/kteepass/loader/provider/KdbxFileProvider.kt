package dev.vicart.kteepass.loader.provider

import java.io.InputStream

interface KdbxFileProvider {
    fun get(): InputStream
}