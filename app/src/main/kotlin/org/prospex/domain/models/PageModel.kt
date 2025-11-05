package org.prospex.domain.models

class PageModel<T>(
    val items: Array<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Int
)