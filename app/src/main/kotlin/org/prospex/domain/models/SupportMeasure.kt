package org.prospex.domain.models

import java.util.*

class SupportMeasure(
    val id: UUID,
    val title: String,
    val measureType: MeasureType,
    val legalTypes: Array<LegalType>,
    val amount: String,
    val features: String,
    val covers: String,
    val whereToApply: String
)