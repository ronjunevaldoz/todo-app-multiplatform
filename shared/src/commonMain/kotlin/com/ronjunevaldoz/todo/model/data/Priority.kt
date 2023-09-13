package com.ronjunevaldoz.todo.model.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class Priority(val value: Int) {
    HIGH(0),
    MEDIUM(1),
    LOW(2),
}

val Priority.color: Color
    get() = when (this) {
        Priority.HIGH -> Color.Red
        Priority.MEDIUM -> Color.Green
        Priority.LOW -> Color.Blue
    }

val Priority.icon: ImageVector
    get() = when (this) {
        Priority.HIGH -> Icons.Default.PriorityHigh
        Priority.MEDIUM -> Icons.Default.DensityMedium
        Priority.LOW -> Icons.Default.LowPriority
    }