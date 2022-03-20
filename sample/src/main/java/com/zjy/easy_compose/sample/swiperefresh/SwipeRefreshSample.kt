package com.zjy.easy_compose.sample.swiperefresh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zjy.easy_compose.ui.theme.EasycomposeTheme
import com.zjy.swiperefresh.SwipeRefreshLayout

class SwipeRefreshSample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SwipeRefreshViewModel = viewModel()
            val isRefreshing = viewModel.showPullRefresh.observeAsState(false).value

            EasycomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SwipeRefreshLayout(isRefreshing = isRefreshing, onRefresh = {
                        viewModel.mockReq()
                    }, content = {
                        ListContent()
                    })
                }
            }
        }
    }

    @Composable
    private fun ListContent() {
        val list = remember { mutableListOf<String>() }
        for (i in 0..30) {
            list.add("row$i")
        }
        LazyColumn {
            items(list.size) { index ->
                Box(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(list[index])
                }
            }
        }
    }
}