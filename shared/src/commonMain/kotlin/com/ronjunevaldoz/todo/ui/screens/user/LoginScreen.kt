package com.ronjunevaldoz.todo.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ronjunevaldoz.todo.model.data.UiEvent
import com.ronjunevaldoz.todo.ui.common.TextInput
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: AuthViewModel
) {

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
//            ExtendedFloatingActionButton(
//                onClick = {
//                    viewModel.onEvent(TaskListEvent.OnAddTaskClick)
//                },
//                icon = {
//                    Icon(Icons.Filled.AddTask, "Add Todo.")
//                },
//                text = {
//                    Text("Create Todo")
//                }
//            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Todo App Authentication", fontSize = 20.sp)
            Image(painterResource("ic_launcher-web.png"), contentDescription = null)
            Spacer(modifier = Modifier.height(8.dp))
            TextInput(
                value = viewModel.fieldUsername,
                onValueChange = {
                    viewModel.onEvent(AuthEvent.OnUsernameChange(it))
                },
                placeholder = "Username",
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextInput(
                value = viewModel.fieldPassword,
                onValueChange = {
                    viewModel.onEvent(AuthEvent.OnPasswordChange(it))
                },
                placeholder = "Password",
                inputType = if (viewModel.passwordHidden) KeyboardType.Text else KeyboardType.Password,
                leadingIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(AuthEvent.OnTogglePassword)
                    }) {
                        Icon(
                            if (viewModel.passwordHidden) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            }, contentDescription = "Password"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // login
                    viewModel.onEvent(AuthEvent.OnLoginClick)
                }
            ) {
                Text(text = "Login")
            }
        }
    }
}