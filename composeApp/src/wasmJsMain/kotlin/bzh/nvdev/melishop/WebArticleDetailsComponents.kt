package bzh.nvdev.melishop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.viewmodels.ArticleViewModel
import coil3.compose.AsyncImage
import melishop.composeapp.generated.resources.Res
import melishop.composeapp.generated.resources.ic_add
import melishop.composeapp.generated.resources.ic_remove
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun ArticleDetailPage(articleViewModel: ArticleViewModel, articleId: String) {
    var article by remember { mutableStateOf<Article?>(null) }
    LaunchedEffect(articleId) {
        article = articleViewModel.getArticle(articleId)
    }
    BoxWithConstraints {
        article?.also {
            ArticleDetailContent(screenWidth = maxWidth, article = it)
        }
    }
}

@Composable
fun ArticleDetailContent(screenWidth: Dp, article: Article) {
    val screenWithoutSpaces = screenWidth - 32.dp * 3
    val leftWith = screenWithoutSpaces / 3
    val rightWith = screenWithoutSpaces - leftWith
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.width(leftWith)) {
            Card(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = article.image,
                    contentDescription = article.name,
                )
            }
        }
        Column(modifier = Modifier.width(rightWith)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = article.name, style = MaterialTheme.typography.h4)
                    Text(text = article.description, style = MaterialTheme.typography.body1)
                    ChipList(labels = article.labels)
                }
            }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    val priceAnnotatedString = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.h4.toSpanStyle()
                                .copy(fontWeight = FontWeight.Bold)
                        ) {
                            append("${article.price} €")
                        }
                        withStyle(style = MaterialTheme.typography.body1.toSpanStyle()) {
                            append(" / ${article.priceUnit}")
                        }
                    }
                    Text(text = priceAnnotatedString)
                    var quantity by remember { mutableStateOf(1) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            value = quantity.toString(),
                            onValueChange = { newValue ->
                                val quantityValue = newValue.toIntOrNull()
                                quantity = quantityValue?.coerceAtLeast(1) ?: 1
                            },
                            label = {
                                Text(text = "Quantity")
                            },
                            leadingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_remove),
                                        contentDescription = "Decrease quantity"
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_add),
                                        contentDescription = "Increase quantity"
                                    )
                                }
                            }
                        )
                        val totalPriceAnnotatedString = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.body1.toSpanStyle()
                                    .copy(fontWeight = FontWeight.Bold)
                            ) {
                                append("total TTC")
                            }
                            withStyle(
                                style = MaterialTheme.typography.h4.toSpanStyle()
                                    .copy(fontWeight = FontWeight.Bold)
                            ) {
                                append(" ${article.price * quantity} €")
                            }
                        }
                        Text(text = totalPriceAnnotatedString)
                    }
                }
            }
        }
    }
}