package com.ronjunevaldoz.todo.ui.common.calendar

import com.ronjunevaldoz.todo.utils.today
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

/**
 * TODO move to view model
 */
fun generateCalendarGrid(year: Int, month: Int): List<CalendarDay> {
    val startDate = LocalDate(year, month, dayOfMonth = 1)
    val endDate = startDate + DatePeriod(months = 1) - DatePeriod(days = 1)
    val today = today

    val daysInMonth = mutableListOf<CalendarDay>()

    // Fill in days for the previous month
    val daysInPreviousMonth = startDate.dayOfWeek.isoDayNumber
    for (day in daysInPreviousMonth downTo 1) {
        val previousMonthDate = startDate - DatePeriod(days = day)
        daysInMonth.add(CalendarDay(previousMonthDate, false))
    }

    // Fill in days for the current month
    for (day in startDate.dayOfMonth..endDate.dayOfMonth) {
        val currentDate = LocalDate(year, month, day)
        daysInMonth.add(CalendarDay(currentDate, true, currentDate == today))
    }

    // Fill in days for the next month
    val daysInNextMonth = 7 - endDate.dayOfWeek.isoDayNumber - 1
    for (day in 1..daysInNextMonth) {
        val nextMonthDate = endDate + DatePeriod(days = day)
        daysInMonth.add(CalendarDay(nextMonthDate, false))
    }

    return daysInMonth
}