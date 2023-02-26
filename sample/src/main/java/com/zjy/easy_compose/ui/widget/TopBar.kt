package com.zjy.easy_compose.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zjy.easy_compose.ui.theme.Purple700

@Composable
internal fun TopBar(
    title: String,
    @DrawableRes leftImage: Int? = null,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    onLeftClicked: (() -> Unit)? = null,
    onRightClicked: (() -> Unit)? = null,
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
            if (leftImage != null) {
                Image(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(shape = CircleShape)
                        .clickable { onLeftClicked?.invoke() },
                    painter = painterResource(id = leftImage),
                    contentDescription = "logo",
                )
            }
            if (leftIcon != null) {
                Icon(
                    modifier = Modifier.clickable { onLeftClicked?.invoke() },
                    imageVector = leftIcon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = AnnotatedString(title) ,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        if (rightIcon != null) {
            Icon(
                modifier = Modifier.clickable {
                    onRightClicked?.invoke()
                },
                imageVector = rightIcon,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}