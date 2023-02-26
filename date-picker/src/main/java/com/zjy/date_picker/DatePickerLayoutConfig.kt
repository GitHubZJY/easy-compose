package com.zjy.date_picker

/**
 * some default configs of date picker layout
 */
internal object DatePickerLayoutConfig {

    /** column count of shown area, must be a odd numbers */
    const val SHOWN_ITEM_COUNT = 5

    /** base on current year, provide [FUTURE_YEAR_NUMBER] future years to select */
    const val FUTURE_YEAR_NUMBER = 1

    /** width of every item, unit: dp */
    const val ITEM_WIDTH_DP = 80

    /** height of every item, unit: dp */
    const val ITEM_HEIGHT_DP = 30
}