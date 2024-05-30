package service.impl

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import service.HttpClientProvider

class HttpClientProviderImpl : HttpClientProvider {
    private val client = HttpClient(CIO) {
        install(HttpCache)
        install(HttpCookies)
        install(ContentNegotiation) { json() }
        defaultRequest {
            host = "localhost"
            port = 8080
            url {
                protocol = URLProtocol.HTTP
            }
        }
        expectSuccess = true
    }
    override fun getClient(): HttpClient = client
    override fun provideAuth(cookie: Cookie) {
        client.config {
            headers {
                set("Cookie", "${cookie.name}=${cookie.value}")
            }
        }
    }
}