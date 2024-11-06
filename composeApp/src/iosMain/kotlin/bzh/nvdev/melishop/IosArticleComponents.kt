package bzh.nvdev.melishop

import androidx.compose.runtime.Composable
import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.data.ArticleListComponent
import bzh.nvdev.melishop.data.CategoryListComponent

@Composable
actual fun ArticleListPage(
    categoryListComponent: CategoryListComponent,
    articleListComponent: ArticleListComponent,
    selection: (Article) -> Unit
) {
}