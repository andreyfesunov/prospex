package org.prospex.domain.value_objects

data class JWT(val payload: String)
{
    init {
        require(payload.isNotBlank()) { "JWT payload cannot be blank." }
        require(payload.count { it == '.' } == 2) { "JWT payload must have three parts separated by dots." }
    }
}