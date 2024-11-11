package bzh.nvdev.melishop.api

import bzh.nvdev.melishop.signInAnonymouslyIfNeeded
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Exception, val customMessage: String? = null) :
        ApiResult<Nothing>()
}

suspend inline fun <reified T> makeApiGetRequest(
    context: Any,
    endpoint: String,
    retry: Boolean = true,
    transform: Boolean = true
): ApiResult<T> =
    withContext(Dispatchers.Default) { // Consider using Dispatchers.IO for network operations
        requestTry(
            context = context,
            retry = retry,
            request = { KtorClient.httpClient.get(endpoint) }).let {
            if (it is ApiResult.Success)
                if (transform) ApiResult.Success(Json.decodeFromString(it.data))
                else ApiResult.Success(it.data as T)
            else {
                it as ApiResult.Error
                ApiResult.Error(it.exception, it.customMessage)
            }
        }
    }

suspend fun requestTry(
    context: Any,
    retry: Boolean = true,
    request: suspend () -> HttpResponse
): ApiResult<String> {

    suspend fun login(): ApiResult<String> {
        val firebaseToken = signInAnonymouslyIfNeeded(context)
        if (firebaseToken.isNullOrBlank())
            return ApiResult.Error(
                Exception("SignIn anonymous failed, firebaseToken is null or blank")
            )
        val token = "Bearer $firebaseToken"
        KtorClient.updateAuthorizationHeader(token)
        return requestTry(context = context, retry = false, request = request)
    }

    return try {
        val execute = request()
        if (execute.status.isSuccess())
            ApiResult.Success(execute.bodyAsText())
        else {
            if (execute.status.value == 403 && retry) login()
            else ApiResult.Error(Exception("Request failed ${execute.status.value} ${execute.status.description}"))
        }
    } catch (e: RedirectResponseException) {
        // 3xx - responses
        ApiResult.Error(e)
    } catch (e: ClientRequestException) {
        // 4xx - responses
        if (e.response.status.value == 403 && retry) login()
        else ApiResult.Error(e, "${e.response.status.value} retry $retry")
    } catch (e: ServerResponseException) {
        // 5xx - responses
        ApiResult.Error(e)
    } catch (e: Exception) {
        ApiResult.Error(e)
    }
}
