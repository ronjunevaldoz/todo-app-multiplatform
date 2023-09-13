package com.ronjunevaldoz.todo.ui.common.calendar

import kotlinx.datetime.LocalDate

/**
 * TODO move to model data
 */
data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isSelected: Boolean = false
)