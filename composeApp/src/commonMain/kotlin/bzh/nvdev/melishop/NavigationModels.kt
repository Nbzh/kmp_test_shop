package bzh.nvdev.melishop

import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.data.Category
import bzh.nvdev.melishop.data.fakeCategories
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlin.random.Random

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    // It's possible to pop multiple screens at a time on iOS
    fun onBackClicked(toIndex: Int)

    // Defines all possible child components
    sealed class Child {
        class ListChild(
            val categoryComponent: CategoryListComponent,
            val articleComponent: ArticleListComponent
        ) : Child()

        class DetailsChild(val component: ArticleComponent) : Child()
    }
}

interface CategoryListComponent {

    val model: MutableValue<Model>

    data class Model(
        val items: List<Category>,
    )
}

interface ArticleListComponent {

    val model: MutableValue<Model>

    data class Model(
        val items: List<Article>,
    )

    suspend fun filter(filterCategories: List<String>)

    fun onItemClicked(item: Article)
}

interface ArticleComponent {
    val model: Value<Model>

    data class Model(
        val item: Article,
    )

    fun onBackPressed()
}


class FoodListComponentImpl(
    componentContext: ComponentContext,
    private val onItemSelected: (item: Article) -> Unit,
) : ArticleListComponent, ComponentContext by componentContext {

    private val allArticles = with(Random.nextInt(20, 45)) {
        mutableListOf<Article>().apply {
            for (i in 0..this@with) {
                add(fakeArticle)
            }
        }
    }

    override suspend fun filter(filterCategories: List<String>) {
        withContext(Dispatchers.Default) {
            model.value = ArticleListComponent.Model(
                allArticles.filter { a -> a.category.id in filterCategories }
                    .sortedBy { a -> a.name }
            )
        }
    }

    override val model: MutableValue<ArticleListComponent.Model> =
        MutableValue(ArticleListComponent.Model(listOf()))

    override fun onItemClicked(item: Article) {
        onItemSelected(item)
    }
}

class FoodComponentImpl(
    componentContext: ComponentContext,
    article: Article,
    private val onFinished: () -> Unit,
) : ArticleComponent, ComponentContext by componentContext {

    override val model: Value<ArticleComponent.Model> =
        MutableValue(ArticleComponent.Model(article))

    override fun onBackPressed() = onFinished()
}

class CategoryListComponentImpl(
    componentContext: ComponentContext,
) : CategoryListComponent, ComponentContext by componentContext {
    override val model: MutableValue<CategoryListComponent.Model> =
        MutableValue(CategoryListComponent.Model(fakeCategories))
}

class DefaultRootComponent(componentContext: ComponentContext) :
    RootComponent, ComponentContext by componentContext {

    private val nav = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.List,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) { // 3
        Config.List -> RootComponent.Child.ListChild(
            CategoryListComponentImpl(componentContext = componentContext),
            FoodListComponentImpl(componentContext = componentContext){ article ->
                nav.pushNew(Config.Detail(article))
            }
        )

        is Config.Detail -> RootComponent.Child.DetailsChild(
            FoodComponentImpl(
                componentContext = componentContext,
                article = config.article,
                onFinished = {
                    nav.pop()
                },
            )
        )
    }

    override fun onBackClicked(toIndex: Int) {
        nav.popTo(toIndex)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object List : Config

        @Serializable
        data class Detail(val article: Article) : Config
    }
}