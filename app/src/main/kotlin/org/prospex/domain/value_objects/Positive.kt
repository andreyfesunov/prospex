package org.prospex.domain.value_objects

data class Positive(val value: UInt) {
    init {
        require(value != 0u) { "Value must be positive." }
    }
}
