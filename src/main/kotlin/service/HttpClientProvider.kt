package service

import io.ktor.client.*
import io.ktor.http.*

interface HttpClientProvider {
    fun getClient(): HttpClient
    fun provideAuth(cookie: Cookie)
}