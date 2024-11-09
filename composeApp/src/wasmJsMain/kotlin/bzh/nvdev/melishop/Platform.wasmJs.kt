package bzh.nvdev.melishop

import androidx.compose.ui.text.intl.Locale
import kotlinx.browser.window


class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

fun getSystemLocaleLanguage() : String {
    val navigator = window.navigator
    return navigator.language
}