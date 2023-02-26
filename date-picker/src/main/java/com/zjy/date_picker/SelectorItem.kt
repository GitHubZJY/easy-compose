package com.zjy.date_picker

/**
 * model for selector item
 * ex:
 * 2023: id = 2023 value = 2023 unit = year
 */
internal data class SelectorItem(
    val id: Int,
    val value: String,
    val unit: String,
)