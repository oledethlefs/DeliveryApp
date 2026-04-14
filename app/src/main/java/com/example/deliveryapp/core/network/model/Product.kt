package com.example.deliveryapp.core.network.model

import com.example.deliveryapp.core.data.model.Product
import kotlinx.serialization.Serializable

/**
 * Represents a product as it is received from the network.
 *
 * @property id The unique identifier of the product.
 * @property name The name of the product.
 * @property brand The brand or manufacturer of the product.
 * @property price The original price of the product.
 * @property reducedPrice The discounted price of the product, if applicable.
 * @property deposit The deposit amount for the product.
 * @property quantity A string representation of the product's quantity (e.g., "1kg", "500g").
 * @property quantityPrice The price per unit of quantity (e.g., "1kg = 4,60€").
 * @property description A detailed description of the product.
 * @property ingredients A string representation of the product's ingredients.
 * @property information Additional information about the product.
 * @property imageUrl The URL of the product's image.
 * @property calories The total number of calories in the product.
 * @property carbs The total number of carbohydrates in the product.
 * @property sugar The total number of sugar in the product.
 * @property protein The total number of protein in the product.
 * @property fat The total number of fat in the product.
 * @property saturatedFat The total number of saturated fat in the product.
 * @property sodium The total number of sodium in the product.
 * @property otherQuantities A list of other quantities or variations of the product.
 * @property nutriScore The nutritional score of the product.
 */
@Serializable
data class NetworkProduct(
    val id: Int,
    val name: String,
    val brand: String,
    val price: Int,
    val reducedPrice: Int,
    val deposit: Int,
    val quantity: String,
    val quantityPrice: String,
    val description: String,
    val ingredients: String,
    val information: String,
    val imageUrl: String,
    val calories: Int,
    val carbs: Float,
    val sugar: Float,
    val protein: Float,
    val fat: Float,
    val saturatedFat: Float,
    val sodium: Float,
    val otherQuantities: List<NetworkProduct>,
    val nutriScore: String
)

/**
 * Converts a [NetworkProduct] to a [Product] model.
 */
fun NetworkProduct.asModel(): Product {
    return Product(
        id = this.id,
        name = this.name,
        brand = this.brand,
        price = this.price,
        reducedPrice = this.reducedPrice,
        deposit = this.deposit,
        quantity = this.quantity,
        quantityPrice = this.quantityPrice,
        description = this.description,
        ingredients = this.ingredients,
        information = this.information,
        imageUrl = this.imageUrl,
        calories = this.calories,
        carbs = this.carbs,
        sugar = this.sugar,
        protein = this.protein,
        fat = this.fat,
        saturatedFat = this.saturatedFat,
        sodium = this.sodium,
        otherQuantities = this.otherQuantities.map { it.asModel() },
        nutriScore = this.nutriScore

    )

}