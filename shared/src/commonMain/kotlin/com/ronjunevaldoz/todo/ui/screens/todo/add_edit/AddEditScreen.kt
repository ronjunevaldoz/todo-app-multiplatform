package com.ronjunevaldoz.todo.ui.screens.todo.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronjunevaldoz.todo.model.data.UiEvent
import com.ronjunevaldoz.todo.ui.common.TextInput
import com.ronjunevaldoz.todo.ui.common.calendar.CalendarPicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@Composable
fun AddEditScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditTaskViewModel
) {
    val showCalendar = remember { mutableStateOf(false) }
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(true) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                UiEvent.PopBackStack -> onPopBackStack()
                is UiEvent.Navigate -> Unit
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message, actionLabel = event.action
                    )
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize().padding(16.dp),
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                viewModel.onEvent(AddEditTaskEvent.OnSaveTaskClick)
            }, icon = {
                Icon(Icons.Filled.Save, "Save.")
            }, text = {
                Text("Save")
            })
        }) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TextInput(
                value = viewModel.fieldTitle,
                onValueChange = {
                    viewModel.onEvent(AddEditTaskEvent.OnTitleChange(it))
                },
                placeholder = "Title",
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextInput(
                value = viewModel.fieldDescription,
                onValueChange = {
                    viewModel.onEvent(AddEditTaskEvent.OnDescriptionChange(it))
                },
                placeholder = "Description",
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextInput(
                enabled = false,
                value = viewModel.fieldTimestamp ?: "",
                onValueChange = {

                },
                leadingIcon = {
                    IconButton(onClick = {
                        showCalendar.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar"
                        )
                    }

                },
                placeholder = "Due Date",
            )
        }
    }

    if (showCalendar.value) {
        CalendarPicker(
            onDismiss = {
                showCalendar.value = false
            },
        ) { calendarDay ->

            val now = Clock.System.now()
            val dateTimeNow = now.toLocalDateTime(TimeZone.currentSystemDefault())
            val selected = LocalDateTime(
                calendarDay.date,
                dateTimeNow.time
            ).toInstant(TimeZone.currentSystemDefault())
            viewModel.onEvent(AddEditTaskEvent.OnDueDateTimeChange(selected.toString()))
            showCalendar.value = false
        }
    }
}