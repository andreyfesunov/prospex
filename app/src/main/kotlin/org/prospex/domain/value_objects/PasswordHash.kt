package org.prospex.domain.value_objects

data class PasswordHash(val hash: String) {
    init {
        require(hash.isNotBlank()) { "PasswordHash cannot be blank." }
        val sha256Regex = Regex("^[a-fA-F0-9]{64}$")
        require(sha256Regex.matches(hash)) { "PasswordHash must be a valid SHA-256 hash." }
    }

    fun fromPlainText(plainText: String): PasswordHash {
        require(plainText.isNotBlank()) { "Password cannot be blank." }
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(plainText.toByteArray(Charsets.UTF_8))
        val hash = hashBytes.joinToString("") { "%02x".format(it) }
        return PasswordHash(hash)
    }
}