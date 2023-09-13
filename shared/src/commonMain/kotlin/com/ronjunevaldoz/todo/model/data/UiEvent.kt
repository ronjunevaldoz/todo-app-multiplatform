package com.ronjunevaldoz.todo.model.data

import moe.tlaster.precompose.navigation.PopUpTo

sealed class UiEvent {
    data object PopBackStack: UiEvent()
    data class Navigate(val route: String, val clear: Boolean = false): UiEvent()
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent()
}