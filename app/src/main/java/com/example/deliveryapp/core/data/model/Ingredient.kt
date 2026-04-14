package com.example.deliveryapp.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Represents an ingredient with its name, unit of measurement, and quantity.
 *
 * @property name The name of the ingredient.
 * @property unit The unit of measurement for the ingredient (e.g., "grams", "ml", "pieces").
 * @property quantity The amount of the ingredient.
 */
@Parcelize
data class Ingredient(
    val name: String,
    val unit: String,
    val quantity: Int
) : Parcelable