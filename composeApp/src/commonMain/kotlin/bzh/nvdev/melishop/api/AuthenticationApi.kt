package bzh.nvdev.melishop.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun getAnonymousToken(context : Any) =
    withContext(Dispatchers.Default) {
        makeApiGetRequest<String>(
            context = context,
            endpoint = "/api/anonymous-token",
            retry = false,
            transform = false
        )
    }