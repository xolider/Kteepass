package dev.vicart.keepasskt.utils

enum class SupportedURIEnum(val scheme: String) {
    HTTP("http"),
    HTTPS("https"),
    FTP("ftp"),
    FILE("file");

    companion object {
        fun isSupported(uriScheme: String) : Boolean {
            return entries.map(SupportedURIEnum::scheme).contains(uriScheme)
        }
    }
}