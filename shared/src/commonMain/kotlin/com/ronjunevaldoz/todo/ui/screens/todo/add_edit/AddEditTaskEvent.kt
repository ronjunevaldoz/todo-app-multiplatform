package com.ronjunevaldoz.todo.ui.screens.todo.add_edit

sealed class AddEditTaskEvent {
    data class OnTitleChange(val value: String) : AddEditTaskEvent()
    data class OnDescriptionChange(val value: String) : AddEditTaskEvent()
    data class OnTimestampChange(val value: String) : AddEditTaskEvent()
    data object OnSaveTaskClick  : AddEditTaskEvent()
}