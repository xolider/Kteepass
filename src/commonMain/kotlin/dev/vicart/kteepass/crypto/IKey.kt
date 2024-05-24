package dev.vicart.kteepass.crypto

interface IKey {
    fun hash() : ByteArray
}