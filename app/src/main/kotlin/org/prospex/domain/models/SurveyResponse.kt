package org.prospex.domain.models

import java.util.*

class SurveyResponse(
    val ideaId: UUID,
    val optionIds: Array<UUID>
)