package org.prospex.domain.value_objects

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

data class JWT(val payload: String)
{
    init {
        require(payload.isNotBlank()) { "JWT payload cannot be blank." }
        require(payload.count { it == '.' } == 2) { "JWT payload must have three parts separated by dots." }
    }

    fun getEmail(signingKey: String): String? {
        return try {
            val key = Keys.hmacShaKeyFor(signingKey.toByteArray())
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(payload)
                .payload
            
            claims["email"] as? String ?: claims.subject
        } catch (e: Exception) {
            null
        }
    }
}