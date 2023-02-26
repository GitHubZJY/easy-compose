package com.zjy.date_picker

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
internal fun DatePickerColumnLayout(
    modifier: Modifier = Modifier,
    selectedItem: SelectorItem?,
    onSelectedChange: (SelectorItem) -> Unit,
    items: List<SelectorItem>,
    itemHeight: Dp,
    shownItemCount: Int,
    selectedFontSize: TextUnit,
    defaultFontSize: TextUnit,
) {
    val columnHeightPx = with(LocalDensity.current) { itemHeight.toPx() }

    val coroutineScope = rememberCoroutineScope()

    val animatedOffset = remember { Animatable(0f) }
        .apply {
            val index = items.indexOf(selectedItem)
            if (index >= 0) {
                val offsetRange = remember(selectedItem, items) {
                    -((items.count() - 1) - index) * columnHeightPx to
                            index * columnHeightPx
                }
                //limit bounds of scroll
                updateBounds(offsetRange.first, offsetRange.second)
            }
        }
    val coercedAnimatedOffset = animatedOffset.value % columnHeightPx
    val currentIndex =
        getItemIndexWithOffset(items, selectedItem, animatedOffset.value, columnHeightPx)

    Box(
        modifier = modifier
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    // sync scrolling with the drag
                    coroutineScope.launch {
                        animatedOffset.snapTo(animatedOffset.value + deltaY)
                    }
                },
                onDragStopped = {
                    // handle selected result after a drag
                    coroutineScope.launch {
                        val result = items.elementAt(
                            getItemIndexWithOffset(
                                items,
                                selectedItem,
                                animatedOffset.value,
                                columnHeightPx,
                            )
                        )
                        onSelectedChange(result)
                        animatedOffset.snapTo(0f)
                    }
                }
            ),
        contentAlignment = Alignment.Center,
        content = {
            Box(
                modifier = Modifier
                    .height(itemHeight * shownItemCount)
                    .offset {
                        // offset between any two items
                        IntOffset(x = 0, y = coercedAnimatedOffset.roundToInt())
                    }
            ) {
                val halfColumnCount = shownItemCount / 2
                for (index in (currentIndex - halfColumnCount)..(currentIndex + halfColumnCount)) {
                    if (index >= 0 && index < items.size) {
                        val item = items.elementAt(index)
                        if (index == currentIndex) {
                            ColumnItemLayout(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .height(itemHeight),
                                selectedFontSize = selectedFontSize,
                                defaultFontSize = defaultFontSize,
                                item = item,
                                isSelected = true,
                            )
                        } else {
                            val off = index - currentIndex
                            ColumnItemLayout(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .height(itemHeight)
                                    .offset(y = itemHeight * off),
                                selectedFontSize = selectedFontSize,
                                defaultFontSize = defaultFontSize,
                                item = item,
                                isSelected = false,
                            )
                        }
                    }
                }
            }
        }
    )
}



@Composable
private fun ColumnItemLayout(
    modifier: Modifier = Modifier,
    selectedFontSize: TextUnit,
    defaultFontSize: TextUnit,
    isSelected: Boolean,
    item: SelectorItem,
) {
    if (isSelected) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.id.toString(),
                style = TextStyle(fontSize = selectedFontSize, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = item.unit,
                style = TextStyle(fontSize = selectedFontSize, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "${item.value}${item.unit}",
                style = TextStyle(fontSize = defaultFontSize),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.35f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * find index of the new selected item, calculation with offset base on current selected item
 * @param selectedItem current selected item
 * @return index of the new selected
 */
private fun getItemIndexWithOffset(
    items: List<SelectorItem>,
    selectedItem: SelectorItem?,
    offset: Float,
    columnHeightPx: Float
): Int {
    if (selectedItem == null) return 0
    val index = items.indexOf(selectedItem) - (offset / columnHeightPx).toInt()
    return maxOf(0, minOf(index, items.count() - 1))
}