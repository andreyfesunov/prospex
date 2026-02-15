package org.prospex.domain.models

import java.util.*

class LegalTypeSurveyOption(
    val id: UUID,
    val questionId: UUID,
    val text: String,
    val legalType: LegalType,
    val score: Int
)
