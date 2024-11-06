package bzh.nvdev.melishop

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bzh.nvdev.melishop.data.Article
import bzh.nvdev.melishop.data.FoodDescriptionGenerator
import bzh.nvdev.melishop.data.FoodNameGenerator
import bzh.nvdev.melishop.data.fakeCategories
import bzh.nvdev.melishop.data.fakeLabels
import bzh.nvdev.melishop.utils.formatToTwoDecimalPlaces
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    MaterialTheme {
        RootContent(modifier = Modifier.fillMaxWidth(), component = rootComponent)
    }
}