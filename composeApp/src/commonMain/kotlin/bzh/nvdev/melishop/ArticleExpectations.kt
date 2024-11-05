package bzh.nvdev.melishop

import androidx.compose.runtime.Composable
import bzh.nvdev.melishop.viewmodels.ArticleViewModel

@Composable
expect fun ArticleListPage(articleViewModel: ArticleViewModel, selection: (String) -> Unit)

@Composable
expect fun ArticleDetailPage(articleViewModel: ArticleViewModel, articleId: String)