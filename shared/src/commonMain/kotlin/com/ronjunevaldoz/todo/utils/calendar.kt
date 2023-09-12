package com.ronjunevaldoz.todo.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn


val today: LocalDate
    get() {
        return Clock.System.todayIn(TimeZone.currentSystemDefault())
    }
val calendarWeekOrder = listOf(
    DayOfWeek.SUNDAY,
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
)

val DayOfWeek.short: String
    get() = when (this) {
        DayOfWeek.SUNDAY -> "Sun"
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
        else -> TODO("not implemented")
    }

val LocalDate.calendarLabel: String
    get() = "${dayOfWeek.short}, ${monthNumberToShortName(monthNumber)} ${dayOfMonth}, ${today.year}"

fun monthNumberToShortName(monthNumber: Int): String {
    val months =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    if (monthNumber in 1..12) {
        return months[monthNumber - 1]
    } else {
        throw IllegalArgumentException("Invalid month number: $monthNumber")
    }
}

