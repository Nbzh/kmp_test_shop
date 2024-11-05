package bzh.nvdev.melishop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ContextualFlowRowOverflowScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bzh.nvdev.melishop.data.Label
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LabelChip(label: Label) {
    Chip(onClick = {}) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(16.dp),
                model = label.image,
                contentDescription = label.name,
                contentScale = ContentScale.Fit
            )
            Text(text = label.name, style = MaterialTheme.typography.caption)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalLayoutApi::class)
@Composable
fun ChipList(labels: List<Label>) {
    val totalCount = labels.size
    var maxLines by remember { mutableStateOf(1) }
    val moreOrCollapseIndicator = @Composable { scope: ContextualFlowRowOverflowScope ->
        val remainingItems = totalCount - scope.shownItemCount
        Chip(onClick = {
            if (remainingItems == 0) {
                maxLines = 1
            } else {
                maxLines += 2
            }
        }) {
            Text(if (remainingItems > 0) "+$remainingItems" else "Less")
        }
    }
    ContextualFlowRow(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxWidth(1f)
            .padding(16.dp)
            .wrapContentHeight(align = Alignment.Top),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        maxLines = maxLines,
        overflow = ContextualFlowRowOverflow.expandOrCollapseIndicator(
            minRowsToShowCollapse = 3,
            expandIndicator = moreOrCollapseIndicator,
            collapseIndicator = moreOrCollapseIndicator
        ),
        itemCount = totalCount
    ) { index ->
        LabelChip(labels[index])
    }
}