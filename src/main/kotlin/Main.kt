import androidx.compose.desktop.ui.tooling.preview.Preview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import screen.RegisterScreen
import service.AuthService
import service.HttpClientProvider
import service.impl.AuthServiceImpl
import service.impl.HttpClientProviderImpl
import viewModel.AuthViewModel

@Composable
@ExperimentalMaterial3Api
@Preview
fun App() {


}
@ExperimentalMaterial3Api
fun main() = application {
    val clientProvider: HttpClientProvider = HttpClientProviderImpl()
    val authService: AuthService = AuthServiceImpl(clientProvider.getClient())

    Window(onCloseRequest = ::exitApplication) {
        MaterialTheme {
            RegisterScreen(
                navController = rememberNavController(),
                authViewModel = viewModel { AuthViewModel(authService) }
            )
        }
    }
}
