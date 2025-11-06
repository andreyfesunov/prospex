package org.prospex.application.utilities

interface UseCase<in Params, out T> {
    suspend fun execute(params: Params): Result<T>
}