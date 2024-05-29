package dev.vicart.kteepass.model

import dev.vicart.kteepass.constant.HeaderConstants
import dev.vicart.kteepass.crypto.PasswordKey
import dev.vicart.kteepass.crypto.decrypt.HMacDecrypter
import dev.vicart.kteepass.exception.DatabaseDecryptionException
import dev.vicart.kteepass.exception.MasterSeedNotFoundException
import dev.vicart.kteepass.loader.helper.model.BlockStream
import dev.vicart.kteepass.utils.encodeToByteArray
import dev.vicart.kteepass.utils.sha256
import dev.vicart.kteepass.utils.sha512

data class KdbxDatabase(
    val header: KdbxHeader,
    val rootGroup: DatabaseGroup
) {
    fun getEntries() : List<DatabaseEntry> {
        return recursiveItemChildren(rootGroup).filterIsInstance<DatabaseEntry>()
    }

    private fun recursiveItemChildren(item: AbstractDatabaseItem) : List<AbstractDatabaseItem> {
        val items = mutableListOf<AbstractDatabaseItem>()
        if(item is DatabaseGroup) {
            items.addAll(item.children.flatMap { recursiveItemChildren(it) })
        } else if(item is DatabaseEntry) {
            items.add(item)
        }
        return items
    }
}
