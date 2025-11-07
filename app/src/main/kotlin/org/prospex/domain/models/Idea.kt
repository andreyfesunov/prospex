package org.prospex.domain.models

import java.util.UUID

class Idea(
    val id: UUID,
    val userId: UUID,
    val title: String
)