package screen.auth

import BottomNavItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import screen.AuthNav
import viewModel.AuthViewModel

@Composable
fun LoginScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val focusManager = LocalFocusManager.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = emailError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                emailError = if (authViewModel.isValidEmail(email)) {
                    focusManager.moveFocus(FocusDirection.Down)
                    null
                } else {
                    "Invalid email"
                }
            })
        )
        if (emailError != null) {
            Text(emailError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            isError = passwordError != null,
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                passwordError = if (authViewModel.isValidPassword(password)) {
                    null
                } else {
                    "Password length at least 6, at most 32"
                }
            })
        )
        if (passwordError != null) {
            Text(passwordError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Button(onClick = {
            emailError = if (authViewModel.isValidEmail(email)) {
                null
            } else {
                "Invalid email"
            }

            passwordError = if (authViewModel.isValidPassword(password)) {
                null
            } else {
                "Password length at least 6, at most 32"
            }

            if (emailError == null && passwordError == null) {
                scope.launch {
                    authViewModel.login(email, password)
                    navController.navigate(BottomNavItem.Home.route)
                }
            }
        }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Login")
        }

        TextButton(onClick = {
            navController.navigate(AuthNav.Register.route)
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Don't have an account? Register")
        }
    }
}