package dev.vicart.kteepass.sample.jvm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.vicart.kteepass.crypto.PasswordKey
import dev.vicart.kteepass.loader.KdbxDatabaseLoader
import java.io.File

fun main() {
    KdbxDatabaseLoader.from(File("/home/clement/Documents/Database.kdbx"))
        .open(PasswordKey("test"))
}
