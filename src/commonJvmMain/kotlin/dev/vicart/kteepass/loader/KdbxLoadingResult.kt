package dev.vicart.kteepass.loader

import dev.vicart.kteepass.model.KdbxDatabase

enum class LoadingWarning {
    HEADER_MINOR_VERSION_NOT_SUPPORTED,
    VARIANT_DIRECTORY_MINOR_VERSION_NOT_SUPPORTED
}

data class KdbxLoadingResult(
    val database: KdbxDatabase,
    val warnings: List<LoadingWarning>
)