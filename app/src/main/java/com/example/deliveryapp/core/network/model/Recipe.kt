package com.example.deliveryapp.core.network.model

import com.example.deliveryapp.core.data.model.Recipe
import com.example.deliveryapp.core.data.model.Ingredient
import kotlinx.serialization.Serializable

/**
 * Represents a recipe fetched from the network.
 *
 * @property id The unique identifier of the recipe.
 * @property name The name of the recipe.
 * @property preparationTime The time required to prepare the recipe, in minutes.
 * @property preparationDescription Describing the preparation steps.
 * @property difficulty The difficulty level of the recipe (e.g., 1-5).
 * @property popularity A numerical representation of the recipe's popularity.
 * @property servings The number of servings the recipe yields.
 * @property ingredients A list of [NetworkIngredient] objects required for the recipe.
 * @property imageUrl The URL of an image associated with the recipe.
 * @property calories The total number of calories in the recipe.
 * @property carbs The total number of carbohydrates in the recipe.
 * @property protein The total number of protein in the recipe.
 * @property fat The total number of fat in the recipe.
 * @property fiber The total number of fiber in the recipe.
 */
@Serializable
data class NetworkRecipe(
    val id: Int,
    val name: String,
    val preparationTime: Int,
    val preparationDescription: String,
    val difficulty: Int,
    val popularity: Float,
    val servings: Int,
    val ingredients: List<NetworkIngredient>,
    val imageUrl: String,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val fiber: Int,
)

/**
 * Converts a [NetworkRecipe] to a [Recipe] domain model.
 *
 * This extension function maps the fields from the network DTO to the corresponding
 * fields in the domain model, including converting any nested [NetworkIngredient] objects
 * to their [Ingredient] domain model counterparts.
 *
 * @return A [Recipe] object representing the domain model of the recipe.
 */
fun NetworkRecipe.asModel(): Recipe {
    return Recipe(
        id = this.id,
        name = this.name,
        preparationTime = this.preparationTime,
        preparationDescription = parsePreparationDescription(this.preparationDescription),
        difficulty = this.difficulty,
        popularity = this.popularity,
        servings = this.servings,
        ingredients = this.ingredients.map { it.asModel() },
        imageUrl = this.imageUrl,
        calories = this.calories,
        carbs = this.carbs,
        protein = this.protein,
        fat = this.fat,
        fiber = this.fiber
    )
}

/**
 * Parses a raw preparation description string into a list of individual steps.
 *
 * This function splits the input string by newline characters and trims any leading or
 * trailing whitespace from each resulting step.
 *
 * @param preparationDescription The raw string containing steps separated by newlines.
 * @return A list of strings, where each element represents a single preparation step.
 */
fun parsePreparationDescription(preparationDescription: String): List<String> {
    return preparationDescription
        .split('\n')
        .map { it.trim() }
}
