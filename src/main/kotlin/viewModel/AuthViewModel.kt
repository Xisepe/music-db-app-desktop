package viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import model.AsyncResult
import model.request.LoginRequest
import model.request.RegisterRequest
import model.response.MessageResponse
import model.response.UserInfoResponse
import service.AuthService

class AuthViewModel(
    private val authService: AuthService
): ViewModel() {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _registerResult = MutableStateFlow<AsyncResult<MessageResponse>>(AsyncResult.Loading)
    val registerRequest:StateFlow<AsyncResult<MessageResponse>> = _registerResult.asStateFlow()

    private val _userInfo = MutableStateFlow<AsyncResult<UserInfoResponse>>(AsyncResult.Loading)
    val userInfo = _userInfo.asStateFlow()
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authService.login(LoginRequest(email, password))
                .collect {
                    _userInfo.value = it
                }
        }
        _isAuthenticated.value = true
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            authService.register(RegisterRequest(email, username, password))
                .collect {
                    _registerResult.value = it
                }
        }
        if (registerRequest.value is AsyncResult.Success) {
            login(email, password)
        }
    }
}