package bzh.nvdev.melishop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import bzh.nvdev.melishop.data.Category
import bzh.nvdev.melishop.utils.hexToColor
import coil3.compose.AsyncImage

@Composable
fun CategoryFilter(
    image: String,
    name: String,
    color: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Card(modifier = Modifier.size(52.dp), shape = CircleShape) {
            AsyncImage(
                modifier = Modifier.fillMaxSize().clip(CircleShape),
                model = image,
                contentDescription = name,
                alpha = if (selected) ContentAlpha.high else ContentAlpha.disabled
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.body2,
            color = color.hexToColor()
                .copy(alpha = if (selected) ContentAlpha.high else ContentAlpha.disabled)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryFilterRow(
    modifier: Modifier,
    categories: List<Category>,
    visible: List<String>,
    onClick: (String, Boolean) -> Unit
) {
    if (categories.isNotEmpty())
        FlowRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            categories.forEach { category ->
                val selected = category.id in visible
                CategoryFilter(
                    image = category.image,
                    name = category.name,
                    color = category.color,
                    selected = selected,
                ) {
                    onClick(category.id, !selected)
                }
            }
        }
}