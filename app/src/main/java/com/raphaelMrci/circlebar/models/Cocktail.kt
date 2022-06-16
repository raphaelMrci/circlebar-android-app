package com.raphaelMrci.circlebar.models

data class Cocktail(
    var name: String? = null,
    val id: Int? = null,
    var icon: Int? = null,
    val recipe: MutableList<RecipeItem>? = null
)