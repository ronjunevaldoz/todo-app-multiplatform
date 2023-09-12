package com.ronjunevaldoz.todo.ui.common.calendar

import kotlinx.datetime.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isSelected: Boolean = false
)