package org.prospex.domain.models

import java.util.*

class EntrepreneurshipForm(
    val id: UUID,
    val title: String,
    val legalType: LegalType,
    val description: String,
    val features: String,
    val requirements: String
)
