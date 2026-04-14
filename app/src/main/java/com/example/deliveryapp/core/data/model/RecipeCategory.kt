package com.example.deliveryapp.core.data.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.deliveryapp.R
import com.example.deliveryapp.core.designsystem.icon.Icons

/**
 * Represents the different categories of recipes available in the application.
 *
 * Each category mapping includes UI resources for display and the corresponding
 * key used for backend API queries.
 *
 * @property label The string resource ID for the category's display name.
 * @property icon_unselected The drawable resource ID for the icon when the category is not active.
 * @property icon_selected The drawable resource ID for the icon when the category is active.
 * @property apiKey The string identifier used as a parameter for API requests.
 */
enum class RecipeCategory (@StringRes val label: Int, @DrawableRes val icon_unselected: Int, @DrawableRes val icon_selected: Int, val apiKey: String){
    STARTER (R.string.starter, Icons.starter, Icons.starter_selected, "starter"),
    DINNER (R.string.dinner, Icons.dinner, Icons.dinner_selected, "dinner"),
    DESSERT (R.string.dessert, Icons.dessert, Icons.dessert_selected, "dessert"),
}