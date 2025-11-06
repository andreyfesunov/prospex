package org.prospex.application.utilities

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Exception? = null) : Result<Nothing>()
}