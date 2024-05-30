package dev.vicart.kteepass.model

import dev.vicart.kteepass.exception.DatabaseClosedException

class KdbxDatabase(
    private val pHeader: KdbxHeader?,
    private var pRootGroup: DatabaseGroup?
) {
    val rootGroup get() = pRootGroup ?: throw DatabaseClosedException()
    val header get() = pHeader ?: throw DatabaseClosedException()

    fun getEntries() : List<DatabaseEntry> {
        return recursiveItemChildren(rootGroup).filterIsInstance<DatabaseEntry>()
    }

    /**
     * Remove all references to the database header and payload, and then run garbage collector to ensure
     * data are freed from memory
     */
    fun closeDatabase() {
        pRootGroup = null
        System.gc()
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
