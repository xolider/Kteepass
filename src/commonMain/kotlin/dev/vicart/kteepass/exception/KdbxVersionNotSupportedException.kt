package dev.vicart.kteepass.exception

import dev.vicart.kteepass.constant.KteepassConstants

class KdbxVersionNotSupportedException :
    RuntimeException("KDBX version not supported. Major version supported: ${KteepassConstants.MAJOR_SUPPORTED_KDBX}")