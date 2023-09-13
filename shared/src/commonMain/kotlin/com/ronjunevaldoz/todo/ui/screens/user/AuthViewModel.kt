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
import kotlinx.coroutines.launch
import moe.tlaster.precompose.stateholder.SavedStateHolder
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class AuthViewModel(
    savedStateHolder: SavedStateHolder
) : ViewModel() {

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

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            // create dummy users
            if (UserRepository.findUser("admin", "admin").find() == null) {
                createDummyUsers("admin", "admin")
            }
            if (UserRepository.findUser("ron", "ron").find() == null) {
                createDummyUsers("ron", "ron")
            }
        }
    }

    private fun createDummyUsers(name: String, pass: String) {
        viewModelScope.launch {
            UserRepository.add(User().apply {
                username = name
                password = pass
            })
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnLoginClick -> {
                val user = UserRepository.findUser(fieldUsername, fieldPassword).find()
                if (user == null) {
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            message = "Authentication failed: Wrong Username or Password"
                        )
                    )
                } else {
                    fieldUsername = ""
                    fieldPassword = ""
                    viewModelScope.launch {
                        UserRepository.logout()
                        UserRepository.update(user.id) {
                            isAuthenticated = true
                        }
                        sendUiEvent(
                            UiEvent.ShowSnackBar(
                                message = "Authentication successful: Welcome, ${user.username}"
                            )
                        )
                        sendUiEvent(UiEvent.Navigate("${Routes.TASK_LIST}/${user.id}"))
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