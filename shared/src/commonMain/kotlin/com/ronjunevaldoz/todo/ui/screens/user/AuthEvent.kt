package com.ronjunevaldoz.todo.ui.screens.user

sealed class AuthEvent {
    data class OnUsernameChange(val username: String) : AuthEvent()
    data class OnPasswordChange(val password: String) : AuthEvent()
    data object OnTogglePassword : AuthEvent()
    data object OnLoginClick : AuthEvent()
}