package bzh.nvdev.melishop.data

import org.jetbrains.compose.resources.DrawableResource

data class Article(
    var id : String,
    val category : Category,
    val name : String,
    val image : String,
    val description : String,
    val isVeggies : Boolean,
    val price : Double,
    val priceUnit : String,
    val labels: List<Label>
)

data class Label(
    val id : String,
    val name : String,
    val image : String
)

data class Category(
    val id : String,
    val name: String,
    val image : String,
    val color : String
)