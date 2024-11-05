package bzh.nvdev.melishop.utils

import androidx.compose.ui.graphics.Color

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