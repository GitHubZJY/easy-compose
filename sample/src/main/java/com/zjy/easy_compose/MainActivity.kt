package com.zjy.easy_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjy.easy_compose.ui.theme.EasyComposeTheme
import com.zjy.easy_compose.ui.widget.ComponentItem
import com.zjy.easy_compose.ui.widget.ComponentItemModel
import com.zjy.easy_compose.ui.widget.SMS_CODE_INPUT
import com.zjy.easy_compose.ui.widget.SWIPE_REFRESH
import com.zjy.easy_compose.ui.widget.TopBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    PageContent()
                }
            }
        }
    }
}

@Composable
private fun PageContent() {
    val components = listOf(
        ComponentItemModel(
            SWIPE_REFRESH,
            "A layout which implements the swipe-to-refresh pattern, allowing the user to refresh content via " +
                    "a vertical swipe gesture"
        ),
        ComponentItemModel(
            SMS_CODE_INPUT,
            "A layout implements the sms codes input effects, layout with one TextField and multiple Text"
        )
    )

    Column {
        TopBar(
            title = stringResource(id = R.string.app_name),
            leftImage = R.mipmap.ic_launcher_round,
        )
        LazyColumn(
            modifier = Modifier.weight(1f).padding(top = 8.dp),
        ) {
            items(
                count = components.size,
                itemContent = { index ->
                    ComponentItem(components[index])
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    EasyComposeTheme {
        PageContent()
    }
}