package org.prospex.application.utilities

interface IUnitOfWork {
    suspend fun <T> execute(block: suspend () -> T): T
}