package bzh.nvdev.melishop.viewmodels

import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.fakeArticle
import kotlin.random.Random

class ArticleViewModel {

    val allArticles = with(Random.nextInt(20, 45)) {
        mutableListOf<Article>().apply {
            for (i in 0..this@with) {
                add(fakeArticle)
            }
        }
    }

    fun getArticle(id: String): Article? =
        allArticles.find { a -> a.id == id }
}