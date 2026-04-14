package com.example.deliveryapp.core.data.model

import androidx.annotation.StringRes
import com.example.deliveryapp.R

/**
 * Defines the categories available for sorting recipes within the application.
 *
 * @property label The string resource ID used for displaying the category name in the user interface.
 * @property apiKey The string value used as a parameter when making requests to the remote API.
 */
enum class RecipeSortingCategory (@StringRes val label: Int, val apiKey: String) {
    POPULARITY (R.string.popularity, "popularity"),
    DIFFICULTY (R.string.difficulty, "difficulty"),
    AMOUNT_OF_TIME (R.string.amount_of_time, "time")

}