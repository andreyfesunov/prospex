package org.prospex.domain.models

import org.prospex.domain.value_objects.Score
import java.util.*

class SupportMeasure(
    val id: UUID,
    val title: String,
    val minScore: Score
)