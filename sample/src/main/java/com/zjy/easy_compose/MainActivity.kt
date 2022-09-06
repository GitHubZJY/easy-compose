package com.zjy.easy_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zjy.easy_compose.ui.theme.EasyComposeTheme
import com.zjy.easy_compose.ui.theme.Purple700

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
        ComponentItemModel(SWIPE_REFRESH, "A layout which implements the swipe-to-refresh pattern, allowing the user to refresh content via " +
                "a vertical swipe gesture"),
        ComponentItemModel(
            SMS_CODE_INPUT, "A layout implements the sms codes input effects, layout with one TextField and multiple Text")
    )

    Column {
        TopBar(title = stringResource(id = R.string.app_name))
        LazyColumn(
            modifier = Modifier.weight(1f),
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

@Composable
private fun TopBar(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .background(Purple700)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = CircleShape),
                painter = painterResource(id = R.mipmap.ic_launcher_round),
                contentDescription = "logo",
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    EasyComposeTheme {
        PageContent()
    }
}