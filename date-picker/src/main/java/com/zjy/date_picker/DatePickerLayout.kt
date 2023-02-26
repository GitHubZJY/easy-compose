package com.zjy.date_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zjy.date_picker.DatePickerLayoutConfig.FUTURE_YEAR_NUMBER
import com.zjy.date_picker.DatePickerLayoutConfig.ITEM_HEIGHT_DP
import com.zjy.date_picker.DatePickerLayoutConfig.ITEM_WIDTH_DP
import com.zjy.date_picker.DatePickerLayoutConfig.SHOWN_ITEM_COUNT
import java.util.*


/**
 * A layout implements the date picker effects, can selected by vertical scrolling, layout with multiple Columns
 *
 * @param itemCount column count of shown area, must be a odd numbers
 * @param itemWidth width of every item, unit: dp
 * @param itemHeight height of every item, unit: dp
 * @param futureYearNum base on current year, provide [FUTURE_YEAR_NUMBER] future years to select
 * @param selectedFontSize set font size of the selected item
 * @param defaultFontSize set font size of the unselected item
 * @param selectedTimeMills time mills of current selected, decide to the current selected item
 */
@Composable
fun DatePickerLayout(
    modifier: Modifier = Modifier,
    itemCount: Int = SHOWN_ITEM_COUNT,
    itemWidth: Dp = ITEM_WIDTH_DP.dp,
    itemHeight: Dp = ITEM_HEIGHT_DP.dp,
    futureYearNum: Int = FUTURE_YEAR_NUMBER,
    selectedFontSize: TextUnit = 20.sp,
    defaultFontSize: TextUnit = 16.sp,
    selectedTimeMills: Long?,
    onSelectedChanged: (year: Int, month: Int, day: Int) -> Unit,
) {

    val currentYear = getCalender(System.currentTimeMillis()).get(Calendar.YEAR)
    val years = LinkedList<SelectorItem>().apply {
        for (year in 1980..(currentYear + futureYearNum)) {
            add(SelectorItem(id = year, value = "$year", unit = DatePickerColumnUnit.YEAR))
        }
    }
    val months = LinkedList<SelectorItem>().apply {
        for (month in 1..12) {
            add(SelectorItem(id = month, value = "$month", unit = DatePickerColumnUnit.MONTH))
        }
    }
    val selectedYearState = remember { mutableStateOf(years[0]) }
    val selectedMonthState = remember { mutableStateOf(months[0]) }
    val dayCountOfMonth =
        getDayCountOfMonth(selectedYearState.value.id, selectedMonthState.value.id)
    val days = LinkedList<SelectorItem>().apply {
        for (day in 1..dayCountOfMonth) {
            add(SelectorItem(id = day, value = "$day", unit = DatePickerColumnUnit.DAY))
        }
    }
    val selectedDayState = remember { mutableStateOf(days[0]) }

    val invokeSelected = {
        onSelectedChanged(
            selectedYearState.value.id,
            selectedMonthState.value.id,
            selectedDayState.value.id,
        )
    }

    if (days.find { it.id == selectedDayState.value.id } == null) {
        //current selected month don't contains selected day,
        //ex: month: 7->6, 1->2
        //should select to the first day
        selectedDayState.value = days[0]
        invokeSelected()
    }

    val selectedCalendar = Calendar.getInstance().apply {
        //if selectedTimeMills is empty, use current time millis as a default selected
        timeInMillis = selectedTimeMills ?: System.currentTimeMillis()
    }
    val selectedYear = selectedCalendar.get(Calendar.YEAR)
    val selectedMonth = selectedCalendar.get(Calendar.MONTH) + 1
    val selectedDay = selectedCalendar.get(Calendar.DAY_OF_MONTH)
    years.find { it.id == selectedYear }?.let { initializationYear ->
        selectedYearState.value = initializationYear
    }
    months.find { it.id == selectedMonth }?.let { initializationMonth ->
        selectedMonthState.value = initializationMonth
    }
    days.find { it.id == selectedDay }?.let { initializationDay ->
        selectedDayState.value = initializationDay
    }

    LaunchedEffect(Unit) {
        //select a date in the first initialization, also callback the selected result
        invokeSelected()
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .padding(horizontal = 44.dp)
            .clipToBounds(),
    ) {
        /** selected bar in center */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(color = Color(0xfff7f7f7), shape = RoundedCornerShape(4.dp))
                .align(Alignment.Center),
        )

        /** picker content layout */
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterVertically,
        ) {

            /** years' column */
            DatePickerColumnLayout(
                modifier = Modifier.width(itemWidth),
                shownItemCount = itemCount,
                itemHeight = itemHeight,
                selectedFontSize = selectedFontSize,
                defaultFontSize = defaultFontSize,
                items = years,
                selectedItem = selectedYearState.value,
                onSelectedChange = {
                    selectedYearState.value = it
                    invokeSelected()
                },
            )
            /** months' column */
            DatePickerColumnLayout(
                modifier = Modifier.width(itemWidth),
                shownItemCount = itemCount,
                itemHeight = itemHeight,
                selectedFontSize = selectedFontSize,
                defaultFontSize = defaultFontSize,
                items = months,
                selectedItem = selectedMonthState.value,
                onSelectedChange = {
                    selectedMonthState.value = it
                    invokeSelected()
                },
            )
            /** days' column */
            DatePickerColumnLayout(
                modifier = Modifier.width(itemWidth),
                shownItemCount = itemCount,
                itemHeight = itemHeight,
                selectedFontSize = selectedFontSize,
                defaultFontSize = defaultFontSize,
                items = days,
                selectedItem = selectedDayState.value,
                onSelectedChange = {
                    selectedDayState.value = it
                    invokeSelected()
                },
            )
        }

        /** gradient bar in top */
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(
                    brush = Brush.verticalGradient(
                        0F to MaterialTheme.colors.surface,
                        1F to Color.Transparent,
                    )
                )
                .align(Alignment.TopCenter),
        )

        /** gradient bar in bottom */
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(
                    brush = Brush.verticalGradient(
                        0F to Color.Transparent,
                        1F to MaterialTheme.colors.surface,
                    )
                )
                .align(Alignment.BottomCenter),
        )
    }
}

private fun getCalender(tsMillis: Long): Calendar {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = tsMillis
    return calendar
}

private fun getDayCountOfMonth(year: Int, month: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

@Preview
@Composable
private fun DatePickerLayoutPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        DatePickerLayout(
            selectedTimeMills = System.currentTimeMillis(),
            onSelectedChanged = { _, _, _ ->

            }
        )
    }
}