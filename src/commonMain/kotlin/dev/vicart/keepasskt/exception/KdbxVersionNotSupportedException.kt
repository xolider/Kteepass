package dev.vicart.keepasskt.exception

import dev.vicart.keepasskt.constant.KeepassKtConstants

class KdbxVersionNotSupportedException :
    RuntimeException("KDBX version not supported. Major version supported: ${KeepassKtConstants.MAJOR_SUPPORTED_KDBX}")