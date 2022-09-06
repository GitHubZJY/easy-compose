package com.zjy.easy_compose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zjy.easy_compose.sample.smsCodeInput.SmsCodeInputSample
import com.zjy.easy_compose.sample.swiperefresh.SwipeRefreshSample

internal data class ComponentItemModel(
    val name: String,
    val description: String,
)

const val SWIPE_REFRESH = "swipe-refresh"
const val SMS_CODE_INPUT = "sms-code-input"

@Composable
internal fun ComponentItem(item: ComponentItemModel) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp),
            )
            .clickable {
                handleClick(
                    context = context,
                    name = item.name,
                )
            },
        elevation = 12.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = item.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

private fun handleClick(context: Context, name: String) {
    val intent = when (name) {
        SWIPE_REFRESH -> {
            Intent(context, SwipeRefreshSample::class.java)
        }
        SMS_CODE_INPUT -> {
            Intent(context, SmsCodeInputSample::class.java)
        }
        else -> {
            null
        }
    }
    intent?.let {
        context.startActivity(it)
    }
}