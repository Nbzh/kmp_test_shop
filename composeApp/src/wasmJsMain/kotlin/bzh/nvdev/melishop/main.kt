package bzh.nvdev.melishop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.lifecycle.viewmodel.compose.viewModel
import bzh.nvdev.melishop.viewmodels.ArticleViewModel
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        ArticleListPage(viewModel { ArticleViewModel() })
    }
}