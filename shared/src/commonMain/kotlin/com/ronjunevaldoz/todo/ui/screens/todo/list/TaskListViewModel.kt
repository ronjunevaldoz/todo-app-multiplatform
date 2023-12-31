package com.ronjunevaldoz.todo.ui.screens.todo.list

import com.ronjunevaldoz.todo.model.data.UiEvent
import com.ronjunevaldoz.todo.model.repository.TodoRepository
import com.ronjunevaldoz.todo.model.repository.UserRepository
import com.ronjunevaldoz.todo.utils.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class TaskListViewModel(private val userId: String) : ViewModel() {

    val currentUser = UserRepository.findUser(userId)
    val tasks = TodoRepository.findTodos(userId)

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.OnTaskClick -> {
                sendUiEvent(UiEvent.Navigate("${Routes.ADD_EDIT_TASK}/$userId/${event.task.id}"))
            }

            TaskListEvent.OnAddTaskClick -> {
                sendUiEvent(UiEvent.Navigate("${Routes.ADD_EDIT_TASK}/$userId"))
            }

            is TaskListEvent.OnStatusCompleted -> {
                viewModelScope.launch {
                    TodoRepository.update(event.task.id) {
                        completed = event.completed
                    }
                    sendUiEvent(UiEvent.ShowSnackBar("Todo ${event.task.title} status changed."))
                }
            }

            is TaskListEvent.DeleteTask -> {
                viewModelScope.launch {
                    TodoRepository.delete(event.task)
                    sendUiEvent(UiEvent.ShowSnackBar("Todo ${event.task.title} has been deleted."))
                }
            }

            is TaskListEvent.OnPriorityChange -> {
                viewModelScope.launch {
                    TodoRepository.update(event.task.id) {
                        priority = event.priority
                    }
                    sendUiEvent(UiEvent.ShowSnackBar("Todo ${event.task.title} priority changed."))
                }
            }

            TaskListEvent.OnLogoutClick -> {
                viewModelScope.launch {
                    UserRepository.logout()
                    sendUiEvent(UiEvent.Navigate(Routes.LOGIN_SCREEN, true))
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