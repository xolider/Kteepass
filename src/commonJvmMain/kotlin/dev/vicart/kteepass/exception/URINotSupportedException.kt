package dev.vicart.kteepass.exception

class URINotSupportedException(val scheme: String) : RuntimeException("$scheme:// is not supported.")