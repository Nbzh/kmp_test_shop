package bzh.nvdev.melishop

import androidx.compose.runtime.Composable
import bzh.nvdev.melishop.data.Article
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    // It's possible to pop multiple screens at a time on iOS
    fun onBackClicked(toIndex: Int)

    // Defines all possible child components
    sealed class Child {
        class ListChild(val component: ArticleListComponent) : Child()
        class DetailsChild(val component: ArticleComponent) : Child()
    }
}

interface ArticleListComponent{
    val model: Value<Model>

    fun onItemClicked(item: String)

    data class Model(
        val items: List<Article>,
    )
}

interface ArticleComponent{
    val model: Value<Model>

    data class Model(
        val item: Article,
    )
}

class MainRooComponent()

@Composable
fun MainNavigation(){

}