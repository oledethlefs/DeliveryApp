package com.example.deliveryapp.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a recipe with its details.
 *
 * @property id The unique identifier of the recipe.
 * @property name The name of the recipe.
 * @property preparationTime The time required to prepare the recipe, in minutes.
 * @property preparationDescription A list of strings describing the preparation steps.
 * @property difficulty The difficulty level of the recipe (e.g., 0 for easy, 2 for hard).
 * @property popularity A float value indicating the popularity of the recipe.
 * @property servings The number of servings the recipe yields.
 * @property ingredients A list of [Ingredient] objects required for the recipe.
 * @property imageUrl The URL of an image representing the recipe.
 * @property calories The total number of calories in the recipe.
 * @property carbs The total number of carbohydrates in the recipe.
 * @property protein The total number of protein in the recipe.
 * @property fat The total number of fat in the recipe.
 * @property fiber The total number of fiber in the recipe.
 */
@Parcelize
data class Recipe(
    val id: Int,
    val name: String,
    val preparationTime: Int,
    val preparationDescription: List<String>,
    val difficulty: Int,
    val popularity: Float,
    val servings: Int,
    val ingredients: List<Ingredient>,
    val imageUrl: String,
    val calories: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val fiber: Int,
) : Parcelable