package com.zjy.sms_code_input

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zjy.sms_code_input.SmsCodeConfig.DEFAULT_ITEM_CORNER_SIZE
import com.zjy.sms_code_input.SmsCodeConfig.DEFAULT_ITEM_SIZE
import com.zjy.sms_code_input.SmsCodeConfig.DEFAULT_ITEM_TEXT_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*********************************************************************
 * sms code input layout with one TextField and multiple Text
 *
 * @param modifier use to modifier the whole component
 * @param itemModifier use to modifier each item of code
 * @param textStyle setting text style for each item of code
 * @param inputCount setting counts of Texts
 * @param inputCallback will callback when input
 **********************************************************************/
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SmsCodeInput(
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier
        .size(DEFAULT_ITEM_SIZE)
        .background(
            Color.LightGray,
            RoundedCornerShape(DEFAULT_ITEM_CORNER_SIZE)
        ),
    textStyle: TextStyle = TextStyle(
        fontSize = DEFAULT_ITEM_TEXT_SIZE,
        textAlign = TextAlign.Start,
    ),
    inputCount: Int = SmsCodeConfig.INPUT_COUNT,
    inputCallback: (code: String) -> Unit
) {
    val (inputContent, onValueChange) = rememberSaveable { mutableStateOf("") }
    val textFieldValue = remember {
        mutableStateOf(
            TextFieldValue(
                text = inputContent,
                selection = TextRange(inputContent.length, inputContent.length)
            )
        )
    }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    TextField(
        value = textFieldValue.value,
        onValueChange = {
            val annotatedString = it.annotatedString
            if (annotatedString.length <= inputCount) {
                textFieldValue.value = it
                onValueChange(annotatedString.toString())
                if (annotatedString.isNotEmpty()) {
                    inputCallback(annotatedString.toString())
                }
            }
        },
        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        (0 until inputCount).map { index ->
            CodeItemBox(
                value = inputContent.getOrNull(index)?.toString() ?: "",
                isCursorVisible = inputContent.length == index,
                modifier = itemModifier,
                textStyle = textStyle,
                clickItem = {
                    focusRequester.requestFocus()
                    keyboard?.show()
                }
            )
        }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit, block = {
        scope.launch {
            delay(50)
            focusRequester.requestFocus()
        }
    })
}


/**
 * Box of each sms code item
 */
@Composable
private fun CodeItemBox(
    value: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
    isCursorVisible: Boolean = false,
    clickItem: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val (cursorSymbol, setCursorSymbol) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = cursorSymbol, isCursorVisible) {
        if (isCursorVisible) {
            scope.launch {
                //mock cursor twinkle
                delay(350)
                setCursorSymbol(if (cursorSymbol.isEmpty()) "|" else "")
            }
        }
    }

    Box(
        modifier = Modifier
            .then(modifier)
            .clickable {
                clickItem()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = if (isCursorVisible) cursorSymbol else value,
            color = if (isCursorVisible) {
                MaterialTheme.colors.primary
            } else {
                MaterialTheme.colors.onSurface
            },
            style = textStyle,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun SmsCodeInputPreview() {
    Surface(modifier = Modifier.padding(24.dp)) {
        SmsCodeInput(inputCount = 6) {

        }
    }
}