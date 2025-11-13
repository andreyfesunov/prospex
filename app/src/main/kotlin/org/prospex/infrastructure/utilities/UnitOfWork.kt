package org.prospex.infrastructure.utilities

import androidx.room.withTransaction
import org.prospex.application.utilities.IUnitOfWork
import org.prospex.infrastructure.database.ProspexDatabase

class UnitOfWork(
    private val database: ProspexDatabase
) : IUnitOfWork {
    override suspend fun <T> execute(block: suspend () -> T): T {
        return database.withTransaction {
            block()
        }
    }
}
