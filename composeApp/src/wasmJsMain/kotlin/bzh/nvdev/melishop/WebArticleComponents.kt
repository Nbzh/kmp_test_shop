package bzh.nvdev.melishop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.data.Label
import bzh.nvdev.melishop.utils.calculateNumberOfColumns
import bzh.nvdev.melishop.utils.formatToTwoDecimalPlaces
import bzh.nvdev.melishop.utils.hexToColor
import bzh.nvdev.melishop.viewmodels.ArticleViewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun ArticleListPage(articleViewModel: ArticleViewModel, selection: (String) -> Unit) {
    LaunchedEffect("OnStart") {
        articleViewModel.setCategories()
    }
    MaterialTheme {
        val articles = articleViewModel.articles
        val categories = articleViewModel.categories
        var visibleCategories by remember { mutableStateOf<List<String>>(listOf()) }
        LaunchedEffect(categories) {
            withContext(Dispatchers.Default) {
                if (visibleCategories.isEmpty()) {
                    visibleCategories = categories.map { it.id }
                }
            }
        }
        LaunchedEffect(visibleCategories) {
            withContext(Dispatchers.Default) {
                articleViewModel.filter(visibleCategories)
            }
        }
        Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            CategoryFilterRow(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                categories = categories,
                visible = visibleCategories
            ) { categoryId, selected ->
                if (selected) visibleCategories += categoryId
                else visibleCategories -= categoryId
            }
            BoxWithConstraints(Modifier.fillMaxWidth().padding(top = 32.dp)) {
                val availableWidth = maxWidth
                ArticleList(
                    screenWidth = availableWidth,
                    modifier = Modifier.align(Alignment.Center),
                    articles = articles,
                    selection = selection
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ArticleList(
    screenWidth: Dp,
    modifier: Modifier,
    articles: List<Article>,
    selection: (String) -> Unit
) {
    val hoverState = remember { mutableStateOf<Article?>(null) } // State for hover
    if (articles.isNotEmpty()) {
        if (screenWidth >= 432.dp) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(
                    calculateNumberOfColumns(
                        screenWidth.value.toInt(),
                        400,
                        16
                    )
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier,
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(articles, key = { it.id }) { article ->
                    val elevation =
                        derivedStateOf { if (hoverState.value == article) 8.dp else 4.dp }
                    ArticleCard(
                        modifier = Modifier
                            .width(400.dp)
                            .onPointerEvent(eventType = PointerEventType.Enter) {
                                hoverState.value = article
                            }
                            .onPointerEvent(eventType = PointerEventType.Exit) {
                                hoverState.value = null
                            },
                        elevation = elevation.value, // Use derived state for elevation
                        image = article.image,
                        name = article.name,
                        category = article.category.name,
                        categoryColor = article.category.color,
                        labels = article.labels,
                        price = article.price,
                        priceUnit = article.priceUnit,
                        onClick = { selection(article.id) }
                    )
                }
            }
        } else {
            LazyColumn {
                items(articles) { article ->
                    val elevation =
                        derivedStateOf { if (hoverState.value == article) 8.dp else 4.dp }
                    ArticleCard(
                        modifier = Modifier.fillMaxWidth(),
                        image = article.image,
                        name = article.name,
                        category = article.category.name,
                        categoryColor = article.category.color,
                        labels = article.labels,
                        price = article.price,
                        priceUnit = article.priceUnit,
                        elevation = elevation.value,
                        onClick = { selection(article.id) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticleCard(
    modifier: Modifier,
    image: String,
    name: String,
    category: String,
    categoryColor: String,
    labels: List<Label>,
    price: Double,
    priceUnit: String,
    elevation: Dp,
    onClick: () -> Unit
) {
    val priceToDisplay = price.formatToTwoDecimalPlaces()
    Card(
        modifier = modifier,
        elevation = elevation,
        onClick = onClick
    ) {
        Column {
            AsyncImage(
                modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
                model = image,
                contentDescription = name,
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp),
                    text = category,
                    style = MaterialTheme.typography.caption.copy(color = categoryColor.hexToColor()),
                )
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = name,
                    style = MaterialTheme.typography.h5
                )
                ChipList(labels = labels)
                Text(
                    modifier = Modifier.align(Alignment.End).padding(top = 4.dp),
                    text = "$priceToDisplay € / $priceUnit",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}