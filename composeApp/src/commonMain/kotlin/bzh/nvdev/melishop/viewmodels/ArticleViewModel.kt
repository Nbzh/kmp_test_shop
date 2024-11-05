package bzh.nvdev.melishop.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.data.Category
import bzh.nvdev.melishop.data.fakeCategories
import bzh.nvdev.melishop.fakeArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ArticleViewModel : ViewModel() {

    private val allArticles = with(Random.nextInt(20, 45)) {
        mutableListOf<Article>().apply {
            for (i in 0..this@with) {
                add(fakeArticle)
            }
        }
    }
    val articles = mutableStateListOf<Article>()
    val categories = mutableStateListOf<Category>()

    fun setCategories() {
        categories.clear()
        categories.addAll(fakeCategories)
    }

    suspend fun filter(filterCategories: List<String>) {
        withContext(Dispatchers.Default) {
            articles.clear()
            articles.addAll(allArticles.filter { a -> a.category.id in filterCategories }
                .sortedBy { a -> a.name })
        }
    }

    suspend fun getArticle(articleId: String): Article? =
        withContext(Dispatchers.Default) {
            return@withContext allArticles.find { a -> a.id == articleId }
        }
}