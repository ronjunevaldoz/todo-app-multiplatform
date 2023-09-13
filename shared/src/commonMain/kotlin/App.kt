import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ronjunevaldoz.todo.database.realm
import com.ronjunevaldoz.todo.ui.screens.todo.add_edit.AddEditScreen
import com.ronjunevaldoz.todo.ui.screens.todo.add_edit.AddEditTaskViewModel
import com.ronjunevaldoz.todo.ui.screens.todo.list.TaskListScreen
import com.ronjunevaldoz.todo.ui.screens.todo.list.TaskListViewModel
import com.ronjunevaldoz.todo.ui.screens.user.AuthViewModel
import com.ronjunevaldoz.todo.ui.screens.user.LoginScreen
import com.ronjunevaldoz.todo.utils.Routes
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.InitialRealm
import io.realm.kotlin.notifications.RealmChange
import io.realm.kotlin.notifications.UpdatedRealm
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun App() {
    val navigator = rememberNavigator()
    LaunchedEffect(Unit) {
        realm.asFlow().collect { realmChange: RealmChange<Realm> ->
            when (realmChange) {
                is InitialRealm<*> -> println("Initial Realm")
                is UpdatedRealm<*> -> println("Realm updated")
            }
        }
    }
    MaterialTheme {
        NavHost(
            navigator = navigator,
            initialRoute = Routes.LOGIN_SCREEN
        ) {
            scene(route = Routes.LOGIN_SCREEN) {
                val authViewModel = viewModel(
                    keys = listOf(
                        "username",
                        "password",
                        "passwordHidden",
                    )
                ) {
                    AuthViewModel(it)
                }
                LoginScreen(onNavigate = {
                    navigator.popBackStack()
                    navigator.navigate(
                        it.route,
                        NavOptions(popUpTo = PopUpTo(it.route, true))
                    )
                }, authViewModel)
            }
            scene(route = "${Routes.TASK_LIST}/{userId}") { backStackEntry ->
                val userId =
                    backStackEntry.path<String>("userId") ?: throw Exception("User id invalid")

                println("User: $userId")
                val taskListViewModel = viewModel {
                    TaskListViewModel(userId)
                }
                TaskListScreen(
                    onNavigate = {
                        if (it.clear) {
                            navigator.popBackStack()
                            navigator.navigate(
                                it.route,
                                NavOptions(popUpTo = PopUpTo(it.route, true))
                            )
                        } else {
                            navigator.navigate(it.route, NavOptions(true))
                        }
                    },
                    viewModel = taskListViewModel
                )
            }
            scene(route = "${Routes.ADD_EDIT_TASK}/{userId}") { backStackEntry ->
                val userId = backStackEntry.path<String>("userId")
                val addEditTaskViewModel = viewModel(
                    keys = listOf(
                        "title",
                        "description",
                        "timestamp",
                    )
                ) { savedState ->
                    AddEditTaskViewModel(userId, null, savedState)
                }

                AddEditScreen(
                    onPopBackStack = {
                        navigator.popBackStack()
                    },
                    viewModel = addEditTaskViewModel
                )
            }
            scene(route = "${Routes.ADD_EDIT_TASK}/{userId}/{taskId}") { backStackEntry ->
                val userId = backStackEntry.path<String>("userId")
                val taskId = backStackEntry.path<String>("taskId")
                val addEditTaskViewModel = viewModel(
                    keys = listOf(
                        "title",
                        "description",
                        "timestamp",
                        "priority",
                    )
                ) { savedState ->
                    AddEditTaskViewModel(userId, taskId, savedState)
                }

                AddEditScreen(
                    onPopBackStack = {
                        navigator.popBackStack()
                    },
                    viewModel = addEditTaskViewModel
                )
            }
        }
    }
}

expect fun getPlatformName(): String