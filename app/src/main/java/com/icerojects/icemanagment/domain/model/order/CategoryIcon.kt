package com.icerojects.icemanagment.domain.model.order

/**
 * Domain model representing a category icon
 */
data class CategoryIcon(
    val id: String = "",
    val name: String = "",
    val iconName: String = ""
)

/**
 * Predefined category icons
 */
object CategoryIcons {
    val ICONS = listOf(
        CategoryIcon(id = "1", name = "Helado", iconName = "icecream"),
        CategoryIcon(id = "2", name = "Postre", iconName = "cake"),
        CategoryIcon(id = "3", name = "Bebida", iconName = "local_drink"),
        CategoryIcon(id = "4", name = "Extras", iconName = "add_circle"),
        CategoryIcon(id = "5", name = "Combo", iconName = "fastfood"),
        CategoryIcon(id = "6", name = "Promoción", iconName = "star")
    )
}