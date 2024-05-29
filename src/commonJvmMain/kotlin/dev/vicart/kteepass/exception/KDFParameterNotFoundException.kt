package dev.vicart.kteepass.exception

class KDFParameterNotFoundException(parameterName: String) : RuntimeException("KDF parameter $parameterName not found") {
}