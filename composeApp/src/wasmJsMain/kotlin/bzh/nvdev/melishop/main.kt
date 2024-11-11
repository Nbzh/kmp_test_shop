package bzh.nvdev.melishop

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import bzh.nvdev.melishop.api.KtorClient
import bzh.nvdev.melishop.data.DefaultRootComponent
import bzh.nvdev.melishop.ui.App
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.webhistory.DefaultWebHistoryController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.essenty.lifecycle.stop
import io.ktor.client.engine.js.Js
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.Document

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val rootComponent = DefaultRootComponent(
        componentContext = DefaultComponentContext(lifecycle),
        webHistoryController = DefaultWebHistoryController()
    )
    lifecycle.attachToDocument()
    KtorClient.initClient(Js, getSystemLocaleLanguage())
    ComposeViewport(document.body!!) {
        App(rootComponent = rootComponent)
    }
}

private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (visibilityState(document) == "visible") {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

// Workaround for Document#visibilityState not available in Wasm
@JsFun("(document) => document.visibilityState")
private external fun visibilityState(document: Document): String