package bzh.nvdev.melishop.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bzh.nvdev.melishop.data.RootComponent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(rootComponent: RootComponent) {
    MaterialTheme {
        RootContent(modifier = Modifier.fillMaxWidth(), component = rootComponent)
    }
}