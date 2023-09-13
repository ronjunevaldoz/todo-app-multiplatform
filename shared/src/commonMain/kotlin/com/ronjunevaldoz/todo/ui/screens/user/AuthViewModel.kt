package com.ronjunevaldoz.todo.ui.screens.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ronjunevaldoz.todo.model.data.UiEvent
import com.ronjunevaldoz.todo.model.data.User
import com.ronjunevaldoz.todo.model.repository.UserRepository
import com.ronjunevaldoz.todo.utils.Routes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class AuthViewModel(
    savedStateHolder: SavedStateHolder
) : ViewModel() {

    private val users = UserRepository.findUsers()

    var fieldUsername by mutableStateOf(
        savedStateHolder.consumeRestored("username") as String? ?: ""
    )
        private set
    var fieldPassword by mutableStateOf(
        savedStateHolder.consumeRestored("password") as String? ?: ""
    )

    var passwordHidden by mutableStateOf(
        savedStateHolder.consumeRestored("passwordHidden") as Boolean? ?: true
    )

    var currentUser by mutableStateOf<User?>(null)

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            UserRepository.add(User().apply {
                username = "admin"
                password = "password"
            })
        }
        viewModelScope.launch {
            users.asFlow().collectLatest { users ->
                val user = users.list.find { it.isAuthenticated }
                if (user != null) {
                    currentUser = user
                    sendUiEvent(UiEvent.Navigate(Routes.TASK_LIST))
                }
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnLoginClick -> {
                viewModelScope.launch {
                    val user = UserRepository.findUser(fieldUsername, fieldPassword).find()
                    if (user == null) {
                        sendUiEvent(
                            UiEvent.ShowSnackBar(
                                message = "Authentication failed: Wrong Username or Password"
                            )
                        )
                    } else {

                        sendUiEvent(UiEvent.Navigate(Routes.TASK_LIST))
                    }
                }
            }

            AuthEvent.OnLogoutClick -> {
                viewModelScope.launch {
                    currentUser?.id?.let {
                        UserRepository.update(it) {
                            isAuthenticated = false
                        }
                    }
                }
            }

            is AuthEvent.OnUsernameChange -> {
                fieldUsername = event.username
            }

            is AuthEvent.OnPasswordChange -> {
                fieldPassword = event.password
            }

            AuthEvent.OnTogglePassword -> {
                passwordHidden = !passwordHidden
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}