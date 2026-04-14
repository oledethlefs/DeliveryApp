package com.example.deliveryapp.core.data.model

/**
 * Represents a filtering criterion for products within the delivery application.
 *
 * @property id The unique identifier for the filtering criterion.
 * @property name The human-readable name of the filtering criterion.
 * @property isBrandFilter A flag indicating whether this criterion is a brand filter.
 *
 */
data class ProductFilter(
    val id: Int,
    val name: String,
    val isBrandFilter: Boolean,
)