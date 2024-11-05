package bzh.nvdev.melishop.data

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    var id: String,
    val category: Category,
    val name: String,
    val image: String,
    val description: String,
    val isVeggies: Boolean,
    val price: Double,
    val priceUnit: String,
    val labels: List<Label>
)

@Serializable
data class Label(
    val id: String,
    val name: String,
    val image: String
)

@Serializable
data class Category(
    val id: String,
    val name: String,
    val image: String,
    val color: String
)