package org.prospex.domain.models

import org.prospex.domain.value_objects.Score
import java.util.*

class QuestionOption(
    val id: UUID,
    val questionId: UUID,
    val text: String,
    val score: Score,
)