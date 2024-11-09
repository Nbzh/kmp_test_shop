package bzh.nvdev.melishop.api

import bzh.nvdev.melishop.data.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun callCategories() =
    withContext(Dispatchers.Default) {
        makeApiGetRequest<List<Category>>("/api/categories")
    }