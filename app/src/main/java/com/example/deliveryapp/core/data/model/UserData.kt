package com.example.deliveryapp.core.data.model

/**
 * Data class representing comprehensive information about a user, including their
 * dietary preferences and preferences for specific products.
 *
 * @property isVegan Indicates whether the user is a vegan.
 * @property isVegetarian Indicates whether the user is a vegetarian.
 * @property isGlutenFree Indicates whether the user eats gluten-free.
 * @property isDairyFree Indicates whether the user eats dairy-free.
 * @property isLowCarb Indicates whether the user eats low-carb.
 * @property isOrganic Indicates whether the user eats organic.
 * @property isHalal Indicates whether the user eats halal.
 * @property isPescetarian Indicates whether the user is pescetarian.
 * @property isGmoFree Indicates whether the user eats GMO-free.
 * @property userId The unique identifier of the user.
 * @property favoriteProductsIds A list of product IDs that the user has marked as favorites.
 * @property bookmarkedRecipeIds A list of recipe IDs that the user has bookmarked.
 */
data class UserData(
    val isVegan: Boolean,
    val isVegetarian: Boolean,
    val isGlutenFree: Boolean,
    val isDairyFree: Boolean,
    val isLowCarb: Boolean,
    val isOrganic: Boolean,
    val isHalal: Boolean,
    val isPescetarian: Boolean,
    val isGmoFree: Boolean,
    val userId: Int,
    val favoriteProductsIds: List<Int>,
    val bookmarkedRecipeIds: List<Int>,

)