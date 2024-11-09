package bzh.nvdev.melishop.api

import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception) : ApiResult<Nothing>()
}

suspend inline fun <reified T> makeApiGetRequest(endpoint: String): ApiResult<T> =
    withContext(Dispatchers.Default) { // Consider using Dispatchers.IO for network operations
        try {
            val response = KtorClient.httpClient.get(endpoint).body<T>()
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e)
        }
    }
