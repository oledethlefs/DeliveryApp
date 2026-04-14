package com.example.deliveryapp.core.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Represents a product with detailed information including pricing, nutritional values, and variants.
 */
@Parcelize
data class Product(
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
    val otherQuantities: List<Product>,
    val nutriScore: String
) : Parcelable