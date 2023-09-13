package com.ronjunevaldoz.todo.ui.screens.todo.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ronjunevaldoz.todo.model.data.UiEvent
import kotlinx.coroutines.flow.collectLatest


@Composable
fun TaskListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TaskListViewModel
) {
    val currentUser by viewModel.currentUser.asFlow().collectAsState(null)
    val tasks by viewModel.tasks.asFlow().collectAsState(null)
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                UiEvent.PopBackStack -> {}
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.onEvent(TaskListEvent.OnAddTaskClick)
                },
                icon = {
                    Icon(Icons.Filled.AddTask, "Add Todo.")
                },
                text = {
                    Text("Create Todo")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentUser?.obj?.username} - Todos",
                    style = MaterialTheme.typography.h6
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    viewModel.onEvent(TaskListEvent.OnLogoutClick)
                }) {
                    Icon(Icons.Default.Logout, contentDescription = "Logout")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                tasks?.let { result ->
                    items(result.list.sortedBy { it.priority.value }) { task ->
                        TaskItem(
                            task = task,
                            onEvent = viewModel::onEvent
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}