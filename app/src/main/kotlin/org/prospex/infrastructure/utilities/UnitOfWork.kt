package org.prospex.infrastructure.utilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.prospex.application.utilities.IUnitOfWork

class UnitOfWork : IUnitOfWork {
    private val database = Database.connect(
        "jdbc:sqlite:business_ideas.db",
        "org.sqlite.JDBC"
    )

    override suspend fun <T> execute(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            transaction(database) { runBlocking { block() } }
        }
    }
}