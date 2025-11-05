package org.prospex.domain.value_objects

data class Email (val value: String) {
    init {
        require(value.isNotBlank()) { "Email cannot be blank." }
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        require(emailRegex.matches(value)) { "Invalid email format." }
    }
}