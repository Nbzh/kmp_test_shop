package bzh.nvdev.melishop

import androidx.compose.runtime.Composable
import bzh.nvdev.melishop.data.ArticleComponent
import bzh.nvdev.melishop.data.ArticleListComponent
import bzh.nvdev.melishop.data.CategoryListComponent
import bzh.nvdev.melishop.data.Article

@Composable
expect fun ArticleListPage(
    categoryListComponent: CategoryListComponent,
    articleListComponent: ArticleListComponent,
    selection: (Article) -> Unit
)

@Composable
expect fun ArticleDetailPage(articleComponent: ArticleComponent)