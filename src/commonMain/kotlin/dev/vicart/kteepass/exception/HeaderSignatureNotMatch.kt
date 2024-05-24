package dev.vicart.kteepass.exception

class HeaderSignatureNotMatch() : SecurityException("Header signature does not match") {
}