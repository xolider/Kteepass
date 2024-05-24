package dev.vicart.kteepass.exception

class UnknownHeaderFieldIDException(id: Byte) : RuntimeException("Unknown header field ID: $id") {
}