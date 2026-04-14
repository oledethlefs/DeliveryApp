package com.example.deliveryapp.core.data.model

import androidx.annotation.StringRes
import com.example.deliveryapp.R

/**
 * Represents various dietary restrictions and preferences used within the application.
 *
 * Each constant maps a user-friendly display name to a specific string key required by
 * the backend API for filtering search results.
 *
 * @property label The string resource ID for the display name of the diet.
 * @property apiKey The string value used for API requests and data mapping.
 */
enum class Diet (@StringRes val label: Int, val apiKey: String){
    VEGAN(R.string.vegan, "vegan"),
    VEGETARIAN(R.string.vegetarian, "vegetarian"),
    HALAL(R.string.halal, "halal"),
    GLUTEN_FREE(R.string.gluten_free, "gluten-free"),
    ORGANIC(R.string.organic, "organic"),
    LOW_CARB(R.string.low_carb, "low-carb"),
    PESCETARIAN(R.string.pescetarian, "pescetarian"),
    GMO_FREE(R.string.gmo_free, "gmo-free"),
    DAIRY_FREE(R.string.dairy_free, "dairy-free"),
}