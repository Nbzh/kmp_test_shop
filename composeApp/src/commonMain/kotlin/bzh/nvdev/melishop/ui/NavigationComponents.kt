package bzh.nvdev.melishop.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bzh.nvdev.melishop.ArticleDetailPage
import bzh.nvdev.melishop.ArticleListPage
import bzh.nvdev.melishop.data.RootComponent
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation

@Composable
fun RootContent(modifier: Modifier = Modifier, component: RootComponent) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(slide()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.ListChild -> ArticleListPage(
                categoryListComponent = child.categoryComponent,
                articleListComponent = child.articleComponent
            ) { article -> child.articleComponent.onItemClicked(article) }

            is RootComponent.Child.DetailsChild ->
                ArticleDetailPage(articleComponent = child.component)
        }
    }
}