import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ronjunevaldoz.todo.database.realm
import com.ronjunevaldoz.todo.ui.screens.todo.add_edit.AddEditScreen
import com.ronjunevaldoz.todo.ui.screens.todo.add_edit.AddEditTaskViewModel
import com.ronjunevaldoz.todo.ui.screens.todo.list.TaskListScreen
import com.ronjunevaldoz.todo.ui.screens.todo.list.TaskListViewModel
import com.ronjunevaldoz.todo.utils.Routes
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.InitialRealm
import io.realm.kotlin.notifications.RealmChange
import io.realm.kotlin.notifications.UpdatedRealm
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
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
            initialRoute = "/screens/${Routes.TASK_LIST}"
        ) {

            scene(route = "/screens/${Routes.TASK_LIST}") {
                val taskListViewModel = viewModel {
                    TaskListViewModel()
                }
                TaskListScreen(
                    onNavigate = {
                        navigator.navigate(it.route, NavOptions(true))
                    },
                    viewModel = taskListViewModel
                )
            }
            scene(route = "/${Routes.ADD_EDIT_TASK}/") {
                val addEditTaskViewModel = viewModel(
                    keys = listOf(
                        "title",
                        "description",
                        "timestamp",
                    )
                ) { savedState ->
                    AddEditTaskViewModel(null, savedState)
                }

                AddEditScreen(
                    onPopBackStack = {
                        navigator.popBackStack()
                    },
                    viewModel = addEditTaskViewModel
                )
            }
            scene(route = "/${Routes.ADD_EDIT_TASK}/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.path<String>("taskId")
                val addEditTaskViewModel = viewModel(
                    keys = listOf(
                        "title",
                        "description",
                        "timestamp",
                    )
                ) { savedState ->
                    AddEditTaskViewModel(taskId, savedState)
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