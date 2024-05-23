package dev.vicart.keepasskt.exception

class UnknownHeaderFieldIDException(id: Byte) : RuntimeException("Unknown header field ID: $id") {
}