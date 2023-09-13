package com.ronjunevaldoz.todo.ui.screens.todo.add_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ronjunevaldoz.todo.model.data.Priority
import com.ronjunevaldoz.todo.model.data.Todo
import com.ronjunevaldoz.todo.model.data.UiEvent
import com.ronjunevaldoz.todo.model.repository.TodoRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.toInstant
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class AddEditTaskViewModel(
    private val taskId: String?,
    savedStateHolder: SavedStateHolder
) : ViewModel() {

    private val task by lazy {
        taskId?.let { TodoRepository.findTodo(it).find() }
    }
    var fieldTitle by mutableStateOf(
        savedStateHolder.consumeRestored("title") as String? ?: task?.title ?: ""
    )
        private set
    var fieldDescription by mutableStateOf(
        savedStateHolder.consumeRestored("description") as String? ?: task?.description ?: ""
    )
        private set
    var fieldTimestamp by mutableStateOf(
        savedStateHolder.consumeRestored("timestamp") as String? ?: task?.dueDateTime?.toString()
        ?: ""
    )
        private set
    var fieldPriority by mutableStateOf(
        savedStateHolder.consumeRestored("priority") as Int? ?: task?.priority?.value
        ?: Priority.LOW.value
    )
        private set

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        savedStateHolder.registerProvider("title") {
            fieldTitle
        }
        savedStateHolder.registerProvider("description") {
            fieldDescription
        }
        savedStateHolder.registerProvider("timestamp") {
            fieldTimestamp
        }
        savedStateHolder.registerProvider("priority") {
            fieldPriority
        }
    }

    fun onEvent(event: AddEditTaskEvent) {
        when (event) {
            is AddEditTaskEvent.OnDescriptionChange -> {
                fieldDescription = event.value
            }

            is AddEditTaskEvent.OnDueDateTimeChange -> {
                fieldTimestamp = event.value
            }

            is AddEditTaskEvent.OnTitleChange -> {
                fieldTitle = event.value
            }

            is AddEditTaskEvent.OnPriorityChange -> {
                fieldPriority = event.value.value
            }

            AddEditTaskEvent.OnSaveTaskClick -> {
                if (fieldTitle.isEmpty()) {
                    sendUiEvent(UiEvent.ShowSnackBar("Todo name cannot be empty"))
                    return
                }
                viewModelScope.launch {
                    task?.let { it ->
                        // update
                        TodoRepository.update(it.id) {
                            title = fieldTitle
                            description = fieldDescription
                            dueDateTime = fieldTimestamp.toInstant()
                            priority =
                                Priority.entries.find { it.value == fieldPriority } ?: Priority.LOW
                        }
                    } ?: run {
                        // add
                        TodoRepository.add(Todo().apply {
                            title = fieldTitle
                            description = fieldDescription
                            dueDateTime = fieldTimestamp.toInstant()
                            priority =
                                Priority.entries.find { it.value == fieldPriority } ?: Priority.LOW
                        })
                    }
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}