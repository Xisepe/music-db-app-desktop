package service.impl

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import model.AsyncResult
import model.asAsyncResult
import model.request.LoginRequest
import model.request.RegisterRequest
import model.response.MessageResponse
import model.response.UserInfoResponse
import service.AuthService
import service.HttpClientProvider

class AuthServiceImpl(
    private val clientProvider: HttpClientProvider
) : AuthService {
    override fun register(request: RegisterRequest): Flow<AsyncResult<MessageResponse>> = flow {
        val response = clientProvider.getClient().post("/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        emit(response.body<MessageResponse>())
    }.asAsyncResult()

    private fun provideCookie(cookie: Cookie) {
        clientProvider.provideAuth(cookie)
    }

    override fun login(request: LoginRequest): Flow<AsyncResult<UserInfoResponse>> = flow {
        val response = clientProvider.getClient().post("/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        val cookie = response.setCookie()
            .first { it.name == "key" }
        provideCookie(cookie)
        emit(response.body<UserInfoResponse>())
    }.asAsyncResult()
}