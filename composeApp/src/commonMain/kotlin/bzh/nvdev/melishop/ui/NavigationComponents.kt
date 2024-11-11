package bzh.nvdev.melishop.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import bzh.nvdev.melishop.ArticleDetailPage
import bzh.nvdev.melishop.ArticleListPage
import bzh.nvdev.melishop.api.ApiResult
import bzh.nvdev.melishop.api.callCategories
import bzh.nvdev.melishop.data.CategoryListComponent
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
            is RootComponent.Child.ListChild -> {
                var debugText by remember { mutableStateOf("")}
                LaunchedEffect("OnStart"){
                    callCategories().also { resp ->
                        debugText = resp.toString()
                        if(resp is ApiResult.Success){
                            child.categoryComponent.model.value = CategoryListComponent.Model(resp.data)
                        }
                    }
                }
                Column {
                    Text(debugText)
                    ArticleListPage(
                        categoryListComponent = child.categoryComponent,
                        articleListComponent = child.articleComponent
                    ) { article -> child.articleComponent.onItemClicked(article) }
                }

            }

            is RootComponent.Child.DetailsChild ->
                ArticleDetailPage(articleComponent = child.component)
        }
    }
}