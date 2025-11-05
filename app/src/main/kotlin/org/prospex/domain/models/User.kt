package org.prospex.domain.models

import org.prospex.domain.value_objects.Email
import java.util.UUID

class User (
    val id: UUID,
    val email: Email,
)