package org.prospex.domain.models

import java.util.*

class Question(
    val id: UUID,
    val text: String,
    val legalType: LegalType
)