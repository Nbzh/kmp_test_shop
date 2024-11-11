package bzh.nvdev.melishop.api

import bzh.nvdev.melishop.apiKey
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorClient {
    lateinit var httpClient: HttpClient

    fun <T : HttpClientEngineConfig> initClient(
        engine: HttpClientEngineFactory<T>,
        language: String
    ) {
        httpClient = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            defaultRequest {
                header(HttpHeaders.AcceptLanguage, language)
                header("X-API-KEY", apiKey)
                url {
                    protocol = URLProtocol.HTTP
                    host = "localhost:8080"
                }
            }
        }
    }

    fun updateAuthorizationHeader(token: String) {
        httpClient = httpClient.config {
            defaultRequest {
                headers.remove(HttpHeaders.Authorization)
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}