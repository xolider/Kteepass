package dev.vicart.kteepass.model

data class DatabaseGroup(
    val name: String,
    val children: List<AbstractDatabaseItem>
) : AbstractDatabaseItem()
