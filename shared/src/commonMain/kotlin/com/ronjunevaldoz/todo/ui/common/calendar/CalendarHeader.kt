package com.ronjunevaldoz.todo.ui.common.calendar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ronjunevaldoz.todo.utils.short
import kotlinx.datetime.DayOfWeek

@Composable
fun CalendarHeader(dayOfWeek: DayOfWeek) {
    Card(
        backgroundColor = Color.White,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = 8.dp,
    ) {
        Text(
            text = dayOfWeek.short,
            fontSize = 10.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}