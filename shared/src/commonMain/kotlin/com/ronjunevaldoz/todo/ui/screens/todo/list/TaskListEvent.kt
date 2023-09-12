package com.ronjunevaldoz.todo.ui.screens.todo.list

import com.ronjunevaldoz.todo.model.data.Priority
import com.ronjunevaldoz.todo.model.data.Todo

sealed class TaskListEvent {
    data class OnTaskClick(val task: Todo) : TaskListEvent()
    data object OnAddTaskClick : TaskListEvent()
    data class OnPriorityChange(val task: Todo, val priority: Priority) : TaskListEvent()
    data class OnDoneChange(val task: Todo, val isDone: Boolean) : TaskListEvent()
    data class DeleteTask(val task: Todo) : TaskListEvent()
}