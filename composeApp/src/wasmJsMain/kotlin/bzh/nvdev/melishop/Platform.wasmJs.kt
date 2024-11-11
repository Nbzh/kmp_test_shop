package bzh.nvdev.melishop

import kotlinx.browser.window

actual val apiKey : String = "MeliShop_apiKey_validation"

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

fun getSystemLocaleLanguage() : String {
    val navigator = window.navigator
    return navigator.language
}