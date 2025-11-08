package org.prospex.domain.models

import org.prospex.domain.value_objects.Positive

class PageModel<T>(
    val items: Array<T>,
    val page: Positive,
    val pageSize: Positive,
    val totalItems: UInt
)