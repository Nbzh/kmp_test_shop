package bzh.nvdev.melishop.api

import bzh.nvdev.melishop.data.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun callArticles(context: Any, categories: List<String>?) =
    withContext(Dispatchers.Default) {
        val categoriesFilter = categories.takeIf { f -> !f.isNullOrEmpty() }?.let { c ->
            "?categories=${c.joinToString(separator = ",")}"
        } ?: ""
        makeApiGetRequest<List<Article>>(
            context = context,
            endpoint = "/api/articles$categoriesFilter"
        )
    }

suspend fun callArticle(context: Any, articleId: String) =
    withContext(Dispatchers.Default) {
        makeApiGetRequest<Article>(context = context, endpoint = "/api/articles/$articleId")
    }