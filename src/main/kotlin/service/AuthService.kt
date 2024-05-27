package service

import io.ktor.client.*
import kotlinx.coroutines.flow.Flow
import model.AsyncResult
import model.request.LoginRequest
import model.request.RegisterRequest
import model.response.MessageResponse
import model.response.UserInfoResponse

interface AuthService {
    fun register(request: RegisterRequest): Flow<AsyncResult<MessageResponse>>
    fun login(request: LoginRequest): Flow<AsyncResult<UserInfoResponse>>
}