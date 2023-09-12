package com.ronjunevaldoz.todo.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ronjunevaldoz.todo.model.data.Priority
import com.ronjunevaldoz.todo.model.data.Todo
import com.ronjunevaldoz.todo.model.data.color
import com.ronjunevaldoz.todo.ui.screens.todo.list.TaskListEvent

@Composable
fun PriorityPicker(todo: Todo, onDismiss: () -> Unit, onEvent: (TaskListEvent) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 40.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(Priority.entries) { priority ->
                    Row {
                        Icon(Icons.Default.Circle, contentDescription = null, tint = priority.color)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = priority.name,
                            color = priority.color,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    onEvent(TaskListEvent.OnPriorityChange(todo, priority))
                                    onDismiss()
                                }
                        )
                    }
                }
            }
        }
    }
}