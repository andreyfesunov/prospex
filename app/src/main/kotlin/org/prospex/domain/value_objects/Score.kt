package org.prospex.domain.value_objects

data class Score(val value: UInt) {
    init {
        require(value <= 100u) { "Score can't be greater than 100" }
    }
}