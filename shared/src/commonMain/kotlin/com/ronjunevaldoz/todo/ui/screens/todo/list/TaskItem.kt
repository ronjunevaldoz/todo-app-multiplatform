package com.ronjunevaldoz.todo.ui.screens.todo.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ronjunevaldoz.todo.model.data.Todo
import com.ronjunevaldoz.todo.model.data.color
import com.ronjunevaldoz.todo.ui.common.PriorityPicker
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Todo,
    onEvent: (TaskListEvent) -> Unit
) {
    val showPicker = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onEvent(TaskListEvent.OnTaskClick(task))
                },
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Default.Task,
                        contentDescription = "Task Icon"
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Priority - ${task.priority.name}",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .border(1.dp, task.priority.color, RoundedCornerShape(6.dp))
                        .background(task.priority.color.copy(0.5f), RoundedCornerShape(6.dp))
                        .padding(3.dp)
                        .clickable {
                            showPicker.value = true
                        }
                )
                Text(text = task.title)
                Text(text = task.description, color = Color.LightGray)
                Text(
                    text = task.dueDateTime.toLocalDateTime(TimeZone.UTC).toString(),
                    color = Color.LightGray
                )
            }
            IconButton(onClick = {
                onEvent(TaskListEvent.DeleteTask(task))
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
    if (showPicker.value)
        PriorityPicker(
            todo = task,
            onDismiss = {
                showPicker.value = false
            }, onEvent
        )
}