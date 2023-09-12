package com.ronjunevaldoz.todo.ui.screens.todo.add_edit

import com.ronjunevaldoz.todo.model.data.Priority

sealed class AddEditTaskEvent {
    data class OnTitleChange(val value: String) : AddEditTaskEvent()
    data class OnDescriptionChange(val value: String) : AddEditTaskEvent()
    data class OnDueDateTimeChange(val value: String) : AddEditTaskEvent()
    data class OnPriorityChange(val value: Priority) : AddEditTaskEvent()
    data object OnSaveTaskClick  : AddEditTaskEvent()
}