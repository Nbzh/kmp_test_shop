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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.utils.formatToTwoDecimalPlaces
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import melishop.composeapp.generated.resources.Res
import melishop.composeapp.generated.resources.icon_add
import melishop.composeapp.generated.resources.icon_remove
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun ArticleDetailPage(articleComponent: ArticleComponent) {
    val article by articleComponent.model.subscribeAsState()
    BoxWithConstraints {
        ArticleDetailContent(screenWidth = maxWidth, article = article.item)
    }
}

@Composable
fun ArticleDetailContent(screenWidth: Dp, article: Article) {
    val part by remember { mutableStateOf(screenWidth / 15) }
    val leftWith by remember { mutableStateOf(part * 5) }
    val rightWith by remember { mutableStateOf(part * 7) }
    var quantity by remember { mutableStateOf(1) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(part),
        horizontalArrangement = Arrangement.spacedBy(part)
    ) {
        Column(modifier = Modifier.width(leftWith)) {
            Card(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f)) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = article.image,
                    contentDescription = article.name,
                )
            }
        }
        Column(
            modifier = Modifier.width(rightWith),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Card(modifier = Modifier.width(rightWith)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = article.name, style = MaterialTheme.typography.h4)
                    Text(text = article.description, style = MaterialTheme.typography.body1)
                    ChipList(labels = article.labels)
                }
            }
            Card(modifier = Modifier.width(rightWith)) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val priceAnnotatedString = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.h4.toSpanStyle()
                                .copy(fontWeight = FontWeight.Bold)
                        ) {
                            append("${article.price.formatToTwoDecimalPlaces()} €")
                        }
                        withStyle(style = MaterialTheme.typography.body1.toSpanStyle()) {
                            append(" / ${article.priceUnit}")
                        }
                    }
                    Text(text = priceAnnotatedString, modifier = Modifier.align(Alignment.End))
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
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            label = {
                                Text(text = "Quantity")
                            },
                            leadingIcon = {
                                IconButton(onClick = {
                                    quantity = (quantity - 1).coerceAtLeast(1)
                                }) {
                                    Icon(
                                        painter = painterResource(Res.drawable.icon_remove),
                                        contentDescription = "Decrease quantity"
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    quantity = (quantity + 1).coerceAtLeast(1)
                                }) {
                                    Icon(
                                        painter = painterResource(Res.drawable.icon_add),
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
                                append(" ${(article.price * quantity).formatToTwoDecimalPlaces()} €")
                            }
                        }
                        Text(text = totalPriceAnnotatedString)
                    }
                }
            }
        }
    }
}