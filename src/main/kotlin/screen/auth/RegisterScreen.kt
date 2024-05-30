package screen.auth

import BottomNavItem
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
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
import viewModel.AuthViewModel

@Composable
@Preview
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val focusManager = LocalFocusManager.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = usernameError != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = {
                usernameError = if (authViewModel.isValidUsername(username)) {
                    focusManager.moveFocus(FocusDirection.Down)
                    null
                } else {
                    "Username has to be in lowercase. Allowed only letters and digits. Username length should be under 64 symbols"
                }
            })
        )
        if (usernameError != null) {
            Text(usernameError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

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
            }),
            leadingIcon = { Icons.Default.Mail }
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
            }),
            leadingIcon = { Icons.Default.Lock }
        )
        if (passwordError != null) {
            Text(passwordError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Button(onClick = {
            usernameError = if (authViewModel.isValidUsername(username)) {
                null
            } else {
                "Username has to be in lowercase. Allowed only letters and digits. Username length should be under 64 symbols"
            }

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

            if (usernameError == null && emailError == null && passwordError == null) {
                scope.launch {
                    authViewModel.register(username, email, password)
                    navController.navigate(BottomNavItem.Home.route)
                }
            }
        }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text("Register")
        }
        TextButton(onClick = { navController.navigateUp() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Already have an account? Login")
        }
    }
}