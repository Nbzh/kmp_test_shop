package bzh.nvdev.melishop.utils

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun String.hexToColor(): Color {
    return Color(toLong(16) or 0xFF000000)
}

fun Double.formatToTwoDecimalPlaces(): String {
    val integerPart = this.toInt()
    val decimalPart = ((this - integerPart) * 100).toInt()
    return "$integerPart.${decimalPart.toString().padStart(2, '0')}"
}

fun calculateNumberOfColumns(screenWidth: Int, columnWidth: Int, spacing: Int): Int {
    return ((screenWidth + spacing) / (columnWidth + spacing))
}

class SingleValueFlow<T> {
    private val _flow = MutableSharedFlow<T>(replay = 0)
    val flow = _flow.asSharedFlow()

    suspend fun setValue(value: T) {
        suspendCoroutine { continuation ->
            _flow.tryEmit(value)
            continuation.resume(Unit)
        }
    }
}