package bzh.nvdev.melishop.api

import bzh.nvdev.melishop.data.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun callArticles(categories : List<String>?) =
    withContext(Dispatchers.Default) {
        val categoriesFilter = categories.takeIf { f -> !f.isNullOrEmpty() }?.let { c ->
            "?categories=${c.joinToString(separator = ",")}"
        } ?: ""
        makeApiGetRequest<List<Article>>("/api/articles$categoriesFilter")
    }

suspend fun callArticle(articleId: String) =
    withContext(Dispatchers.Default) {
        makeApiGetRequest<Article>("/api/article/$articleId")
    }