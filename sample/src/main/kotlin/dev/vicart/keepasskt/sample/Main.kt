package dev.vicart.keepasskt.sample

import dev.vicart.keepasskt.model.KdbxDatabase
import java.io.File

fun main() {
    val file = File("/home/clement/Documents/Database.kdbx")

    val database = KdbxDatabase.load(file)
    println("${database.header.version.major}.${database.header.version.minor}")
}