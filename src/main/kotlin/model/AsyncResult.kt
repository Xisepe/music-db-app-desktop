package model

import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import model.response.ErrorResponse

sealed interface AsyncResult<out T> {
    data class Success<T>(val data: T) : AsyncResult<T>
    data class Error(val error: ErrorResponse) : AsyncResult<Nothing>
    data object Loading : AsyncResult<Nothing>
}

fun <T, R> AsyncResult<T>.mapSuccess(transform: (T) -> R): AsyncResult<R> {
    return when (this) {
        is AsyncResult.Loading -> AsyncResult.Loading
        is AsyncResult.Success -> AsyncResult.Success(transform(data))
        is AsyncResult.Error -> AsyncResult.Error(error)
    }
}

fun <T> Flow<T>.asAsyncResult(): Flow<AsyncResult<T>> {
    return this
        .map<T, AsyncResult<T>> {
            AsyncResult.Success(it)
        }
        .onStart { emit(AsyncResult.Loading) }
        .catch {
            val response = (it as ClientRequestException).response.body<ErrorResponse>()
            emit(AsyncResult.Error(response))
        }
}