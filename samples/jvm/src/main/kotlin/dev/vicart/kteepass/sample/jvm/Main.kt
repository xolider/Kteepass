package dev.vicart.kteepass.sample.jvm

import dev.vicart.kteepass.crypto.PasswordKey
import dev.vicart.kteepass.loader.KdbxDatabaseLoader
import java.io.File

fun main() {
    val database = KdbxDatabaseLoader.from(File("/home/clement/Documents/Database.kdbx"))
        .open(PasswordKey("test"))
}