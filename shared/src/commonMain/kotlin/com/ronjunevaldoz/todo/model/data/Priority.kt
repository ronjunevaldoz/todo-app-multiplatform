package com.ronjunevaldoz.todo.model.data

import androidx.compose.ui.graphics.Color

enum class Priority(val value: Int) {
    HIGH(0),
    MEDIUM(1),
    LOW(2),
    UNKNOWN(3),
}

val Priority.color: Color
    get() = when (this) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> Color.Green
        Priority.LOW -> Color.Blue
        Priority.UNKNOWN -> Color.LightGray
    }