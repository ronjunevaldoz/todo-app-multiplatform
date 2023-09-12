package com.ronjunevaldoz.todo.ui.common.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronjunevaldoz.todo.utils.calendarLabel
import com.ronjunevaldoz.todo.utils.calendarWeekOrder
import com.ronjunevaldoz.todo.utils.today

@Composable
fun Calendar(modifier: Modifier = Modifier, onSelectDay: (CalendarDay) -> Unit) {
    val grids = generateCalendarGrid(today.year, today.monthNumber)
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = today.calendarLabel,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        LazyVerticalGrid(columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(calendarWeekOrder) {
                    CalendarHeader(it)
                }
                items(grids) {
                    CalendarDayView(it, onSelectDay)
                }
            })
    }
}

