package com.zjy.easy_compose.sample.smsCodeInput

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zjy.easy_compose.ui.theme.EasyComposeTheme
import com.zjy.easy_compose.ui.widget.SMS_CODE_INPUT
import com.zjy.easy_compose.ui.widget.TopBar
import com.zjy.sms_code_input.SmsCodeInput

class SmsCodeInputSample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            EasyComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    Column {
                        TopBar(
                            title = SMS_CODE_INPUT,
                            leftIcon = Icons.Default.ArrowBack,
                            onLeftClicked = {
                                onBackPressed()
                            })
                        PageContent()
                    }

                }
            }
        }
    }

    @Composable
    private fun PageContent() {
        val inputResult = remember {
            mutableStateOf("")
        }
        val inputCount = 4

        Column {
            Text(
                modifier = Modifier.padding(start = 20.dp, top = 60.dp),
                text = "Input your sms code",
                fontSize = 26.sp,
            )
            SmsCodeInput(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                itemModifier = Modifier
                    .size(60.dp)
                    .background(
                        Color(0xfff7f7f7),
                        RoundedCornerShape(8.dp)
                    ),
                inputCount = inputCount,
                inputCallback = {
                    if (it.length == inputCount) {
                        inputResult.value = it
                    } else {
                        inputResult.value = ""
                    }
                }
            )

            Text(
                modifier = Modifier.padding(start = 20.dp, top = 60.dp),
                text = "Result: " + inputResult.value,
                fontSize = 26.sp,
            )
        }
    }
}