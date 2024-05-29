import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import screen.RegisterScreen
import screen.Screens
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
                        popUpTo(navController.graph.startDestinationRoute!!)
                        launchSingleTop = true
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
            startDestination = if (!authViewModel.isAuthenticated.value) Screens.Login.route else BottomNavItem.Home.route
        ) {
            composable(Screens.Login.route) {
                LoginScreen(navController, authViewModel)
            }
            composable(Screens.Register.route) {
                RegisterScreen(navController, authViewModel)
            }
            composable(BottomNavItem.Home.route) {
                Text("Hello, world!")
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
