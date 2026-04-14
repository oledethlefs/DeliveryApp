package com.example.deliveryapp.core.data.model

import androidx.annotation.StringRes
import com.example.deliveryapp.R

/**
 * Represents the available criteria for sorting products in the catalog.
 *
 * Each constant maps a UI-friendly label to a specific key required by the backend API.
 *
 * @property label The string resource ID used for displaying the category name in the UI.
 * @property apiKey The string value sent to the API to specify the sort order.
 */
enum class ProductSortingCategory (@StringRes val label: Int, val apiKey: String) {
    POPULARITY (R.string.popularity, "popularity"),
    PRICE_ASCENDING (R.string.price_ascending, "price_asc"),
    PRICE_DESCENDING (R.string.price_descending, "price_desc"),
    ON_SALE (R.string.on_sale, "on_sale")




}