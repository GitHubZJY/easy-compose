package com.zjy.swiperefresh

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RefreshHeader(state: SwipeRefreshState, refreshTrigger: Dp) {
    val text: String = if (state.isRefreshing) {
        stringResource(id = R.string.swipe_refresh_header_loading)
    } else if (state.isSwipeInProgress) {
        if (state.indicatorOffset > refreshTrigger.value) {
            stringResource(id = R.string.swipe_refresh_header_release_refresh)
        } else {
            stringResource(id = R.string.swipe_refresh_header_pull_to_refresh)
        }
    } else {
        stringResource(id = R.string.swipe_refresh_header_pull_to_refresh)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .height(60.dp), contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .wrapContentSize()
                    .clipToBounds()
                    .padding(16.dp, 0.dp)
            )
        }
    }
}