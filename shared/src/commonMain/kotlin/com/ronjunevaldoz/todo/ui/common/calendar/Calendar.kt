package com.ronjunevaldoz.todo.ui.common.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ronjunevaldoz.todo.utils.calendarLabel
import com.ronjunevaldoz.todo.utils.calendarTitle
import com.ronjunevaldoz.todo.utils.calendarWeekOrder
import com.ronjunevaldoz.todo.utils.today
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Month
import kotlinx.datetime.minus
import kotlinx.datetime.plus

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    onSelectDay: (
        CalendarDay
    ) -> Unit
) {
    var currentDate by remember { mutableStateOf(today) }
    val grids = generateCalendarGrid(currentDate.year, currentDate.monthNumber)
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = currentDate.calendarTitle
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = today.calendarLabel,
            )
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = {
                    // check first if prev is allowed
                    currentDate -= if (currentDate.month == Month.JANUARY) {
                        // move to last year if current month is january
                        DatePeriod(months = 1, years = 0)
                    } else {
                        DatePeriod(months = 1)
                    }
                }) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Prev")
            }
            IconButton(
                onClick = {
                    currentDate += if (currentDate.month == Month.DECEMBER) {
                        // move to next year if current month is december
                        DatePeriod(months = 1, years = 0)
                    } else {
                        // otherwise add only 1 month
                        DatePeriod(months = 1)
                    }
                }) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Next")
            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(
                start = 12.dp,
//                top = 16.dp,
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

        Text(
            text = "Custom Calendar by Ron June Valdoz",
            fontSize = 10.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(8.dp))
    }
}

