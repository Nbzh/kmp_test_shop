package bzh.nvdev.melishop

import androidx.compose.runtime.Composable
import bzh.nvdev.melishop.data.Article

@Composable
actual fun ArticleListPage(
    categoryListComponent: CategoryListComponent,
    articleListComponent: ArticleListComponent,
    selection: (Article) -> Unit
) {
}