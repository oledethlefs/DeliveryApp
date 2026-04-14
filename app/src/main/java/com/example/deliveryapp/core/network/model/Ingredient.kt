package com.example.deliveryapp.core.network.model

import com.example.deliveryapp.core.data.model.Ingredient
import kotlinx.serialization.Serializable

/**
 * Represents an ingredient as received from the network.
 *
 * @property name The name of the ingredient.
 * @property unit The unit of measurement for the ingredient (e.g., "grams", "ml", "pcs").
 * @property quantity The amount of the ingredient.
 */
@Serializable
data class NetworkIngredient(
    val name: String,
    val unit: String,
    val quantity: Int
)

/**
 * Converts a [NetworkIngredient] to an [Ingredient] model.
 *
 * This function maps the properties of a network DTO (Data Transfer Object)
 * representing an ingredient to the corresponding properties of the domain model
 * for an ingredient.
 *
 * @return An [Ingredient] object.
 */
fun NetworkIngredient.asModel(): Ingredient {
    return Ingredient(
        name = this.name,
        unit = this.unit,
        quantity = this.quantity
    )
}
