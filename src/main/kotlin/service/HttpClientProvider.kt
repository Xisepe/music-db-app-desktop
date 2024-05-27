package service

import io.ktor.client.*

interface HttpClientProvider {
    fun getClient(): HttpClient
}