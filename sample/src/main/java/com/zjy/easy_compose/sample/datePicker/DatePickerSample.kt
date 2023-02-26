package com.zjy.easy_compose.sample.datePicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zjy.date_picker.DatePickerLayout
import com.zjy.easy_compose.ui.theme.EasyComposeTheme
import com.zjy.easy_compose.ui.widget.DATE_PICKER
import com.zjy.easy_compose.ui.widget.TopBar
import java.util.*

class DatePickerSample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            EasyComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    Column {
                        TopBar(
                            title = DATE_PICKER,
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
        val result = remember {
            mutableStateOf("")
        }
        val selectedTimeMillsState = remember {
            mutableStateOf(System.currentTimeMillis())
        }

        Column {
            Text(
                modifier = Modifier.padding(start = 20.dp, top = 16.dp),
                text = "Scroll to select your date:",
                fontSize = 26.sp,
            )
            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = "「Single Selector」",
                fontSize = 26.sp,
                style = TextStyle(fontWeight = FontWeight.Bold),
            )
            DatePickerLayout(
                modifier = Modifier.padding(top = 8.dp),
                selectedTimeMills = selectedTimeMillsState.value,
                onSelectedChanged = { year, month, day ->
                    selectedTimeMillsState.value = getTimeMills(year, month, day)
                    result.value = "$year $month $day"
                }
            )

            Text(
                modifier = Modifier.padding(start = 20.dp, top = 16.dp),
                text = "Result: " + result.value,
                fontSize = 26.sp,
            )
        }
    }

    private fun getTimeMills(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.timeInMillis
    }
}