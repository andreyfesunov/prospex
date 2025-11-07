package org.prospex.infrastructure.utilities

import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.prospex.application.utilities.IUnitOfWork

class UnitOfWork(
    private val db: R2dbcDatabase
) : IUnitOfWork {
    override suspend fun <T> execute(block: suspend () -> T): T {
        return suspendTransaction(db) {
            block()
        }
    }
}