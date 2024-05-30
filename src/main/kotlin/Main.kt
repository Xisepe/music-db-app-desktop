import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import model.response.ErrorResponse
import screen.AsyncResultHandler
import screen.auth.RegisterScreen
import screen.AuthNav
import screen.auth.LoginScreen
import service.AuthService
import service.HttpClientProvider
import service.impl.AuthServiceImpl
import service.impl.HttpClientProviderImpl
import viewModel.AuthViewModel


enum class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    Playlists("playlists", Icons.Default.LibraryMusic, "Playlists"),
    Home("home", Icons.Default.Home, "Home"),
    Search("search", Icons.Default.Search, "Search"),
    Albums("albums", Icons.Default.Album, "Albums")
}

@Composable
@ExperimentalMaterial3Api
@Preview
fun BottomNavigationBar(
    navController: NavController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomNavigation {
        BottomNavItem.entries.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                icon = { Icon(item.icon, contentDescription = null) },
                label = { item.label },
                onClick = {
                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.startDestinationRoute!!)
//                        launchSingleTop = true
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().navigatorName) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }

                }
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun App(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel
) {
//    // Get current back stack entry
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    // Get the name of the current screen
//    val currentScreen = BottomNavItem.valueOf(
//        backStackEntry?.destination?.route ?: BottomNavItem.Home.route
//    )

    Scaffold(
        bottomBar = { if (authViewModel.isAuthenticated.value) BottomNavigationBar(navController) },
    ) {
        NavHost(
            navController = navController,
            startDestination = if (!authViewModel.isAuthenticated.value) AuthNav.Login.route else BottomNavItem.Home.route
        ) {
            composable(AuthNav.Login.route) {
                LoginScreen(navController, authViewModel)
            }
            composable(AuthNav.Register.route) {
                RegisterScreen(navController, authViewModel)
            }
            composable(BottomNavItem.Home.route) {
                AsyncResultHandler(
                    authViewModel.userInfo.value,
                    onError = { BackErrorHandler(navController, it) }
                ) {
                    Text(it.toString())
                }
            }
            composable(BottomNavItem.Playlists.route) {

            }
            composable(BottomNavItem.Albums.route) {

            }
            composable(BottomNavItem.Search.route) {

            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun BackErrorHandler(navController: NavHostController, error: ErrorResponse) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = error.msg,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
        TextButton(onClick = {
            navController.navigateUp()
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Back")
        }
    }
}

@ExperimentalMaterial3Api
fun main() = application {
    val clientProvider: HttpClientProvider = HttpClientProviderImpl()
    val authService: AuthService = AuthServiceImpl(clientProvider)

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            val authViewModel = viewModel { AuthViewModel(authService) }
            App(
                authViewModel = authViewModel
            )
        }
    }
}
