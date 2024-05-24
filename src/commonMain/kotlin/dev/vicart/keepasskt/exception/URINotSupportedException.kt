package dev.vicart.keepasskt.exception

class URINotSupportedException(val scheme: String) : RuntimeException("$scheme:// is not supported.")