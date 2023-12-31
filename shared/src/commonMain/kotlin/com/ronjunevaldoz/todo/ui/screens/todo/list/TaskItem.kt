package com.ronjunevaldoz.todo.ui.screens.todo.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ronjunevaldoz.todo.model.data.Todo
import com.ronjunevaldoz.todo.model.data.color
import com.ronjunevaldoz.todo.ui.common.PriorityPicker
import com.ronjunevaldoz.todo.utils.calendarLabel
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
            .padding(horizontal = 8.dp, vertical = 8.dp),
        elevation = 4.dp
    ) {

        Box(contentAlignment = Alignment.CenterEnd) {
            Column {
                IconButton(
                    onClick = {
                        showPicker.value = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = "Priority",
                        modifier = Modifier.border(1.dp, task.priority.color, CircleShape),
                        tint = task.priority.color.copy(alpha = 0.5f),
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


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    onEvent(
                        TaskListEvent.OnStatusCompleted(
                            task = task,
                            completed = !task.completed
                        )
                    )
                }) {
                    val taskIcon =
                        if (task.completed) Icons.Default.CheckBox
                        else Icons.Default.CheckBoxOutlineBlank
                    Icon(
                        imageVector = taskIcon,
                        contentDescription = "Task Icon"
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.status)
                Text(text = task.title)
                Text(text = task.description, color = Color.LightGray)
                Text(
                    text = task.dueDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).date.calendarLabel,
                    color = Color.LightGray
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