package org.prospex.application.utilities

import java.util.*

interface IAuthContext {
    suspend fun getUserId(): UUID
}