package com.zjy.swiperefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


/**
 * A layout which implements the swipe-to-refresh pattern, allowing the user to refresh content via
 * a vertical swipe gesture.
 *
 * This layout requires its content to be scrollable so that it receives vertical swipe events.
 * The scrollable content does not need to be a direct descendant though. Layouts such as
 * [androidx.compose.foundation.lazy.LazyColumn] are automatically scrollable, but others such as
 * [androidx.compose.foundation.layout.Column] require you to provide the
 * [androidx.compose.foundation.verticalScroll] modifier to that content.
 *
 * Apps should provide a [onRefresh] block to be notified each time a swipe to refresh gesture
 * is completed. That block is responsible for updating the [isRefreshing] as appropriately,
 * typically by setting [SwipeRefreshState.isRefreshing] to `true` once a 'refresh' has been
 * started. Once a refresh has completed, the app should then set
 * [SwipeRefreshState.isRefreshing] to `false`.
 *
 * If an app wishes to show the progress animation outside of a swipe gesture, it can
 * set [SwipeRefreshState.isRefreshing] as required.
 *
 * This layout does not clip any of it's contents, including the indicator. If clipping
 * is required, apps can provide the [androidx.compose.ui.draw.clipToBounds] modifier.
 *
 * @sample com.zjy.easy_compose.sample.swiperefresh.SwipeRefreshSample
 *
 * @param isRefreshing the boolean object to be used to control the [SwipeRefreshLayout] refresh.
 * @param onRefresh Lambda which is invoked when a swipe to refresh gesture is completed.
 * @param modifier the modifier to apply to this layout.
 * @param swipeEnabled Whether the the layout should react to swipe gestures or not.
 * @param refreshTriggerDistance The minimum swipe distance which would trigger a refresh.
 * @param indicatorPadding Content padding for the indicator, to inset the indicator in if required.
 * @param indicator the indicator that represents the current state. By default this
 * will use a [RefreshHeader].
 * @param clipIndicatorToPadding Whether to clip the indicator to [indicatorPadding]. If false is
 * provided the indicator will be clipped to the [content] bounds. Defaults to true.
 * @param content The content containing a scroll composable.
 */
@Composable
fun SwipeRefreshLayout(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    swipeEnabled: Boolean = true,
    refreshTriggerDistance: Dp = 80.dp,
    indicatorPadding: PaddingValues = PaddingValues(0.dp),
    indicator: @Composable (state: SwipeRefreshState, refreshTrigger: Dp) -> Unit = { s, trigger ->
        RefreshHeader(s, trigger)
    },
    clipIndicatorToPadding: Boolean = false,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val state = rememberSwipeRefreshState(isRefreshing)
    val updatedOnRefresh = rememberUpdatedState(onRefresh)
    var indicatorHeight by remember {
        mutableStateOf(0)
    }

    // Our LaunchedEffect, which animates the indicator to its resting position
    LaunchedEffect(state.isSwipeInProgress, state.isRefreshing) {
        if (!state.isSwipeInProgress) {
            if (state.isRefreshing) {
                state.animateOffsetTo(indicatorHeight*1.0f)
            } else {
                state.animateOffsetTo(0f)
            }
        }
    }

    val refreshTriggerPx = with(LocalDensity.current) { refreshTriggerDistance.toPx() }


    // Our nested scroll connection, which updates our state.
    val nestedScrollConnection = remember(state, coroutineScope) {
        SwipeRefreshNestedScrollConnection(state, coroutineScope) {
            // On refresh, re-dispatch to the update onRefresh block
            updatedOnRefresh.value.invoke()
        }
    }.apply {
        this.enabled = swipeEnabled
        this.refreshTrigger = refreshTriggerPx
    }

    Box(
        modifier
            .nestedScroll(connection = nestedScrollConnection)
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned {
                indicatorHeight = it.size.height
            }
            .padding(indicatorPadding)
            .let { if (clipIndicatorToPadding) it.clipToBounds() else it }
            .offset {
                IntOffset(0, - indicatorHeight + state.indicatorOffset.toInt())
            }
            .zIndex(0f)
        ) {
            indicator(state, refreshTriggerDistance)
        }
        Box(modifier = Modifier.offset {
            IntOffset(0, state.indicatorOffset.toInt())
        }) {
            content()
        }
    }
}
